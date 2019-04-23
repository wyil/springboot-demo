package com.reven.uitl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.HtmlUtils;

import com.alibaba.fastjson.JSON;

/**
 * <code>{@link XssHttpServletRequestWrapper}</code>
 * 
 * @author
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private static Logger logger = LoggerFactory.getLogger(XssHttpServletRequestWrapper.class);
    private HttpServletRequest orgRequest = null;
    // 判断是否是上传，上传的请求则忽略
    private boolean isUpData = false;

    /**   
     * @Fields iscleanXss : 是否清除非法的html脚本，false：HtmlUtils.htmlEscape转义字符
     */
    private boolean iscleanXss = true;

    public XssHttpServletRequestWrapper(HttpServletRequest request, boolean iscleanXss) {
        super(request);
        orgRequest = request;
        String contentType = request.getContentType();
        if (null != contentType) {
            isUpData = contentType.startsWith("multipart");
        }
        this.iscleanXss = iscleanXss;
    }

    /**
     * 覆盖getParameter方法，将参数名和参数值都做xss过滤。<br/>
     * 如果需要获得原始的值，则通过super.getParameterValues(name)来获取<br/>
     * getParameterNames,getParameterValues和getParameterMap也可能需要覆盖
     */
    @Override
    public String getParameter(String name) {
        Boolean flag = ("content".equals(name) || name.endsWith("WithHtml"));
        if (flag) {
            return super.getParameter(name);
        }
        name = JsoupUtil.clean(name);
        String value = super.getParameter(name);
        if (StringUtils.isNotBlank(value)) {
            try {
                if (iscleanXss) {
                    value = JsoupUtil.clean(URLDecoder.decode(value, "UTF-8"));
                } else {
                    value = HtmlUtils.htmlEscape(URLDecoder.decode(value, "UTF-8"));
                }
            } catch (UnsupportedEncodingException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return value;
    }

    @Override
    public String[] getParameterValues(String name) {
        Boolean flag = ("content".equals(name) || name.endsWith("WithHtml"));
        if (flag) {
            return super.getParameterValues(name);
        }
        String[] arr = super.getParameterValues(name);
        if (arr != null) {
            for (int i = 0; i < arr.length; i++) {
                try {
                    if (iscleanXss) {
                        arr[i] = JsoupUtil.clean(URLDecoder.decode(arr[i], "UTF-8"));
                    } else {
                        arr[i] = HtmlUtils.htmlEscape(URLDecoder.decode(arr[i], "UTF-8"));
                    }
                } catch (UnsupportedEncodingException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return arr;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> paramMap = super.getParameterMap();
        for (Iterator<Entry<String, String[]>> iterator = paramMap.entrySet().iterator(); iterator.hasNext();) {
            Entry<String, String[]> entry = iterator.next();
            String[] arr = (String[]) entry.getValue();
            for (int i = 0; i < arr.length; i++) {
                if (arr[i] instanceof String) {
                    try {
                        if (iscleanXss) {
                            arr[i] = JsoupUtil.clean(URLDecoder.decode(arr[i], "UTF-8"));
                        } else {
                            arr[i] = HtmlUtils.htmlEscape(URLDecoder.decode(arr[i], "UTF-8"));
                        }
                    } catch (UnsupportedEncodingException e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }
            entry.setValue(arr);
        }
        return paramMap;
    }

    /**
     * 覆盖getHeader方法，将参数名和参数值都做xss过滤。<br/>
     * 如果需要获得原始的值，则通过super.getHeaders(name)来获取<br/>
     * getHeaderNames 也可能需要覆盖
     */
    @Override
    public String getHeader(String name) {
        name = JsoupUtil.clean(name);
        String value = super.getHeader(name);
        if (StringUtils.isNotBlank(value)) {
            try {
                if (iscleanXss) {
                    value = JsoupUtil.clean(URLDecoder.decode(value, "UTF-8"));
                } else {
                    value = HtmlUtils.htmlEscape(URLDecoder.decode(value, "UTF-8"));
                }
            } catch (UnsupportedEncodingException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return value;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (isUpData) {
            return super.getInputStream();
        } else {
            String str = getRequestBody(super.getInputStream());
            @SuppressWarnings("unchecked")
            Map<String, Object> map = JSON.parseObject(str, Map.class);
            Map<String, Object> resultMap = new HashMap<>();
            for (String key : map.keySet()) {
                Object val = map.get(key);
                if (map.get(key) instanceof String) {
                    if (iscleanXss) {
                        resultMap.put(key, JsoupUtil.clean(URLDecoder.decode(val.toString(), "UTF-8")));
                    } else {
                        resultMap.put(key, HtmlUtils.htmlEscape(URLDecoder.decode(val.toString(), "UTF-8")));

                    }
                } else {
                    resultMap.put(key, val);
                }

            }
            str = JSON.toJSONString(resultMap);
            final ByteArrayInputStream bais = new ByteArrayInputStream(str.getBytes());

            return new ServletInputStream() {

                @Override
                public int read() throws IOException {
                    return bais.read();
                }

                @Override
                public boolean isFinished() {
                    return false;
                }

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setReadListener(ReadListener listener) {

                }
            };
        }

    }

    // TODO
    private String getRequestBody(InputStream stream) {
        String line = "";
        StringBuilder body = new StringBuilder();
        // 读取POST提交的数据内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, Charset.forName("UTF-8")));
        try {
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return body.toString();
    }

    /**
     * 获取最原始的request
     * 
     * @return
     */
    public HttpServletRequest getOrgRequest() {
        return orgRequest;
    }

    /**
     * 获取最原始的request的静态方法
     * 
     * @return
     */
    public static HttpServletRequest getOrgRequest(HttpServletRequest req) {
        if (req instanceof XssHttpServletRequestWrapper) {
            return ((XssHttpServletRequestWrapper) req).getOrgRequest();
        }
        return req;
    }

}

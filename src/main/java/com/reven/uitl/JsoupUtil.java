package com.reven.uitl;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * xss非法标签过滤工具类 过滤html中的xss字符
 */
public class JsoupUtil {
    private static Logger logger = LoggerFactory.getLogger(JsoupUtil.class);

    /**
     * 标签白名单 relaxed() 允许的标签: a, b, blockquote, br, caption, cite, code, col,
     * colgroup, dd, dl, dt, em, h1, h2, h3, h4, h5, h6, i, img, li, ol, p, pre, q,
     * small, strike, strong, sub, sup, table, tbody, td, tfoot, th, thead, tr, u,
     * ul。 结果不包含标签rel=nofollow ，如果需要可以手动添加。
     * 
     * 使用自带的basicWithImages 白名单
     * 允许的便签有a,b,blockquote,br,cite,code,dd,dl,dt,em,i,li,ol,p,pre,q,small,span,
     * strike,strong,sub,sup,u,ul,img
     * 以及a标签的href,img标签的src,align,alt,height,width,title属性
     */
    private static final Whitelist WHITELIST = Whitelist.basicWithImages();
    /** 配置过滤化参数,不对代码进行格式化 */
    private static final Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);
    static {
        // 富文本编辑时一些样式是使用style来进行实现的
        // 比如红色字体 style="color:red;"
        // 所以需要给所有标签添加style属性
        /**
         * addTags() 设置白名单标签 addAttributes() 设置标签需要保留的属性 ,[:all]表示所有
         * preserveRelativeLinks() 是否保留元素的URL属性中的相对链接，或将它们转换为绝对链接,默认为false.
         * 为false时将会把baseUri和元素的URL属性拼接起来
         */
        WHITELIST.addAttributes(":all", "style");
        WHITELIST.preserveRelativeLinks(true);
    }

    public static String clean(String content) {
        if (StringUtils.isNotBlank(content)) {
            content = content.trim();
        }
        /**
         * baseUri ,非空 如果baseUri为空字符串或者不符合Http://xx类似的协议开头,属性中的URL链接将会被删除
         * 如果WHITELIST.preserveRelativeLinks(false), 会将baseUri和属性中的URL链接进行拼接
         */
        return Jsoup.clean(content, "", WHITELIST, outputSettings);
    }

    /**
     * 处理Json类型的Html标签,进行xss过滤
     * 
     * @param s
     * @return
     */
    public static String cleanJson(String s) {
        // 先处理双引号的问题
        s = jsonStringConvert(s);
        return clean(s);
    }

    /**
     * 将json字符串本身的双引号以外的双引号变成单引号
     * 
     * @param s
     * @return
     */
    public static String jsonStringConvert(String s) {
//        logger.info("[处理JSON字符串] [将嵌套的双引号转成单引号] [原JSON] :{}", s);
        char[] temp = s.toCharArray();
        int n = temp.length;
        for (int i = 0; i < n; i++) {
            if (temp[i] == ':' && temp[i + 1] == '"') {
                for (int j = i + 2; j < n; j++) {
                    if (temp[j] == '"') {
                        // 如果该字符为双引号,下个字符不是逗号或大括号,替换
                        if (temp[j + 1] != ',' && temp[j + 1] != '}') {
                            // 将json字符串本身的双引号以外的双引号变成单引号
                            temp[j] = '\'';
                        } else if (temp[j + 1] == ',' || temp[j + 1] == '}') {
                            break;
                        }
                    }
                }
            }
        }
        String r = new String(temp);
//        logger.info("[处理JSON字符串] [将嵌套的双引号转成单引号] [处理后的JSON] :{}", r);
        return r;
    }

    public static void main(String[] args) throws IOException {
        String text = "<a href=\"http://www.baidu.com/a\" onclick=\"alert(1);\">sss</a><script>alert(0);</script>sss   ";
        System.out.println((text) + "/end");
        System.out.println(clean(text) + "/end");

        String s = "<p><a href=\"www.test.xhtml\">test</a><a title=\"哈哈哈\" href=\"/aaaa.bbv.com\" href1=\"www.baidu.com\" href2=\"www.baidu.com\" onclick=\"click()\"></a><script>ss</script><img script=\"xxx\" "
                + "onclick=function  src=\"https://www.xxx.png\" title=\"\" width=\"100%\" alt=\"\"/>"
                + "<br/></p><p>电饭锅进口量的说法</p><p>————————</p><p><span style=\"text-decoration: line-through;\">大幅度发</span></p>"
                + "<p><em>sd</em></p><p><em><span style=\"text-decoration: underline;\">dsf</span></em></p><p><em>"
                + "<span style=\"border: 1px solid rgb(0, 0, 0);\">撒地方</span></em></p><p><span style=\"color: rgb(255, 0, 0);\">似懂非懂</span><br/></p>"
                + "<p><span style=\"color: rgb(255, 0, 0);\"><strong>撒地方</strong></span></p><p><span style=\"color: rgb(221, 217, 195);\"><br/></span></p>"
                + "<p style=\"text-align: center;\"><span style=\"color: rgb(0, 0, 0); font-size: 20px;\">撒旦法</span></p><p><br/></p>";
        logger.info(clean(s));

    }
}

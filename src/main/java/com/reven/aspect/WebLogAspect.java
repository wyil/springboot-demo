package com.reven.aspect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

//import com.aspire.sims.rtplt.framework.portal.client.pojo.Staff;
//import com.aspire.sims.rtplt.framework.portal.client.util.LoginHelper;
import com.reven.plugs.ipaddr.IPService;


/**
 * @ClassName:  OperationLogAspect   
 * @Description:web系统操作日志切面
 * @author huangruiwen
 * @date   2019年3月4日
 */
@Aspect
@Component
public class WebLogAspect {
    private static Logger log = LoggerFactory.getLogger(WebLogAspect.class);
    ExpressionParser parser = new SpelExpressionParser();
    LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
    @Autowired
    private IPService ipService;
//    @Autowired
//    protected IBusinessLogService businessLogService;

    @Value("${operation.log.on}")
    public boolean logOn;

    /**
     * 统计方法执行耗时Around环绕通知
     * @param joinPoint
     * @return
     */

    @Around("@annotation(net.diaowen.log.WebLogAnnotation)")
    public Object timeAround(ProceedingJoinPoint joinPoint) {
        // 定义返回对象、得到方法需要的参数
        Object obj = null;
        Object[] args = joinPoint.getArgs();
        long startTime = System.currentTimeMillis();
        String logId = UUID.randomUUID().toString();
        log.info("log={},开始执行时间：{}", logId, startTime);

        String operationResult = "成功";
        try {
            obj = joinPoint.proceed(args);
        } catch (RuntimeException e) {
            operationResult = "失败";
            log.error("统计某方法执行耗时环绕通知出错", e);
        } catch (Throwable e) {
            operationResult = "失败";
        }

        // 获取执行的方法名
        long endTime = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.toShortString();
        // 打印耗时的信息
        long diffTime = endTime - startTime;
        log.info("log={},执行耗时：{} ms，执行方法名：{}", logId, diffTime, methodName);
        try {
            if (logOn) {
                businessLog(joinPoint, operationResult);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return obj;
    }

    @Async
    private void businessLog(ProceedingJoinPoint joinPoint, String operationResult)
            throws NoSuchFieldException, IllegalAccessException {
        MethodInvocationProceedingJoinPoint methodPoint = (MethodInvocationProceedingJoinPoint) joinPoint;
        Field proxy = methodPoint.getClass().getDeclaredField("methodInvocation");
        proxy.setAccessible(true);
        ReflectiveMethodInvocation j = (ReflectiveMethodInvocation) proxy.get(methodPoint);
        Method method = j.getMethod();
        WebLogAnnotation webLogAnno = method.getAnnotation(WebLogAnnotation.class);

        Object[] args = joinPoint.getArgs();
        String[] params = discoverer.getParameterNames(method);
        EvaluationContext context = new StandardEvaluationContext();
        for (int len = 0; len < params.length; len++) {
            context.setVariable(params[len], args[len]);
        }
        String dataId = null;

        if (StringUtils.isNotEmpty(webLogAnno.spelDataId())) {
            Expression expression = parser.parseExpression(webLogAnno.spelDataId());
            Object valueObj = expression.getValue(context);
            if (valueObj != null) {
                dataId = valueObj.toString();
            }
        }
        log.info("logType:{}", webLogAnno.webLogTypeEnum().getLogType());
//        log.info("logTypeName:{}", webLogAnno.webLogTypeEnum().getLogTypeName());
        log.info("action:{}", webLogAnno.webLogTypeEnum().getAction());
        log.info("spelDataId:{}={}", webLogAnno.spelDataId(), dataId);

        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String ip = ipService.getIp(request);
        // 记录下请求内容
        log.info("URL : {}", request.getRequestURL());
        log.info("HTTP_METHOD : {}", request.getMethod());
        log.info("IP : {}", ip);
        log.info("CurCity : {}", ipService.getCurCity(request));
        StringBuilder paramjson = new StringBuilder();
        StringBuilder paramAll = new StringBuilder();
        paramjson.append("{");

        Enumeration<String> enu = request.getParameterNames();
        while (enu.hasMoreElements()) {
            String name = enu.nextElement();
            if (paramAll.length() > 2) {
                paramAll.append(",");
            }
            paramAll.append("\"" + name + "\":\"" + request.getParameter(name) + "\"");
            if (paramjson.length() < 1500 && !("ticket".equals(name) || "roleType".equals(name) || "domain".equals(name)
                    || "a".equals(name))) {
                if (paramjson.length() > 2) {
                    paramjson.append(",");
                }
                paramjson.append("\"" + name + "\":\"" + request.getParameter(name) + "\"");
            }

        }
        paramjson.append("}");
        log.info("paramAll : {}", paramAll);
        log.info("paramjson : {}", paramjson);

        insertBusinessLog(operationResult, webLogAnno, dataId, request, ip, paramjson);
    }

    private void insertBusinessLog(String operationResult, WebLogAnnotation webLogAnno, String dataId,
            HttpServletRequest request, String ip, StringBuilder paramjson) {
        String logType = webLogAnno.webLogTypeEnum().getLogType();
        // 被操作实体类型
        // 参考cmop都写1
        Integer businessType = 1;
        // 被操作实体ID
        String businessId = dataId;
        if (StringUtils.isEmpty(businessId)) {
            businessId = webLogAnno.webLogTypeEnum().getAction();
        }
        // 被操作实体名称
//        String businessName = webLogAnno.webLogTypeEnum().getLogTypeName();
        Map<String, String> logInfoMap = new HashMap<>(6);
        logInfoMap.put("operation", webLogAnno.webLogTypeEnum().getAction());
        logInfoMap.put("comment", paramjson.toString());
//        Staff staff = LoginHelper.getLoginStaff(request.getSession());
//        if (staff != null) {
//            logInfoMap.put("operatorId", staff.getLoginName());
//            logInfoMap.put("operatorName", staff.getRealName());
//        } else {
//            String loginMobile = (String) request.getSession().getAttribute(SessionConstant.LOGIN_MOBILE);
//            logInfoMap.put("operatorId", loginMobile);
//            logInfoMap.put("operatorName", loginMobile);
//        }
//        logInfoMap.put("clientIp", ip);
//        logInfoMap.put("result", operationResult);
//        businessLogService.info(logType, businessType, businessId, businessName, operationResult, logInfoMap);
    }
}

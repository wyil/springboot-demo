//package com.reven.aspect;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.AfterThrowing;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.serializer.SerializerFeature;
//import com.reven.model.entity.WebLogEntity;
//import com.reven.uitl.WebUtil;
//
///**
// * 
// * @author Administrator
// *
// */
//@Aspect
//@Component
//public class WebLogAspect {
//    private final static Logger logger = LoggerFactory.getLogger(WebLogAspect.class);
//    // https://www.cnblogs.com/dolphin0520/p/3920407.html
//    /**
//     * @Fields webLogEntityThreadLocal : 本地线程webLogEntity副本
//     */
//    ThreadLocal<WebLogEntity> webLogEntityThreadLocal = new ThreadLocal<WebLogEntity>();
//
//    /**
//     * *定义一个切入点.
//     */
//    @Pointcut("execution(public * com.reven.controller.*.*(..)) && !execution( * com.reven.controller.*.initBinder(..))")
//    public void webLog() {
//    }
//
//    /**
//     * *前置通知，方法调用前被调用
//     * 
//     * @param joinPoint
//     */
//    @Before("webLog()")
//    public void doBefore(JoinPoint joinPoint) {
//
//        // 接收到请求，记录请求内容
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = attributes.getRequest();
//        // 创建日志实体
//        WebLogEntity logger = new WebLogEntity();
//        // 请求开始时间
//        logger.setStartTime(System.currentTimeMillis());
//        // 获取请求sessionId
//        String sessionId = request.getRequestedSessionId();
//        // 请求路径
//        String url = request.getRequestURI();
//        // 获取请求参数信息
//        String paramData = JSON.toJSONString(request.getParameterMap(),
//                SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteMapNullValue);
//        // 设置客户端ip
//        logger.setClientIp(WebUtil.getCliectIp(request));
//
//        logger.setServerIp(WebUtil.getCliectIp(request));
//        // 设置请求方法
//        logger.setMethod(request.getMethod());
//        // 设置请求类型（json|普通请求）
//        logger.setType(WebUtil.getRequestType(request));
//        // 设置请求地址
//        logger.setUri(url);
//        // 设置sessionId
//        logger.setSessionId(sessionId);
//        // 请求的类及名称
//        logger.setClassMethod(
//                joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
//        // 设置请求参数内容json字符串
//        logger.setParamData(paramData);
//        webLogEntityThreadLocal.set(logger);
//
//    }
//
//    /**
//     * *后置返回通知 这里需要注意的是: 如果参数中的第一个参数为JoinPoint，则第二个参数为返回值的信息
//     * *如果参数中的第一个参数不为JoinPoint，则第一个参数为returning中对应的参数 returning
//     * *限定了只有目标方法返回值与通知方法相应参数类型时才能执行后置返回通知，否则不执行，对于returning对应的通知方法参数为Object类型将匹配任何目标返回值
//     * 
//     * @param joinPoint
//     * @param returnData
//     */
//    @AfterReturning(value = "webLog()", returning = "returnData")
//    public void doAfterReturning(JoinPoint joinPoint, Object returnData) {
//        try {
//            // 处理完请求，返回内容
//            logger.debug("WebLogAspect.doAfterReturning()");
//            logger.debug("RETURN DATA:{}", returnData);
//
//            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
//                    .getRequestAttributes();
//            HttpServletResponse response = attributes.getResponse();
//            // 获取请求错误码
//            int status = response.getStatus();
//            // 获取本次请求日志实体
//            WebLogEntity webLogEntity = webLogEntityThreadLocal.get();
//            // 请求结束时间
//            webLogEntity.setEndTime(System.currentTimeMillis());
//            // 设置请求时间差
//            webLogEntity
//                    .setTimeConsuming(Integer.valueOf((webLogEntity.getEndTime() - webLogEntity.getStartTime()) + ""));
//            // 设置返回时间
//            webLogEntity.setReturnTime(webLogEntity.getEndTime() + "");
//            // 设置返回错误码
//            webLogEntity.setHttpStatusCode(status + "");
//            // 设置返回值
//            webLogEntity.setReturnData(JSON.toJSONString(returnData, SerializerFeature.DisableCircularReferenceDetect,
//                    SerializerFeature.WriteMapNullValue));
//            // 执行将日志写入数据库
//            logger.info(JSON.toJSONString(webLogEntity));
//        } finally {
//            // 必须回收自定义的ThreadLocal变量，尤其在线程池场景下，线程经常会被复用，如果不清理自定义的
//            // ThreadLocal变量，可能会影响后续业务逻辑和造成内存泄露等问题。尽量在代理中使用try-finally块进行回收。
//            webLogEntityThreadLocal.remove();
//        }
//    }
//
//    /**
//     * *后置异常通知 定义一个名字，该名字用于匹配通知实现方法的一个参数名，当目标方法抛出异常返回后，将把目标方法抛出的异常传给通知方法； throwing
//     * *限定了只有目标方法抛出的异常与通知方法相应参数异常类型时才能执行后置异常通知，否则不执行，
//     * *对于throwing对应的通知方法参数为Throwable类型将匹配任何异常。
//     * 
//     * @param joinPoint
//     * @param exception
//     */
//    @AfterThrowing(value = "webLog()", throwing = "exception")
//    public void doAfterThrowingAdvice(JoinPoint joinPoint, Throwable exception) {
//        try {
//
//            // 目标方法名：
//            WebLogEntity webLogEntity = webLogEntityThreadLocal.get();
//            // 请求结束时间
//            webLogEntity.setEndTime(System.currentTimeMillis());
//            // 设置请求时间差
//            webLogEntity
//                    .setTimeConsuming(Integer.valueOf((webLogEntity.getEndTime() - webLogEntity.getStartTime()) + ""));
//            // 设置返回时间
//            webLogEntity.setReturnTime(webLogEntity.getEndTime() + "");
//            // 设置返回错误码
//            webLogEntity.setHttpStatusCode("400");
//            webLogEntity.setExceptionMessage(exception.getMessage());
//            logger.info(JSON.toJSONString(webLogEntity));
//        } finally {
//            // 必须回收自定义的ThreadLocal变量，尤其在线程池场景下，线程经常会被复用，如果不清理自定义的
//            // ThreadLocal变量，可能会影响后续业务逻辑和造成内存泄露等问题。尽量在代理中使用try-finally块进行回收。
//            webLogEntityThreadLocal.remove();
//        }
//    }
//
//    /**
//     * *后置最终通知（目标方法只要执行完了就会执行后置通知方法）
//     * 
//     * @param joinPoint
//     */
////     @After(value = "webLog()")  
////     public void doAfterAdvice(JoinPoint joinPoint){  
////         System.out.println("后置通知执行了!!!!");  
////     }  
//
//    /**
//     * *环绕通知： 环绕通知非常强大，可以决定目标方法是否执行，什么时候执行，执行时是否需要替换方法参数，执行完毕是否需要替换返回值。
//     * *环绕通知第一个参数必须是org.aspectj.lang.ProceedingJoinPoint类型
//     */
////     @Around(value = "webLog()")  
////     public Object doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint){  
////         System.out.println("环绕通知的目标方法名："+proceedingJoinPoint.getSignature().getName());  
////         try {//obj之前可以写目标方法执行前的逻辑  
////             Object obj = proceedingJoinPoint.proceed();//调用执行目标方法  
////             return obj;  
////         } catch (Throwable throwable) {  
////             throwable.printStackTrace();  
////         }  
////         return null;  
////     }  
//
//}
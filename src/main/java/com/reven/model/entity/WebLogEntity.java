package com.reven.model.entity;

import java.sql.Timestamp;

/**
 * @ClassName: WebLoggerEntity
 * @author reven
 * @date 2018年9月7日
 */
public class WebLogEntity {

    /**
     * @Fields id : 编号
     */
    private Long id;
    /**
     * @Fields clientIp :客户端请求ip
     */
    private String clientIp;

    /**
     * @Fields serverIp : TODO(用一句话描述这个变量表示什么)
     */
    private String serverIp;

    /**
     * @Fields uri : 客户端请求路径
     */
    private String uri;
    /**
     * @Fields type : 终端请求方式,普通请求,ajax请求
     */
    private String type;
    /**
     * @Fields method : 请求方式method,post,get等
     */
    private String method;
    /**
     * @Fields classMethod :请求的处理类及方法
     */
    private String classMethod;
    /**
     * @Fields paramData : 请求参数内容,json
     */
    private String paramData;

    private String sessionId;

    private Timestamp time;
    /**
     * @Fields returnTime : 接口返回时间
     */
    private String returnTime;
    /**
     * @Fields returnData : 接口返回数据json
     */
    private String returnData;
    /**
     * @Fields httpStatusCode : 请求时httpStatusCode代码，如：200,400,404等
     */
    private String httpStatusCode;
    /**
     * @Fields timeConsuming : 请求耗时毫秒
     */
    private int timeConsuming;
    /**
     * @Fields exceptionMessage : 异常描述
     */
    private String exceptionMessage;

    private long startTime;

    private long endTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getClassMethod() {
        return classMethod;
    }

    public void setClassMethod(String classMethod) {
        this.classMethod = classMethod;
    }

    public String getParamData() {
        return paramData;
    }

    public void setParamData(String paramData) {
        this.paramData = paramData;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime;
    }

    public String getReturnData() {
        return returnData;
    }

    public void setReturnData(String returnData) {
        this.returnData = returnData;
    }

    public String getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(String httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public int getTimeConsuming() {
        return timeConsuming;
    }

    public void setTimeConsuming(int timeConsuming) {
        this.timeConsuming = timeConsuming;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("WebLoggerEntity [id=");
        builder.append(id);
        builder.append(", clientIp=");
        builder.append(clientIp);
        builder.append(", serverIp=");
        builder.append(serverIp);
        builder.append(", uri=");
        builder.append(uri);
        builder.append(", type=");
        builder.append(type);
        builder.append(", method=");
        builder.append(method);
        builder.append(", classMethod=");
        builder.append(classMethod);
        builder.append(", paramData=");
        builder.append(paramData);
        builder.append(", sessionId=");
        builder.append(sessionId);
        builder.append(", time=");
        builder.append(time);
        builder.append(", returnTime=");
        builder.append(returnTime);
        builder.append(", returnData=");
        builder.append(returnData);
        builder.append(", httpStatusCode=");
        builder.append(httpStatusCode);
        builder.append(", timeConsuming=");
        builder.append(timeConsuming);
        builder.append(", exceptionMessage=");
        builder.append(exceptionMessage);
        builder.append(", startTime=");
        builder.append(startTime);
        builder.append(", endTime=");
        builder.append(endTime);
        builder.append("]");
        return builder.toString();
    }

}
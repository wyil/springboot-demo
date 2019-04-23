package com.reven.controller.common;


/**
 * @ClassName:  ResResultCode   
 * @Description: 响应码枚举，参考HTTP状态码的语义
 * @author reven
 * @date   2018年8月28日
 */
public enum ResResultCode {
    /**
     * 成功
     */
    SUCCESS(200),
    /**
     * 失败
     */
    FAIL(400),
    /**
     * 接口不存在
     */
    NOT_FOUND(404),
    /**
     * 服务器内部错误
     */
    INTERNAL_SERVER_ERROR(500);

    public int code;

    ResResultCode(int code) {
        this.code = code;
    }
}

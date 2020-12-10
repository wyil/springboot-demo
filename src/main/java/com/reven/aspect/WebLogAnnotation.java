package com.reven.aspect;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @ClassName:  WebOperationLog   
 * @Description:操作日志注解
 * @author reven
 * @date   2019年3月8日
 */
@Target(ElementType.METHOD) // 用于描述方法
@Retention(RUNTIME)
@Documented
public @interface WebLogAnnotation {
    /**   
     * @Title: spelDataId   
     * @Description: 操作业务对象的id，支持Spel表达式 
     * @return      
     */
    String spelDataId() default "";

    /**   
     * @Title: logType   
     * @Description: 日志类型枚举类
     * @return      
     */
    WebLogTypeEnum webLogTypeEnum();

    /**   
     * @Title: action  
     * @Description: 操作的动作（查询、新增、修改、删除）
     * @return      
     */
    String action() default "";
}

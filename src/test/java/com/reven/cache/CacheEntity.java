package com.reven.cache;

import java.io.Serializable;

public class CacheEntity implements Serializable {

    private static final long serialVersionUID = -5431498217838646933L;

    private Object value; // 值

    private long gmtModify; // 保存的时间戳

    private int expire; // 有效时间

    public CacheEntity(Object value, long gmtModify, int expire) {
        super();
        this.value = value;
        this.gmtModify = gmtModify;
        this.expire = expire;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public long getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(long gmtModify) {
        this.gmtModify = gmtModify;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }
}
package com.reven.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author reven
 */
@Table(name = "t_demo")
public class Demo extends BaseEntity implements Serializable {
    /**
     * @Fields id 自增主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * @Fields name  名称
     */
    private String name;

    /**
     * @Fields date 数据日期
     */
    private Date date;

    /**
     * @Fields timestamp 更新记录时刷新当前时间戳记时
     */
    private Date timestamp;

    /**
     * @Fields key 测试关键字
     */
    @Column(name = "`KEY`")
    private String key;

    @Column(name = "`ac dd`")
    private String acDd;

    private static final long serialVersionUID = 1L;
    
    
    public Demo() {
        super();
    }

    public Demo(Integer id, String name, Date date, Date timestamp, String key, String acDd) {
        super();
        this.id = id;
        this.name = name;
        this.date = date;
        this.timestamp = timestamp;
        this.key = key;
        this.acDd = acDd;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key == null ? null : key.trim();
    }

    public String getAcDd() {
        return acDd;
    }

    public void setAcDd(String acDd) {
        this.acDd = acDd == null ? null : acDd.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", date=").append(date);
        sb.append(", timestamp=").append(timestamp);
        sb.append(", key=").append(key);
        sb.append(", acDd=").append(acDd);
        sb.append("]");
        return sb.toString();
    }
}
package com.reven.model.entity;

import java.util.Date;

/**
 * @author reven
 */
public class DemoExcel {
    private Integer id;

    private String name;

    private Date date;

    private String enName;

    private Boolean flag;

    private Float numFloat;

    private Double numDouble;

    private Long numLong;

    public DemoExcel() {
        super();
    }

    public DemoExcel(Integer id, String name, Date date, String enName, Boolean flag, Float numFloat, Double numDouble,
            Long numLong) {
        super();
        this.id = id;
        this.name = name;
        this.date = date;
        this.enName = enName;
        this.flag = flag;
        this.numFloat = numFloat;
        this.numDouble = numDouble;
        this.numLong = numLong;
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
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public Float getNumFloat() {
        return numFloat;
    }

    public void setNumFloat(Float numFloat) {
        this.numFloat = numFloat;
    }

    public Double getNumDouble() {
        return numDouble;
    }

    public void setNumDouble(Double numDouble) {
        this.numDouble = numDouble;
    }

    public Long getNumLong() {
        return numLong;
    }

    public void setNumLong(Long numLong) {
        this.numLong = numLong;
    }

    @Override
    public String toString() {
        return "DemoExcel [id=" + id + ", name=" + name + ", date=" + date + ", enName=" + enName + ", flag=" + flag
                + ", numFloat=" + numFloat + ", numDouble=" + numDouble + ", numLong=" + numLong + "]";
    }
}
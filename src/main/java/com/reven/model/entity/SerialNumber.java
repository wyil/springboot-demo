package com.reven.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "serial_number")
public class SerialNumber extends BaseEntity implements Serializable {
    @Id
    @Column(name = "auto_id")
    private Integer autoId;

    /**
     * @Fields serialRuleId 流水号规则id
     */
    @Column(name = "serial_rule_id")
    private Integer serialRuleId;

    /**
     * @Fields serialYear 当前年份
     */
    @Column(name = "serial_year")
    private Integer serialYear;

    /**
     * @Fields serialMonth 当前月份
     */
    @Column(name = "serial_month")
    private Integer serialMonth;

    /**
     * @Fields serialDay 当前日期
     */
    @Column(name = "serial_day")
    private Integer serialDay;

    /**
     * @Fields currentSerial 当前序列号
     */
    @Column(name = "current_serial")
    private Integer currentSerial;

    /**
     * @Fields updateTime 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Integer getAutoId() {
        return autoId;
    }

    public void setAutoId(Integer autoId) {
        this.autoId = autoId;
    }

    public Integer getSerialRuleId() {
        return serialRuleId;
    }

    public void setSerialRuleId(Integer serialRuleId) {
        this.serialRuleId = serialRuleId;
    }

    public Integer getSerialYear() {
        return serialYear;
    }

    public void setSerialYear(Integer serialYear) {
        this.serialYear = serialYear;
    }

    public Integer getSerialMonth() {
        return serialMonth;
    }

    public void setSerialMonth(Integer serialMonth) {
        this.serialMonth = serialMonth;
    }

    public Integer getSerialDay() {
        return serialDay;
    }

    public void setSerialDay(Integer serialDay) {
        this.serialDay = serialDay;
    }

    public Integer getCurrentSerial() {
        return currentSerial;
    }

    public void setCurrentSerial(Integer currentSerial) {
        this.currentSerial = currentSerial;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", autoId=").append(autoId);
        sb.append(", serialRuleId=").append(serialRuleId);
        sb.append(", serialYear=").append(serialYear);
        sb.append(", serialMonth=").append(serialMonth);
        sb.append(", serialDay=").append(serialDay);
        sb.append(", currentSerial=").append(currentSerial);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }
}
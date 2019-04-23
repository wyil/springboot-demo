package com.reven.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "serial_rule")
public class SerialRule extends BaseEntity implements Serializable {
    @Id
    @Column(name = "serial_rule_id")
    private Integer serialRuleId;

    /**
     * @Fields moduleCode 模块编码
     */
    @Column(name = "module_code")
    private String moduleCode;

    /**
     * @Fields moduleName 模块名称
     */
    @Column(name = "module_name")
    private String moduleName;

    /**
     * @Fields withYear 是否包含年
     */
    @Column(name = "with_year")
    private Boolean withYear;

    /**
     * @Fields yearLength 年份的长度，4：yyyy，2：yy
     */
    @Column(name = "year_length")
    private Integer yearLength;

    /**
     * @Fields withMonth 是否包含月，mm
     */
    @Column(name = "with_month")
    private Boolean withMonth;

    /**
     * @Fields withDay 是否包含天，dd
     */
    @Column(name = "with_day")
    private Boolean withDay;

    /**
     * @Fields prefix 流水号前缀
     */
    @Column(name = "prefix")
    private String prefix;

    /**
     * @Fields serialLength 流水号序号长度，例如4,则为0001
     */
    @Column(name = "serial_length")
    private Integer serialLength;

    private static final long serialVersionUID = 1L;

    public Integer getSerialRuleId() {
        return serialRuleId;
    }

    public void setSerialRuleId(Integer serialRuleId) {
        this.serialRuleId = serialRuleId;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode == null ? null : moduleCode.trim();
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName == null ? null : moduleName.trim();
    }

    public Boolean getWithYear() {
        return withYear;
    }

    public void setWithYear(Boolean withYear) {
        this.withYear = withYear;
    }

    public Integer getYearLength() {
        return yearLength;
    }

    public void setYearLength(Integer yearLength) {
        this.yearLength = yearLength;
    }

    public Boolean getWithMonth() {
        return withMonth;
    }

    public void setWithMonth(Boolean withMonth) {
        this.withMonth = withMonth;
    }

    public Boolean getWithDay() {
        return withDay;
    }

    public void setWithDay(Boolean withDay) {
        this.withDay = withDay;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix == null ? null : prefix.trim();
    }

    public Integer getSerialLength() {
        return serialLength;
    }

    public void setSerialLength(Integer serialLength) {
        this.serialLength = serialLength;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", serialRuleId=").append(serialRuleId);
        sb.append(", moduleCode=").append(moduleCode);
        sb.append(", moduleName=").append(moduleName);
        sb.append(", withYear=").append(withYear);
        sb.append(", yearLength=").append(yearLength);
        sb.append(", withMonth=").append(withMonth);
        sb.append(", withDay=").append(withDay);
        sb.append(", prefix=").append(prefix);
        sb.append(", serialLength=").append(serialLength);
        sb.append("]");
        return sb.toString();
    }
}
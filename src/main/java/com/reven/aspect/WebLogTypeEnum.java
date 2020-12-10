package com.reven.aspect;

/**
 * @ClassName:  WebLogTypeEnum   
 * @Description: web请求日志类型
 * @author reven
 * @date   2019年3月8日
 */
public enum WebLogTypeEnum {
    
    // 定制上报：查询|新增|删除|审核|打回|结单|催办|模板查询|模板新增|模板修改|模板删除|模板无效|模板复制|模板查询
    custom_report_query("200001001", "查询工单","查询"),
    custom_report_add("200001002", "新增工单","新增"),
    custom_report_update("200001003", "修改工单","修改"),
    custom_report_delete("200001004", "删除工单","删除"),
    custom_report_repulse("200001005", "打回工单","打回"),
    custom_report_audit("200001006", "审批工单","审批"),
    custom_report_batchUrge("200001007", "批量催办工单","催办"),
    custom_report_finish("200001008", "结单","结单"),
    
    custom_report_template_query("200001009", "模板查询","模板查询"),
    custom_report_template_add("200001010", "模板新增","模板新增"),
    custom_report_template_update("200001011", "模板修改","模板修改"),
    custom_report_template_delete("200001012", "模板删除","模板删除"),
    custom_report_template_useless("200001013", "模板无效","模板无效"),
    custom_report_template_copy("200001014", "模板复制","模板复制"),
    
    ;
    
    private String logType;
    private String businessName;
    private String action;


    private WebLogTypeEnum(String logType, String businessName, String action) {
        this.logType = logType;
        this.businessName = businessName;
        this.action = action;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
    

}

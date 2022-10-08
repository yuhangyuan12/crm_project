package com.hhy.crm.query;

import com.hhy.crm.base.BaseQuery;


//营销机会查询类
public class SaleChanceQuery extends BaseQuery {
    //营销机会
    //客户名
    private String customerName;
    //创建人
    private String createMan;
    //分配状态
    private Integer state;

    //客户开发计划查询
    private String devResult;
    private Integer assignMan;


    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCreateMan() {
        return createMan;
    }

    public void setCreateMan(String createMan) {
        this.createMan = createMan;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getDevResult() {
        return devResult;
    }

    public void setDevResult(String devResult) {
        this.devResult = devResult;
    }

    public Integer getAssignMan() {
        return assignMan;
    }

    public void setAssignMan(Integer assignMan) {
        this.assignMan = assignMan;
    }
}

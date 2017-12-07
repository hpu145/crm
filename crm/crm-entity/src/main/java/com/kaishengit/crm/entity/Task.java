package com.kaishengit.crm.entity;

import java.io.Serializable;

import java.util.Date;

/**
 * @author 
 */
public class Task implements Serializable {
    private Integer id;

    private String title;

    private Date finishTime;

    private Date remindTime;

    /**
     * 0表示未完成；1表示完成
     */
    private Byte done;

    private Integer employeeId;

    private Integer custId;

    private Integer saleId;

    private Date createTime;

    private Customer customer;
    private SaleChance saleChance;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public SaleChance getSaleChance() {
        return saleChance;
    }

    public void setSaleChance(SaleChance saleChance) {
        this.saleChance = saleChance;
    }

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public Date getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(Date remindTime) {
        this.remindTime = remindTime;
    }

    public Byte getDone() {
        return done;
    }

    public void setDone(Byte done) {
        this.done = done;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public Integer getSaleId() {
        return saleId;
    }

    public void setSaleId(Integer saleId) {
        this.saleId = saleId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 判断待办任务是否逾期
     * @return true表示逾期  false表示正常
     */
    public boolean isOverTime() {
        //这种转换jsp会出错
        /*//将Date类型转换成string类型
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM--dd");
        String finishtime = simpleDateFormat.format(getFinishTime());
        //将string类型转换成joda中的DateTime类型
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        DateTime finishTime = formatter.parseDateTime(finishtime);*/

        return finishTime.before(new Date());
    }


}
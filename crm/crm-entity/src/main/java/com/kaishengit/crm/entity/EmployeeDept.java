package com.kaishengit.crm.entity;

import java.io.Serializable;

/**
 * @author 
 */
public class EmployeeDept implements Serializable {
    private Integer employeeId;

    private Integer deptId;

    private static final long serialVersionUID = 1L;

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }
}
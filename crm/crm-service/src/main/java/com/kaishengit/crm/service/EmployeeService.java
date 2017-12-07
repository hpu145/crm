package com.kaishengit.crm.service;

import com.github.pagehelper.PageInfo;
import com.kaishengit.crm.entity.Dept;
import com.kaishengit.crm.entity.Employee;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangyu on 2017/11/7.
 */
public interface EmployeeService {

    Employee login(String mobile,String password);

    void addNewDept(String deptName);

    List<Dept> findAllDept();

    List<Employee> pageForEmployee(Map<String, Object> queryParam);

    Long employeeCountByDeptId(Integer deptId);

    void addNewEmployee(String employeeName, String mobile, String password, Integer[] deptIds);

    void deleteEmployeeById(Integer id);

    List<Employee> findAllEmployee();


    Employee findByMobile(String userName);

    /**
     * 根据employeeId获取所在的部门列表
     * @param id
     * @return
     */
    List<Dept> findDeptListByEmployeeId(Integer id);

    /**
     * 更改员工账户密码
     * @param employeeId
     * @param password
     */
    void updateEmployeePassword(Integer employeeId, String password);
}

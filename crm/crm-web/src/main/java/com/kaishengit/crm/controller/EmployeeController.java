package com.kaishengit.crm.controller;

import com.github.pagehelper.PageInfo;
import com.kaishengit.crm.entity.Dept;
import com.kaishengit.crm.entity.Employee;
import com.kaishengit.crm.exception.ServiceException;
import com.kaishengit.crm.service.EmployeeService;
import com.kaishengit.web.result.AjaxResult;
import com.kaishengit.web.result.DataTablesResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 账号管理的控制器类
 * Created by zhangyu on 2017/11/8.
 */
@Controller
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;


    @GetMapping
    public String list() {
        return "employee/list";
    }

    /**
     * 添加新部门
     * @param deptName
     * @return
     */
    @PostMapping("/dept/new")
    @ResponseBody
    public AjaxResult addNewDept(String deptName) {
        try{
            employeeService.addNewDept(deptName);
            return AjaxResult.success();
        } catch (ServiceException ex) {
            //ex.printStackTrace();
            return AjaxResult.error(ex.getMessage());
        }
    }

    /**
     * 获取部门的json数据
     * @return
     */
    @GetMapping("/dept.json")
    @ResponseBody
    public List<Dept> findAllDept() {
        return  employeeService.findAllDept();
    }

    /**
     * 根据用户名关键字进行模糊查询实现搜索功能
     * @param draw
     * @param start
     * @param length
     * @param request
     * @return
     */
    @GetMapping("/load.json")
    @ResponseBody
    public DataTablesResult<Employee> loadEmployeeList(
            Integer draw, Integer start, Integer length,Integer deptId,HttpServletRequest request) {
        String keyword = request.getParameter("search[value]");
        Map<String,Object> map = new HashMap<>();
        map.put("start",start);
        map.put("length",length);
        map.put("deptId",deptId);
        map.put("employeeName",keyword);

        List<Employee> employeeList = employeeService.pageForEmployee(map);
        Long total = employeeService.employeeCountByDeptId(deptId);
        return new DataTablesResult<Employee>(draw,total.intValue(),employeeList);
    }

    /**
     * 添加员工
     * @param employeeName
     * @param mobile
     * @param password
     * @param deptId
     * @return
     */
    @PostMapping("/new")
    @ResponseBody
    public AjaxResult addNewEmployee(String employeeName,String mobile,
                                 String password,Integer[] deptId) {
        try {
            employeeService.addNewEmployee(employeeName,mobile,password,deptId);
            return AjaxResult.success();
        } catch (ServiceException ex) {
            return AjaxResult.error(ex.getMessage());
        }

    }

    @GetMapping("/delete/{id:\\d+}")
    @ResponseBody
    public AjaxResult deleteEmployee(@PathVariable Integer id) {
        try {
            employeeService.deleteEmployeeById(id);
            return AjaxResult.success();
        } catch (ServiceException ex) {
            return AjaxResult.error(ex.getMessage());
        }

    }




}

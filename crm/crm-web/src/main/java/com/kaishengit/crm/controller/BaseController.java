package com.kaishengit.crm.controller;

import com.kaishengit.crm.auth.ShiroUtil;
import com.kaishengit.crm.entity.Employee;

import javax.servlet.http.HttpSession;

/**
 * 所有控制器的父类，里面提供所有控制器使用
 * 的公共方法
 * Created by zhangyu on 2017/11/10.
 */
public abstract class BaseController {

    /**
     * 获取当前登录系统的登录账号
     * @param session
     * @return
     */
    public Employee getCurrentEmployee(HttpSession session) {

        return ShiroUtil.getCurrentEmployee();
        //return (Employee) session.getAttribute("current_employee");
    }



}

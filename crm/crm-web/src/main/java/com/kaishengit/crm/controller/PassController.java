package com.kaishengit.crm.controller;

import com.kaishengit.crm.entity.Employee;
import com.kaishengit.crm.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 忘记密码
 * Created by zhangyu on 2017/11/30.
 */
@Controller
@RequestMapping("/getpass")
public class PassController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public String getPassWord() {
        return "pass/pass";
    }
    @PostMapping
    public String getPassWord(String mobile, RedirectAttributes redirectAttributes) {
        Employee employee = employeeService.findByMobile(mobile);
        if (employee == null) {
            redirectAttributes.addFlashAttribute("message","对不起，您输入的账户不存在");
            return "redirect:/getpass";
        }
        redirectAttributes.addFlashAttribute("employeeId",employee.getId());
        return "redirect:/getpass/update";
    }

    @GetMapping("/update")
    public String update() {
        return "pass/update";
    }
    @PostMapping("/update")
    public String update(Integer employeeId,String password,
                         RedirectAttributes redirectAttributes) {
        employeeService.updateEmployeePassword(employeeId,password);
        redirectAttributes.addFlashAttribute("message","密码修改成功");
        return "redirect:/";
    }




}

package com.kaishengit.crm.controller;


import com.kaishengit.crm.auth.ShiroUtil;
import com.kaishengit.crm.service.EmployeeService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 登录与退出的控制器类
 * Created by zhangyu on 2017/11/7.
 */
@Controller
public class HomeController {

    @Autowired
    private EmployeeService employeeService;


    @GetMapping("/")
    public String login() {
        Subject subject = ShiroUtil.getSubject();
        System.out.println("isAuthenticated ? " + subject.isAuthenticated());
        System.out.println("isRemembered? " + subject.isRemembered());
        if (subject.isAuthenticated()) {
            //认为用户要切换账户
            subject.logout();
        }
        if (!subject.isAuthenticated() && subject.isRemembered()) {
            //被记住的用户直接去登录页面
            return "redirect:/home";
        }
        return "login";
    }

    /**
     * 表单登录方法
     * @param mobile
     * @param password
     * @return
     */
    @PostMapping("/")
    public String login(String mobile, String password, boolean rememberMe,
                        RedirectAttributes redirectAttributes, HttpServletRequest request) {
        try{
           /* Employee employee = employeeService.login(mobile,password);
            session.setAttribute("current_employee",employee);*/

            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken usernamePasswordToken =
                    new UsernamePasswordToken(mobile,new Md5Hash(password).toString(),rememberMe);

            subject.login(usernamePasswordToken);

            //可以在jsp页面利用shiro的标签库获取employeeId
            //从ShiroRealm获取当前登录的对象
            //Employee employee = (Employee) subject.getPrincipal();
            //将登录成功的对象放入session
            //Session session = subject.getSession();
            //session.setAttribute("current_employee",employee);

            //跳转到登录前访问的URL
            String url = "/home";
            SavedRequest savedRequest = WebUtils.getSavedRequest(request);
            if (savedRequest != null) {
                //获得登录前访问的URL
                url = savedRequest.getRequestUrl();
            }
            return "redirect:" + url;
        } catch (AuthenticationException ex) {
            redirectAttributes.addFlashAttribute("message","账号或密码输入错误");
            return "redirect:/";
        }
    }

    /**
     * 登录成功后进入首页
     * @return
     */
    @GetMapping("/home")
    public String home() {
        return "home";
    }

    /**
     * 实现安全退出功能
     * @param session
     * @param redirectAttributes
     * @return
     */
    @GetMapping("/logout")
    public String logout(HttpSession session,RedirectAttributes redirectAttributes) {
//        session.invalidate();

        SecurityUtils.getSubject().logout();
        redirectAttributes.addFlashAttribute("message","您已成功退出系统");
        return "redirect:/";
    }




}

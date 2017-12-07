package com.kaishengit.crm.controller.interceptor;

import com.kaishengit.crm.entity.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

/**
 * 用于判断是否登录的拦截器
 * 返回true表示放行，false表示进行拦截
 * Created by zhangyu on 2017/11/8.
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

    private Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    /**
     * 拦截判断用户是否登录
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        //获取请求路径
        String requestURI = request.getRequestURI();


        //放行静态资源，如img图片加载请求
        if(requestURI.startsWith("/static")) {
            return true;
        }
        //放行登录页面
        if("".equals(requestURI) || "/".equals(requestURI)) {
            return true;
        }
        //根据session判断是否已经登录
        HttpSession session = request.getSession();
        Employee employee = (Employee)session.getAttribute("current_employee");
        if(employee != null) {
            return true;
        }
        response.sendRedirect("/");
        return false;
    }


}

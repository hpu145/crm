package com.kaishengit.crm.auth;

import com.kaishengit.crm.entity.Employee;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * Created by zhangyu on 2017/11/24.
 */
public class ShiroUtil {

    /**
     * 获取当前登录的employee账户
     * @return
     */
    public static Employee getCurrentEmployee() {
        return (Employee)getSubject().getPrincipal();
    }

    /**
     * 获得登录的subject对象
     * @return
     */
    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

}

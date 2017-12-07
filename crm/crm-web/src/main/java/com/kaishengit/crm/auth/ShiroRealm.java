package com.kaishengit.crm.auth;

import com.kaishengit.crm.entity.Dept;
import com.kaishengit.crm.entity.Employee;
import com.kaishengit.crm.service.EmployeeService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyu on 2017/11/24.
 */
public class ShiroRealm extends AuthorizingRealm {

    private EmployeeService employeeService;

    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    /**
     * 角色或者权限认证
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取当前的登录对象
        Employee employee = (Employee) principalCollection.getPrimaryPrincipal();
        //根据登录的对象获取它所在的部门列表
        List<Dept> deptList = employeeService.findDeptListByEmployeeId(employee.getId());
        //获取deptList集合的deptName，创建字符串列表
        List<String> deptNameList = new ArrayList<>();
        for (Dept dept : deptList) {
            deptNameList.add(dept.getDeptName());
        }

        //将部门名称当作当前用户的角色
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRoles(deptNameList);
        //权限
        //simpleAuthorizationInfo.setStringPermissions();
        return simpleAuthorizationInfo;
    }

    /**
     * 登录认证时使用
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String userName = token.getUsername();
        Employee employee = employeeService.findByMobile(userName);
        if (employee != null) {
            return new SimpleAuthenticationInfo(employee,employee.getPassword(),getName());
        }
        return null;
    }

}

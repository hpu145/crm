package com.kaishengit.crm.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kaishengit.crm.entity.Customer;
import com.kaishengit.crm.entity.Dept;
import com.kaishengit.crm.entity.Employee;
import com.kaishengit.crm.entity.EmployeeDept;
import com.kaishengit.crm.example.CustomerExample;
import com.kaishengit.crm.example.DeptExample;
import com.kaishengit.crm.example.EmployeeDeptExample;
import com.kaishengit.crm.example.EmployeeExample;
import com.kaishengit.crm.exception.AuthenticationException;
import com.kaishengit.crm.exception.ServiceException;
import com.kaishengit.crm.mapper.CustomerMapper;
import com.kaishengit.crm.mapper.DeptMapper;
import com.kaishengit.crm.mapper.EmployeeDeptMapper;
import com.kaishengit.crm.mapper.EmployeeMapper;
import com.kaishengit.crm.service.CustomerService;
import com.kaishengit.crm.service.EmployeeService;
import com.kaishengit.crm.util.WeiXinUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangyu on 2017/11/7.
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private DeptMapper deptMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private EmployeeDeptMapper employeeDeptMapper;
    @Autowired
    private WeiXinUtil weiXinUtil;

    private Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    /**
     * 公司id为常量1
     */
    private static final Integer COMPANY_ID = 1;

    /**
     * 登录
     * @param mobile
     * @param password
     * @return
     */
    @Override
    public Employee login(String mobile, String password) {
        EmployeeExample employeeExample = new EmployeeExample();
        employeeExample.createCriteria().andMobileEqualTo(mobile);
        List<Employee> employeeList = employeeMapper.selectByExample(employeeExample);
        Employee employee = null;
        //当employeeList集合为空时，就不会有employeeList.get(0)这个值存在
        if(employeeList != null && !employeeList.isEmpty()) {
            employee = employeeList.get(0);
        }
        if(employee != null && employee.getPassword().equals(password)) {
            logger.info("{}在{}登录成功",employee.getEmployeeName(),new Date());
            return employee;
        } else {
            throw new AuthenticationException("账号或密码输入错误");
        }
    }

    /**
     * 添加部门
     * @param deptName
     */
    @Override
    @Transactional
    public void addNewDept(String deptName) {
        //首先判断该部门是不是已经存在
        DeptExample deptExample = new DeptExample();
        deptExample.createCriteria().andDeptNameEqualTo(deptName);
        List<Dept> deptList = deptMapper.selectByExample(deptExample);
        Dept dept = null;
        if(deptList != null && !deptList.isEmpty()) {
            dept = deptList.get(0);
        }
        //如果存在，抛出ServiceException
        if(dept != null) {
            throw new ServiceException("您要添加的部门已存在，请重新输入");
        }
        //如果不存在，进行添加新的部门
        dept = new Dept();
        dept.setDeptName(deptName);
        dept.setpId(COMPANY_ID);
        deptMapper.insertSelective(dept);
        //发送到微信
        weiXinUtil.createDept(dept.getId(),COMPANY_ID,deptName);

        //纪录日志
        logger.info("添加新的部门{}成功",deptName);
    }

    @Override
    public List<Dept> findAllDept() {
        DeptExample deptExample = new DeptExample();
        return deptMapper.selectByExample(deptExample);
    }

    @Override
    public List<Employee> pageForEmployee(Map<String, Object> queryParam) {
        Integer start = (Integer) queryParam.get("start");
        Integer length = (Integer) queryParam.get("length");
        Integer deptId = (Integer) queryParam.get("deptId");
        String employeeName = (String) queryParam.get("employeeName");


        if(deptId == null || COMPANY_ID.equals(deptId)) {
            deptId = null;
        }

        List<Employee> employeeList = employeeMapper.findByDeptId(employeeName,deptId,start,length);
        //System.out.println("大小 ：" + employeeList.size());
        return employeeList;

    }

    /**
     * 根据部门的id获取employee的总数量
     * @param deptId
     * @return
     */
    @Override
    public Long employeeCountByDeptId(Integer deptId) {
        if(deptId == null || COMPANY_ID.equals(deptId)) {
            deptId = null;
        }
        return employeeMapper.countByDeptId(deptId);
    }

    /**
     * 添加新的员工
     * @param employeeName
     * @param mobile
     * @param password
     * @param deptIds
     */
    @Override
    @Transactional  //事务管理
    public void addNewEmployee(String employeeName, String mobile,
                               String password, Integer[] deptIds) {
        //根据mobile查找添加的员工是否存在
        EmployeeExample employeeExample = new EmployeeExample();
        employeeExample.createCriteria().andMobileEqualTo(mobile);
        List<Employee> employeeList = employeeMapper.selectByExample(employeeExample);
        Employee employee = null;
        if (employeeList != null && !employeeList.isEmpty()) {
            employee = employeeList.get(0);
        }
        if(employee != null) {
            throw new ServiceException("该手机号已被使用");
        }
        //封装employee
        employee = new Employee();
        employee.setEmployeeName(employeeName);
        employee.setMobile(mobile);
        employee.setPassword(password);
        employee.setCreateTime(new Date());
        employee.setUpdateTime(new Date());
        employeeMapper.insertSelective(employee);

        //最后，添加employee与dept的关系
        for(Integer deptId : deptIds) {
            EmployeeDept employeeDept = new EmployeeDept();
            employeeDept.setDeptId(deptId);
            employeeDept.setEmployeeId(employee.getId());
            employeeDeptMapper.insertSelective(employeeDept);
        }

        //发送到微信
        weiXinUtil.createUser(employee.getId(),employeeName,mobile, Arrays.asList(deptIds));
        logger.info("添加员工{}成功",employeeName);

    }

    @Override
    public void deleteEmployeeById(Integer id) {
        // TODO 判断该员工下是否有客户
        CustomerExample customerExample = new CustomerExample();
        customerExample.createCriteria().andEmployeeIdEqualTo(id);
        List<Customer> customerList = customerMapper.selectByExample(customerExample);
        if (customerList.size() > 0) {
            throw new ServiceException("该员工下有对应的客户，无法删除");
        }
        //1.删除和部门的关联关系
        EmployeeDeptExample employeeDeptExample = new EmployeeDeptExample();
        employeeDeptExample.createCriteria().andEmployeeIdEqualTo(id);
        employeeDeptMapper.deleteByExample(employeeDeptExample);
        //2.删除账号
        employeeMapper.deleteByPrimaryKey(id);
        //发送到微信
        weiXinUtil.delUser(id);
    }

    /**
     * 查找所有员工
     * @return List<Employee>
     */
    @Override
    public List<Employee> findAllEmployee() {
        return employeeMapper.selectByExample(new EmployeeExample());
    }

    /**
     * 根据用户名（电话号）查找员工Employee
     * @param userName
     * @return
     */
    @Override
    public Employee findByMobile(String userName) {
        EmployeeExample employeeExample = new EmployeeExample();
        employeeExample.createCriteria().andMobileEqualTo(userName);
        List<Employee> employeeList = employeeMapper.selectByExample(employeeExample);
        Employee employee = null;
        if (employeeList != null && !employeeList.isEmpty()) {
            return employeeList.get(0);
        }
        return null;
    }

    /**
     * 根据employeeId获取所在的部门列表
     * @param id
     * @return
     */
    @Override
    public List<Dept> findDeptListByEmployeeId(Integer id) {
        return employeeMapper.findDeptListByEmployeeId(id);
    }

    /**
     * 更改员工账户密码
     * @param employeeId
     * @param password
     */
    @Override
    public void updateEmployeePassword(Integer employeeId, String password) {
        //MD5加密 保存到数据库
        String md5 = DigestUtils.md5Hex(password);

        Employee employee = employeeMapper.selectByPrimaryKey(employeeId);
        employee.setPassword(md5);
        employeeMapper.updateByPrimaryKeySelective(employee);
    }


}

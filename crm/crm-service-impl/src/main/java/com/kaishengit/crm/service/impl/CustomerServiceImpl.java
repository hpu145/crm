package com.kaishengit.crm.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kaishengit.crm.entity.Customer;
import com.kaishengit.crm.entity.Employee;
import com.kaishengit.crm.example.CustomerExample;
import com.kaishengit.crm.exception.ServiceException;
import com.kaishengit.crm.mapper.CustomerMapper;
import com.kaishengit.crm.mapper.EmployeeMapper;
import com.kaishengit.crm.service.CustomerService;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangyu on 2017/11/9.
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private EmployeeMapper employeeMapper;
    //实现类中添加日志功能
    private Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);
    //将properties中的配置文件注入实现类,注入的结果是字符串，也可以用enviro...注入
    //springEL表达式
    @Value("#{'${customer.source}'.split(',')}")
    private List<String> customerSource;
    @Value("#{'${customer.trade}'.split(',')}")
    private List<String> customerTrade;



    /**
     * 添加新的客户
     * @param
     * @param employee
     */
    @Override
    public void addNewCustomer(Customer customer, Employee employee) {
        CustomerExample customerExample = new CustomerExample();
        customerExample.createCriteria().andMobileEqualTo(customer.getMobile());
        List<Customer> customerList = customerMapper.selectByExample(customerExample);
        Customer customer_find = null;
        if(customerList != null && !customerList.isEmpty()) {
            customer_find = customerList.get(0);
        }
        if(customer_find != null) {
            throw new ServiceException("对不起，该手机号已被其他客户占用");
        }
        customer.setCreateTime(new Date());
        customer.setUpdateTime(new Date());
        customer.setEmployeeId(employee.getId());
        customerMapper.insertSelective(customer);
        logger.info("员工id为：{}的员工添加了新的客户{}",customer.getEmployeeId(),customer.getCustName());
    }

    /**
     * 展示我的所有客户并进行分页
     * @param employee
     * @param pageNum
     * @return
     */
    @Override
    public PageInfo<Customer> findPageForMyCustomers(Employee employee, Integer pageNum) {
        CustomerExample customerExample = new CustomerExample();
        customerExample.createCriteria().andEmployeeIdEqualTo(employee.getId());
        System.out.println("employee" + employee.getId());
        PageHelper.startPage(pageNum,5);
        List<Customer> customerList = customerMapper.selectByExample(customerExample);
        //System.out.println("客户：" + customerList.size());

        return new PageInfo<Customer>(customerList);
    }


    /**
     * 获得所有客户的来源
     * @return
     */
    @Override
    public List<String> findAllCustomerSource() {
        return customerSource;
    }

    /**
     * 获取所有的行业名称
     * @return
     */
    @Override
    public List<String> findAllCustomerTrade() {
        return customerTrade;
    }


    /**
     * 根据id查询对应客户
     * @param id
     * @return
     */
    @Override
    public Customer findCustomerById(Integer id) {
        return customerMapper.selectByPrimaryKey(id);
    }


    /**
     * 根据id删除客户
     * @param customer
     */
    @Override
    public void deleteCustomer(Customer customer) {
        //TODO 检查是否有关联交易记录
        //TODO 检查是否有关联待办事项
        //TODO 检查是否有关联资料文件
        customerMapper.deleteByPrimaryKey(customer.getId());
    }

    /**
     * 放入公海
     * @param customer
     */
    @Override
    public void putPublicCustomer(Customer customer) {
        Employee employee = employeeMapper.selectByPrimaryKey(customer.getEmployeeId());
        customer.setEmployeeId(null);
        if(customer.getReminder() == null) {
            customer.setReminder(employee.getEmployeeName()+"将该客户放入公海");
        } else {
            customer.setReminder(customer.getReminder() + " " + employee.getEmployeeName()+"将该客户放入公海");
        }
        customerMapper.updateByPrimaryKey(customer);
    }

    /**
     * 编辑客户
     * @param customer
     */
    @Override
    public void editCustomer(Customer customer) {
        customer.setUpdateTime(new Date());
        customerMapper.updateByPrimaryKeySelective(customer);
    }

    /**
     * 转交客户
     * @param customer
     * @param toEmployeeId
     */
    @Override
    public void transCustomer(Customer customer, Integer toEmployeeId) {
        Employee employee = employeeMapper.selectByPrimaryKey(customer.getEmployeeId());
        customer.setEmployeeId(toEmployeeId);
        if(customer.getReminder() == null) {
            customer.setReminder("从"+employee.getEmployeeName()+"转交过来 ");
        } else {
            customer.setReminder(customer.getReminder()+" "+"从"+employee.getEmployeeName()+"转交过来 ");
        }
        customerMapper.updateByPrimaryKeySelective(customer);
    }

    /**
     * 查找公海客户
     * @return
     */
    @Override
    public List<Customer> findPublicCustomer() {
        return customerMapper.findPublicCustomer();
    }

    /**
     * 导出为csv文件
     * @param outputStream
     * @param employee
     */
    @Override
    public void exportCsvFileToOutputStream(OutputStream outputStream, Employee employee) throws IOException {
        List<Customer> customerList = findAllCustomerByEmployeeId(employee);
        StringBuilder sb = new StringBuilder();
        sb.append("姓名")
                .append(",")
                .append("联系电话")
                .append(",")
                .append("职位")
                .append(",")
                .append("地址")
                .append("\r\n");
        for(Customer customer : customerList) {
            sb.append(customer.getCustName())
                    .append(",")
                    .append(customer.getMobile())
                    .append(",")
                    .append(customer.getJobTitle())
                    .append(",")
                    .append(customer.getAddress())
                    .append("\r\n");
        }
        IOUtils.write(sb.toString(),outputStream,"GBK");
        outputStream.flush();
        outputStream.close();
    }

    /**
     * 导出为xls文件
     * @param outputStream
     * @param employee
     */
    @Override
    public void exportXlsFileToOutputStream(OutputStream outputStream, Employee employee) throws IOException {
        List<Customer> customerList = findAllCustomerByEmployeeId(employee);
        //创建工作表
        Workbook workbook = new HSSFWorkbook();
        //创建sheet
        Sheet sheet = workbook.createSheet("我的客户");
        //创建行
        Row titleRow = sheet.createRow(0);
        //创建单元格
        Cell nameCell = titleRow.createCell(0);
        nameCell.setCellValue("姓名");
        titleRow.createCell(1).setCellValue("电话");
        titleRow.createCell(2).setCellValue("职位");
        titleRow.createCell(3).setCellValue("地址");

        for(int i = 0;i < customerList.size();i++) {
            Customer customer = customerList.get(i);

            Row dataRow = sheet.createRow(i+1);
            dataRow.createCell(0).setCellValue(customer.getCustName());
            dataRow.createCell(1).setCellValue(customer.getMobile());
            dataRow.createCell(2).setCellValue(customer.getJobTitle());
            dataRow.createCell(3).setCellValue(customer.getAddress());
        }

        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();

    }

    /**
     * 查找某个员工对应的所有客户
     * @param employee
     * @return
     */
    @Override
    public List<Customer> findMyCustomerList(Employee employee) {
        CustomerExample customerExample = new CustomerExample();
        customerExample.createCriteria().andEmployeeIdEqualTo(employee.getId());
        return customerMapper.selectByExample(customerExample);
    }

    @Override
    public Customer findPublicCustomerById(Integer custId) {
        List<Customer> customers = findPublicCustomer();
        for (Customer customer :customers) {
            if (customer.getId().equals(custId)) {
                return customer;
            }
        }
        return null;
    }

    @Override
    public void transPublicToMy(Customer customer, Integer id) {
        customer.setEmployeeId(id);
        customerMapper.updateByPrimaryKeySelective(customer);
    }

    /**
     * 根据员工id获取所有的客户列表
     * @param employee
     * @return
     */
    private List<Customer> findAllCustomerByEmployeeId(Employee employee) {
        CustomerExample customerExample = new CustomerExample();
        customerExample.createCriteria().andEmployeeIdEqualTo(employee.getId());
        return customerMapper.selectByExample(customerExample);
    }


}

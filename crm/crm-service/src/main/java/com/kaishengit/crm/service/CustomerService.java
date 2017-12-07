package com.kaishengit.crm.service;

import com.github.pagehelper.PageInfo;
import com.kaishengit.crm.entity.Customer;
import com.kaishengit.crm.entity.Employee;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by zhangyu on 2017/11/9.
 */
public interface CustomerService {



    void addNewCustomer(Customer customer, Employee employee);

    PageInfo<Customer> findPageForMyCustomers(Employee employee, Integer pageNum);

    /**
     * 获得所有客户的来源
     * @return
     */
    List<String> findAllCustomerSource();

    /**
     * 获取所有的行业名称
     * @return
     */
    List<String> findAllCustomerTrade();

    /**
     * 根据id查询对应客户
     * @param id
     * @return
     */
    Customer findCustomerById(Integer id);

    /**
     * 删除客户
     * @param customer
     */
    void deleteCustomer(Customer customer);

    /**
     * 放入公海
     * @param customer
     */
    void putPublicCustomer(Customer customer);

    /**
     * 编辑客户
     * @param customer
     */
    void editCustomer(Customer customer);

    /**
     * 转交客户
     * @param customer
     * @param toEmployeeId
     */
    void transCustomer(Customer customer, Integer toEmployeeId);

    /**
     * 查找公海用户
     * @return
     */
    List<Customer> findPublicCustomer();

    /**
     * 导出为csv文件
     * @param outputStream
     * @param employee
     */
    void exportCsvFileToOutputStream(OutputStream outputStream, Employee employee) throws IOException;

    /**
     * 导出为xls文件
     * @param outputStream
     * @param employee
     */
    void exportXlsFileToOutputStream(OutputStream outputStream, Employee employee) throws IOException;

    List<Customer> findMyCustomerList(Employee employee);


    Customer findPublicCustomerById(Integer custId);

    void transPublicToMy(Customer customer, Integer id);

}

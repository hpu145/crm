package com.kaishengit.crm.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kaishengit.crm.controller.exception.ForbiddenException;
import com.kaishengit.crm.controller.exception.NotFoundException;
import com.kaishengit.crm.entity.Customer;
import com.kaishengit.crm.entity.Employee;
import com.kaishengit.crm.exception.ServiceException;
import com.kaishengit.crm.service.CustomerService;
import com.kaishengit.crm.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * 客户管理的控制器
 * Created by zhangyu on 2017/11/9.
 */
@Controller
@RequestMapping("/customer")
public class CustomerController extends BaseController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private EmployeeService employeeService;

    /**
     * 展示所有客户
     * @param model
     * @return
     */
    @GetMapping("/customer-my")
    public String findAllCustomer(@RequestParam(required = false,defaultValue = "1",name="p") Integer pageNum,
                                  Model model,HttpSession session) {
        //访问我的客户，对客户数据进行分页，还要判断该客户是不是属于登录账户的客户
        Employee employee = getCurrentEmployee(session);
        PageInfo<Customer> customerPageInfo = customerService.findPageForMyCustomers(employee,pageNum);
        model.addAttribute("page",customerPageInfo);
        return "customer/customer-my";
    }

    /**
     * 公海客户
     * @return
     */
    @GetMapping("/customer-public")
    public String publicCustomer(Model model) {
        List<Customer> customerList = customerService.findPublicCustomer();
        System.out.println("集合："+customerList.size());
        model.addAttribute("customerList",customerList);
        return "customer/customer-public";
    }

    /**
     * 新增客户
     * 需要把行业与客户来源带到前端页面
     * @return
     */
    @GetMapping("/customer-my/new")
    public String addNewCustomer(Model model) {
        model.addAttribute("sources",customerService.findAllCustomerSource());
        model.addAttribute("trades",customerService.findAllCustomerTrade());
        return "customer/customer-new";
    }

    @PostMapping("/customer-my/new")
    public String addNewCustomer(Customer customer, RedirectAttributes redirectAttributes,HttpSession session) {
        try {
            Employee employee = getCurrentEmployee(session);
            customerService.addNewCustomer(customer,employee);
        } catch (ServiceException ex) {
            redirectAttributes.addFlashAttribute("message",ex.getMessage());
            return "redirect:/customer/customer-my/new";
        }
        redirectAttributes.addFlashAttribute("message","添加客户成功");
        return "redirect:/customer/customer-my";
    }

    /**
     * 根据id查找对应客户
     * @param id
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/customer-my/{id:\\d+}")
    public String showCustomerDetail(@PathVariable Integer id,Model model,HttpSession session) {
        Customer customer = checkCustomerRole(id,session);
        model.addAttribute("customer",customer);
        model.addAttribute("employeeList",employeeService.findAllEmployee());
        return "customer/customer-detail";
    }

    /**
     * 根据客户id删除客户,删除成功后跳转到我的客户列表
     * @param id
     */
    @GetMapping("/customer-my/delete/{id:\\d+}")
    public String deleteMyCustomer(@PathVariable Integer id,HttpSession session,RedirectAttributes redirectAttributes) {
        Customer customer = checkCustomerRole(id,session);
        customerService.deleteCustomer(customer);
        redirectAttributes.addFlashAttribute("message","您已成功删除该客户!");
        return "redirect:/customer/customer-my";
    }

    /**
     * 根据客户id与当前登录的employee账号判断权限
     * @param id
     * @param session
     * @return
     */
    private Customer checkCustomerRole(Integer id,HttpSession session) {
        //1.判断该id对应的客户是否存在
        Customer customer = customerService.findCustomerById(id);
        if (customer ==null) {
            //404
            throw new NotFoundException("对不起，无法找到" + id + "对应的客户");
        }
        //2.判断登录的员工账号是否有权限查看该客户信息
        Employee employee = getCurrentEmployee(session);
        if(!employee.getId().equals(customer.getEmployeeId())) {
            //403
            throw new ForbiddenException("对不起，您无权限查看" + id + "对应的客户");
        }
        return customer;
    }

    @GetMapping("/customer-my/public/{id:\\d+}")
    public String publicCustomer(@PathVariable Integer id,HttpSession session,RedirectAttributes redirectAttributes) {
        Customer customer = checkCustomerRole(id,session);
        customerService.putPublicCustomer(customer);
        redirectAttributes.addFlashAttribute("message","您已成功将该客户放入公海!");
        return "redirect:/customer/customer-my";
    }

    /**
     * 编辑客户
     * @return
     */
    @GetMapping("/customer-my/edit/{id:\\d+}")
    public String editCustomer(@PathVariable Integer id,HttpSession session,Model model) {
        Customer customer = checkCustomerRole(id,session);
        model.addAttribute("customer",customer);
        model.addAttribute("sources",customerService.findAllCustomerSource());
        model.addAttribute("trades",customerService.findAllCustomerTrade());
        return "customer/customer-edit";
    }
    @PostMapping("/customer-my/edit/{id:\\d+}")
    public String editCustomer(Customer customer,HttpSession session,RedirectAttributes redirectAttributes) {
        checkCustomerRole(customer.getId(),session);
        customerService.editCustomer(customer);
        redirectAttributes.addFlashAttribute("message","编辑成功");
        return "redirect:/customer/customer-my/" + customer.getId();
    }

    /**
     * 转交客户
     * @param customerId
     * @param toEmployeeId
     * @param session
     * @param redirectAttributes
     * @return
     */
    @GetMapping("/customer-my/{customerId:\\d+}/trans/{toEmployeeId:\\d+}")
    public String transCustomer(@PathVariable Integer customerId,@PathVariable Integer toEmployeeId,
                                HttpSession session,RedirectAttributes redirectAttributes) {
        Customer customer = checkCustomerRole(customerId,session);
        customerService.transCustomer(customer,toEmployeeId);
        redirectAttributes.addFlashAttribute("message","您已成功转交客户");
        return "redirect:/customer/customer-my";
    }


    /**
     * 将数据导出为csv文件
     * @param response
     * @param session
     * @throws IOException
     */
    @GetMapping("/customer-my/export.csv")
    public void exportCsvData(HttpServletResponse response,HttpSession session)
            throws IOException{
        Employee employee = getCurrentEmployee(session);
        response.setContentType("text/csv;charset=GBK");
        String fileName = new String("我的客户.csv".getBytes("UTF-8"),"ISO8859-1");
        response.addHeader("Content-Disposition","attachment; filename=\""+fileName+"\"");
        OutputStream outputStream = response.getOutputStream();
        customerService.exportCsvFileToOutputStream(outputStream,employee);
    }

    /**
     * 将数据导出为xls文件
     * @param response
     * @param session
     * @throws IOException
     */
    @GetMapping("/customer-my/export.xls")
    public void exportXlsData(HttpServletResponse response,HttpSession session)
            throws IOException{
        Employee employee = getCurrentEmployee(session);
        response.setContentType("application/vnd.ms-excel");
        String fileName = new String("我的客户.xls".getBytes("UTF-8"),"ISO8859-1");
        response.addHeader("Content-Disposition","attachment; filename=\""+fileName+"\"");
        OutputStream outputStream = response.getOutputStream();
        customerService.exportXlsFileToOutputStream(outputStream,employee);
    }

    /**
     * 根据id查找对应公海客户
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/customer-public/{id:\\d+}")
    public String showPublicCustomerDetail(@PathVariable Integer id,Model model) {
        Customer customer = customerService.findPublicCustomerById(id);
        model.addAttribute("customer",customer);
        return "customer/customer-detail-pub";
    }

    /**
     * 公海客户转交给自己
     */
    @GetMapping("/customer-public-my/{id:\\d+}")
    public String transCustomerToMy(@PathVariable Integer id,HttpSession session,RedirectAttributes redirectAttributes) {
        Employee employee = getCurrentEmployee(session);
        Customer customer = customerService.findCustomerById(id);
        customerService.transPublicToMy(customer,employee.getId());
        redirectAttributes.addFlashAttribute("message","您已成功将该公海客户转交给自己");
        return "redirect:/customer/customer-my";
    }












}

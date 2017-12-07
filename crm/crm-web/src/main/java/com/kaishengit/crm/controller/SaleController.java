package com.kaishengit.crm.controller;

import com.github.pagehelper.PageInfo;
import com.kaishengit.crm.controller.exception.ForbiddenException;
import com.kaishengit.crm.controller.exception.NotFoundException;
import com.kaishengit.crm.entity.Employee;
import com.kaishengit.crm.entity.SaleChance;
import com.kaishengit.crm.entity.SaleChanceRecord;
import com.kaishengit.crm.service.CustomerService;
import com.kaishengit.crm.service.SaleChanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by zhangyu on 2017/11/13.
 */
@Controller
@RequestMapping("/sale")
public class SaleController extends BaseController{

    @Autowired
    private CustomerService customerService;
    @Autowired
    private SaleChanceService saleChanceService;

    /**
     * 查找我的销售机会列表
     * @return
     */
    @GetMapping("/chance-my")
    public String myChance(@RequestParam(required = false,defaultValue = "1",name = "p")Integer pageNum,
                           Model model, HttpSession session) {
        Employee employee = getCurrentEmployee(session);
        PageInfo<SaleChance> saleChancePageInfo = saleChanceService.findPageForSaleChance(pageNum,employee);
        model.addAttribute("page",saleChancePageInfo);
        return "sale/chance-my";
    }


    /**
     * 添加机会
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/chance-my/new")
    public String newChance(Model model, HttpSession session) {
        Employee employee = getCurrentEmployee(session);
        //当前登录员工的客户列表
        model.addAttribute("customerList",customerService.findMyCustomerList(employee));
        //进度列表
        model.addAttribute("saleProcess",saleChanceService.findSaleProcess());
        return "sale/chance-new";
    }

    @PostMapping("/chance-my/new")
    public String newChance(SaleChance saleChance,HttpSession session,
                            RedirectAttributes redirectAttributes) {
        Employee employee = getCurrentEmployee(session);
        saleChanceService.addNewChance(saleChance,employee);
        redirectAttributes.addFlashAttribute("message","您已成功添加新的销售机会");
        return "redirect:/sale/chance-my";
    }

    /**
     * 销售机会详情
     * @return
     */
    @GetMapping("/chance-my/detail/{id:\\d+}")
    public String showChanceInfo(@PathVariable Integer id,
                                 HttpSession session,Model model) {
        SaleChance saleChance = checkSaleChanceRole(id,session);

        //查询该销售机会对应的销售跟进记录
        List<SaleChanceRecord> recordList = saleChanceService.findSaleChanceRecordListBySaleId(id);

        model.addAttribute("recordList",recordList);
        model.addAttribute("saleChance",saleChance);
        model.addAttribute("processList",saleChanceService.findSaleProcess());

        return "sale/chance-detail";
    }

    /**
     * 根据销售机会的id与当前登录员工的session进行权限控制
     * @param id
     * @param session
     * @return
     */
    private SaleChance checkSaleChanceRole(Integer id,HttpSession session) {
        Employee employee = getCurrentEmployee(session);
        SaleChance saleChance = saleChanceService.findSaleChanceWithCustomerBySaleId(id);
        if(saleChance == null) {
            throw new NotFoundException();
        }
        if (!employee.getId().equals(saleChance.getEmployeeId())) {
            throw new ForbiddenException();
        }
        return saleChance;
    }

    /**
     * 根据id删除某个销售机会
     * @param id
     * @param session
     * @param redirectAttributes
     * @return
     */
    @GetMapping("/chance-my/delete/{id:\\d+}")
    public String deleteSaleChance(@PathVariable Integer id,
                                   HttpSession session,RedirectAttributes redirectAttributes) {
        checkSaleChanceRole(id,session);
        saleChanceService.deleteSaleChanceById(id);
        redirectAttributes.addFlashAttribute("message","您已成功删除销售机会");
        return "redirect:/sale/chance-my";
    }

    /**
     * 修改销售机会的进度
     * @return
     */
    @PostMapping("/chance-my/{id:\\d+}/process/update")
    public String updateSaleChanceProcess(@PathVariable Integer id,
                                          String process,HttpSession session) {
        checkSaleChanceRole(id,session);
        saleChanceService.updateSaleChanceProcess(id,process);
        return "redirect:/sale/chance-my/detail/"+id;
    }


    /**
     * 添加新的跟进记录
     * @param saleChanceRecord
     * @param session
     * @return
     */
    @PostMapping("/chance-my/new/record")
    public String newSaleChanceRecord(SaleChanceRecord saleChanceRecord,HttpSession session) {
        checkSaleChanceRole(saleChanceRecord.getSaleId(),session);
        saleChanceService.newSaleChanceRecord(saleChanceRecord);
        return "redirect:/sale/chance-my/detail/" + saleChanceRecord.getSaleId();
    }








}

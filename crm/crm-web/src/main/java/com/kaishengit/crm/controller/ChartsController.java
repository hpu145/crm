package com.kaishengit.crm.controller;

import com.kaishengit.crm.service.ChartService;
import com.kaishengit.web.result.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 统计图表控制器
 * Created by zhangyu on 2017/11/18.
 */
@Controller
@RequestMapping("/charts")
public class ChartsController {


    @Autowired
    private ChartService chartService;


    @GetMapping
    public String showCharts() {
        return "charts/chart";
    }

    /**
     * 客户级别统计
     * @return
     */
    @GetMapping("/customer/level")
    @ResponseBody
    public AjaxResult showCustomerLevel() {
        List<Map<String,Object>> result = chartService.findCustomerCountByLevel();
        return AjaxResult.successWithData(result);
    }

    /**
     * 每月新增客户的数量
     * @return
     */
    @GetMapping("/customer/increase")
    @ResponseBody
    public AjaxResult increaseCustomerPerMonth() {
        List<Map<String,Object>> result = chartService.increaseCustomerPerMonth();
        return AjaxResult.successWithData(result);
    }

    /**
     * 查找员工对应的销售机会
     */
    @GetMapping("/employee/salechance")
    @ResponseBody
    public AjaxResult employeeSaleChance() {
        List<Map<String,Object>> result = chartService.findEmployeeSaleChance();
        return AjaxResult.successWithData(result);
    }



}

package com.kaishengit.crm.service.impl;

import com.kaishengit.crm.entity.SaleChance;
import com.kaishengit.crm.mapper.CustomerMapper;
import com.kaishengit.crm.mapper.SaleChanceMapper;
import com.kaishengit.crm.service.ChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangyu on 2017/11/18.
 */
@Service
public class ChartServiceImpl implements ChartService {

    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private SaleChanceMapper saleChanceMapper;

    /**
     * 客户级别统计
     * @return
     */
    @Override
    public List<Map<String, Object>> findCustomerCountByLevel() {
        return customerMapper.findCustomerCountByLevel();
    }

    /**
     * 每月新增客户的数量
     * @return
     */
    @Override
    public List<Map<String, Object>> increaseCustomerPerMonth() {
        return customerMapper.increaseCustomerPerMonth();
    }

    /**
     * 查找员工对应的销售机会
     */
    @Override
    public List<Map<String, Object>> findEmployeeSaleChance() {
        return saleChanceMapper.findEmployeeSaleChance();
    }
}

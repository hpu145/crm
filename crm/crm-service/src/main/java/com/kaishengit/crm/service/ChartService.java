package com.kaishengit.crm.service;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangyu on 2017/11/18.
 */

public interface ChartService {
    /**
     * 客户级别统计
     * @return
     */
    List<Map<String,Object>> findCustomerCountByLevel();

    /**
     * 每月新增客户的数量
     * @return
     */
    List<Map<String,Object>> increaseCustomerPerMonth();

    /**
     * 查找员工对应的销售机会
     */
    List<Map<String,Object>> findEmployeeSaleChance();
}

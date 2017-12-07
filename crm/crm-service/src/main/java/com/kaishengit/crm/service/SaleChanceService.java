package com.kaishengit.crm.service;

import com.github.pagehelper.PageInfo;
import com.kaishengit.crm.entity.Employee;
import com.kaishengit.crm.entity.SaleChance;
import com.kaishengit.crm.entity.SaleChanceRecord;

import java.util.List;

/**
 * Created by zhangyu on 2017/11/13.
 */
public interface SaleChanceService {


    /**
     * 添加新的机会
     * @param saleChance
     */
    void addNewChance(SaleChance saleChance, Employee employee);

    public List<String> findSaleProcess();

    /**
     * 分页
     * 查找所有的销售机会
     * @return
     */
    PageInfo<SaleChance> findPageForSaleChance(Integer pageNum, Employee employee);

    /**
     * 查找附带customer对象的销售机会
     * @param id
     * @return
     */
    SaleChance findSaleChanceWithCustomerBySaleId(Integer id);


    /**
     * 根据销售机会的ID查询对应的跟进记录列表
     *
     * @param id
     * @return
     */
    List<SaleChanceRecord> findSaleChanceRecordListBySaleId(Integer id);

   /**
     * 根据id删除某个销售机会
     * @param id
     */
    void deleteSaleChanceById(Integer id);

    /**
     * 修改销售机会的进度
     */
    void updateSaleChanceProcess(Integer id, String process);

    /**
     * 添加新的跟进记录
     * @param saleChanceRecord
     */
    void newSaleChanceRecord(SaleChanceRecord saleChanceRecord);

    /**
     * 根据员工Id查找对应的销售机会列表
     * @param employeeId
     * @return
     */
    List<SaleChance> findMySaleChanceList(Integer employeeId);

}

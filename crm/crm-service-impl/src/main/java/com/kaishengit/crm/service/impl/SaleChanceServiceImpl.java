package com.kaishengit.crm.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kaishengit.crm.entity.Customer;
import com.kaishengit.crm.entity.Employee;
import com.kaishengit.crm.entity.SaleChance;
import com.kaishengit.crm.entity.SaleChanceRecord;
import com.kaishengit.crm.example.SaleChanceExample;
import com.kaishengit.crm.example.SaleChanceRecordExample;
import com.kaishengit.crm.mapper.CustomerMapper;
import com.kaishengit.crm.mapper.SaleChanceMapper;
import com.kaishengit.crm.mapper.SaleChanceRecordMapper;
import com.kaishengit.crm.service.SaleChanceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by zhangyu on 2017/11/13.
 */
@Service
public class SaleChanceServiceImpl implements SaleChanceService {

    @Autowired
    private SaleChanceMapper saleChanceMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private SaleChanceRecordMapper saleChanceRecordMapper;

    @Value("#{'${sale.process}'.split(',')}")
    private List<String> saleProcess;


    /**
     * 添加新的机会
     * @param saleChance
     */
    @Override
    @Transactional
    public void addNewChance(SaleChance saleChance, Employee employee) {
        saleChance.setEmployeeId(employee.getId());
        saleChance.setCreateTime(new Date());
        saleChance.setLastTime(new Date());
        //更新对应客户的最后跟进时间
        Customer customer = customerMapper.selectByPrimaryKey(saleChance.getCustId());
        customer.setLastContactTime(new Date());
        customerMapper.updateByPrimaryKeySelective(customer);

        //保存销售机会
        saleChanceMapper.insertSelective(saleChance);

        //判断销售机会的详细内容是否存在，如果存在，保存一条跟进记录
        if(StringUtils.isNotEmpty(saleChance.getContent())) {
            SaleChanceRecord saleChanceRecord = new SaleChanceRecord();
            saleChanceRecord.setContent(saleChance.getContent());
            saleChanceRecord.setCreateTime(new Date());
            saleChanceRecord.setSaleId(saleChance.getId());
            saleChanceRecordMapper.insertSelective(saleChanceRecord);
        }
    }

    /**
     * 获取销售机会的进度列表
     * @return
     */
    @Override
    public List<String> findSaleProcess() {
        return saleProcess;
    }

    /**
     * 根据employee对象查找所有的销售机会分页列表
     * @return
     */
    @Override
    public PageInfo<SaleChance> findPageForSaleChance(Integer pageNum, Employee employee) {
        PageHelper.startPage(pageNum,10);
        List<SaleChance> saleChanceList = saleChanceMapper.findSaleChanceByEmployeeId(employee.getId());
        return new PageInfo<>(saleChanceList);
    }

    /**
     * 查找附带customer对象的销售机会
     * @param id
     * @return
     */
    @Override
    public SaleChance findSaleChanceWithCustomerBySaleId(Integer id) {
        return saleChanceMapper.findSaleChanceWithCustomerBySaleId(id);
    }

    /**
     * 根据销售机会的ID查询对应的跟进记录列表
     *
     * @param id
     * @return
     */
    @Override
    public List<SaleChanceRecord> findSaleChanceRecordListBySaleId(Integer id) {
        SaleChanceRecordExample saleChanceRecordExample = new SaleChanceRecordExample();
        saleChanceRecordExample.createCriteria().andSaleIdEqualTo(id);
        return saleChanceRecordMapper.selectByExample(saleChanceRecordExample);
    }

    /**
     * 根据id删除某个销售机会
     * @param id
     */
    @Override
    @Transactional
    public void deleteSaleChanceById(Integer id) {
        SaleChance saleChance = saleChanceMapper.selectByPrimaryKey(id);

        //1.删除该销售机会对应的跟进记录
        SaleChanceRecordExample recordExample = new SaleChanceRecordExample();
        recordExample.createCriteria().andSaleIdEqualTo(id);
        saleChanceRecordMapper.deleteByExample(recordExample);

        //2.删除销售机会
        saleChanceMapper.deleteByPrimaryKey(id);

        //3.将该销售机会对应客户的最后跟进时间设置为null或空
        //判断对应的客户是否还有其他的销售机会，如果没有就设置为null或空
        //如果有，就将最后跟进时间修改为最近一次销售机会的最后跟进时间
        SaleChanceExample saleChanceExample = new SaleChanceExample();
        saleChanceExample.createCriteria().andCustIdEqualTo(saleChance.getCustId());
        saleChanceExample.setOrderByClause("last_time desc");
        List<SaleChance> saleChances = saleChanceMapper.selectByExample(saleChanceExample);

        Customer customer = customerMapper.selectByPrimaryKey(saleChance.getCustId());
        if (saleChances.isEmpty()) {
            customer.setLastContactTime(null);
        } else {
            customer.setLastContactTime(saleChances.get(0).getLastTime());
        }
        customerMapper.updateByPrimaryKeySelective(customer);
    }

    /**
     * 修改销售机会的进度
     */
    @Override
    @Transactional
    public void updateSaleChanceProcess(Integer id, String process) {
        SaleChance saleChance = saleChanceMapper.selectByPrimaryKey(id);
        //改变进度
        saleChance.setProcess(process);
        //改变销售机会的最后跟进时间
        saleChance.setLastTime(new Date());
        saleChanceMapper.updateByPrimaryKeySelective(saleChance);

        //改变销售机会关联客户的最后跟进时间
        Customer customer = customerMapper.selectByPrimaryKey(saleChance.getCustId());
        customer.setLastContactTime(new Date());
        customerMapper.updateByPrimaryKeySelective(customer);

        //产生一个新的跟进记录
        SaleChanceRecord chanceRecord = new SaleChanceRecord();
        chanceRecord.setSaleId(id);
        chanceRecord.setCreateTime(new Date());
        chanceRecord.setContent("您已将销售机会的当前进度修改为: ["+ process +"]");

        saleChanceRecordMapper.insertSelective(chanceRecord);

    }

    /**
     * 添加新的跟进记录
     * @param saleChanceRecord
     */
    @Override
    public void newSaleChanceRecord(SaleChanceRecord saleChanceRecord) {
        saleChanceRecord.setCreateTime(new Date());
        saleChanceRecordMapper.insertSelective(saleChanceRecord);

        //销售机会的最后跟进时间
        SaleChance saleChance = saleChanceMapper.selectByPrimaryKey(saleChanceRecord.getSaleId());
        saleChance.setLastTime(new Date());
        saleChanceMapper.updateByPrimaryKeySelective(saleChance);

        //关联客户的最后跟进时间
        Customer customer = customerMapper.selectByPrimaryKey(saleChance.getCustId());
        customer.setLastContactTime(new Date());
        customerMapper.updateByPrimaryKeySelective(customer);
    }

    /**
     * 根据员工Id查找对应的销售机会列表
     * @param employeeId
     * @return
     */
    @Override
    public List<SaleChance> findMySaleChanceList(Integer employeeId) {
        SaleChanceExample saleChanceExample = new SaleChanceExample();
        saleChanceExample.createCriteria().andEmployeeIdEqualTo(employeeId);
        return saleChanceMapper.selectByExample(saleChanceExample);
    }


}

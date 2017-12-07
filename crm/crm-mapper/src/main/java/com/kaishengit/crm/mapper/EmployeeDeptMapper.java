package com.kaishengit.crm.mapper;


import com.kaishengit.crm.entity.EmployeeDept;
import com.kaishengit.crm.example.EmployeeDeptExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EmployeeDeptMapper {
    long countByExample(EmployeeDeptExample example);

    int deleteByExample(EmployeeDeptExample example);

    int deleteByPrimaryKey(EmployeeDept employeeDept);

    int insert(EmployeeDept EmployeeDept);

    int insertSelective(EmployeeDept EmployeeDept);

    List<EmployeeDept> selectByExample(EmployeeDeptExample example);

    int updateByExampleSelective(@Param("record") EmployeeDept employeeDept, @Param("example") EmployeeDeptExample example);

    int updateByExample(@Param("record") EmployeeDept employeeDept, @Param("example") EmployeeDeptExample example);
}
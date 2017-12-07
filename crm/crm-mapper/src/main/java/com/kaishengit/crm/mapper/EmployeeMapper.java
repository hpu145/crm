package com.kaishengit.crm.mapper;


import com.kaishengit.crm.entity.Dept;
import com.kaishengit.crm.example.EmployeeExample;
import com.kaishengit.crm.entity.Employee;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EmployeeMapper {
    long countByExample(EmployeeExample example);

    int deleteByExample(EmployeeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Employee record);

    int insertSelective(Employee record);

    List<Employee> selectByExample(EmployeeExample example);

    Employee selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Employee record, @Param("example") EmployeeExample example);

    int updateByExample(@Param("record") Employee record, @Param("example") EmployeeExample example);

    int updateByPrimaryKeySelective(Employee record);

    int updateByPrimaryKey(Employee record);

    List<Employee> findByDeptId(@Param("employeeName")String employeeName,
                                @Param("deptId")Integer deptId,
                                @Param("start")Integer start,@Param("length")Integer length);


    Long countByDeptId(@Param("deptId") Integer deptId);

    List<Dept> findDeptListByEmployeeId(Integer id);

}
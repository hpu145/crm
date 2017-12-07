package com.kaishengit.crm.service;

import com.kaishengit.crm.entity.Employee;
import com.kaishengit.crm.entity.Task;

import java.util.Date;
import java.util.List;

/**
 * Created by zhangyu on 2017/11/14.
 */
public interface TaskService {

    /**
     * 添加新的任务
     */
    void addNewTask(Task task,String remindTime);

    /**
     * 查找我的任务列表
     * @return List<Task>
     */
    List<Task> findTaskListByEmployeeId(Integer id);


    /**
     * 根据待办任务的id查找待办任务
     * @param id
     * @return Task task
     */
    Task findTaskById(Integer id);

    /**
     * 根据id删除任务
     * @param id
     */
    void deleteTaskById(Integer id);

    /**
     * 修改任务的状态
     * @param task
     */
    void updateStateUndoneToDone(Task task);

    /**
     * 根据id修改已选择任务状态
     * 将已经完成状态修改为未完成状态
     */
    void updateStateDoneToUndone(Task task);

    /**
     * 根据task的结束时间与状态查找逾期事项
     * @return
     */
    List<Task> findOverdueTask(List<Task> taskList);

}

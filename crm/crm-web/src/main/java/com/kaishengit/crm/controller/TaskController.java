package com.kaishengit.crm.controller;

import com.kaishengit.crm.controller.exception.ForbiddenException;
import com.kaishengit.crm.controller.exception.NotFoundException;
import com.kaishengit.crm.entity.Employee;
import com.kaishengit.crm.entity.Task;
import com.kaishengit.crm.service.CustomerService;
import com.kaishengit.crm.service.SaleChanceService;
import com.kaishengit.crm.service.TaskService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangyu on 2017/11/14.
 */
@Controller
@RequestMapping("/task")
public class TaskController extends BaseController{

    @Autowired
    private TaskService taskService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private SaleChanceService saleChanceService;


    /**
     * 显示我的待办任务列表
     * @return
     */
    @GetMapping("/todo")
    public String taskTodo(Model model, HttpSession session) {
        Employee employee = getCurrentEmployee(session);
        List<Task> taskList = taskService.findTaskListByEmployeeId(employee.getId());
        model.addAttribute("taskList",taskList);
        return "task/list-todo";
    }

    /**
     * 添加新的任务
     * @return
     */
    @GetMapping("/new")
    public String newTask(HttpSession session,Model model) {
        Employee employee = getCurrentEmployee(session);
        //当前登录员工的客户列表
        model.addAttribute("customerList",customerService.findMyCustomerList(employee));
        model.addAttribute("saleChanceList",saleChanceService.findMySaleChanceList(employee.getId()));
        return "task/list-new";
    }
    @PostMapping("/new")
    public String newTask(Integer employeeId,String title,String finishTime,String remindTime,
                         /* @RequestParam(required = false,defaultValue = "null")Integer custId,
                          @RequestParam(required = false,defaultValue = "null")Integer saleId,*/
                          RedirectAttributes redirectAttributes) throws ParseException {
        //使用插件从前端获取的时间是String类型，
        // 使用MBG自动生成的实体类中时间是Date类型，因此需要转换成Date类型
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateFinishTime = simpleDateFormat.parse(finishTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date dateRemindTime = dateFormat.parse(remindTime);
        Task task = new Task();
        task.setEmployeeId(employeeId);
        task.setTitle(title);
        task.setFinishTime(dateFinishTime);
        task.setRemindTime(dateRemindTime);
        task.setCreateTime(new Date());
       /* task.setCustId(custId);
        task.setSaleId(saleId);*/
        //保存任务
        taskService.addNewTask(task,remindTime);
        redirectAttributes.addFlashAttribute("message","您已成功添加新的任务");
        return "redirect:/task/todo";
    }


    /**
     * 根据id删除任务
     */
    @GetMapping("/delete/{id:\\d+}")
    public String deleteTask(@PathVariable Integer id,HttpSession session,
                             RedirectAttributes redirectAttributes){
        checkTaskRole(id, session);
        //执行删除操作
        taskService.deleteTaskById(id);
        redirectAttributes.addFlashAttribute("message","您已成功删除任务");
        return "redirect:/task/todo";
    }

    /**
     * 根据id修改已选择任务（就是已经完成）的状态
     * 将未完成状态修改为已完成状态
     * @param id
     * @return
     */
    @GetMapping("/state/done/{id:\\d+}")
    public String updateStateUndoneToDone(@PathVariable Integer id,HttpSession session) {
        Task task = checkTaskRole(id, session);
        taskService.updateStateUndoneToDone(task);
        return "redirect:/task/todo";
    }

    /**
     * 根据id修改已选择任务状态
     * 将已经完成状态修改为未完成状态
     * @param id
     * @return
     */
    @GetMapping("/state/undone/{id:\\d+}")
    public String updateStateDoneToUndone(@PathVariable Integer id,HttpSession session) {
        Task task = checkTaskRole(id, session);
        taskService.updateStateDoneToUndone(task);
        return "redirect:/task/todo";
    }



    /**
     * 判断任务的权限与存在与否
     * @param id
     * @param session
     */
    private Task checkTaskRole(@PathVariable Integer id, HttpSession session) {
        Employee employee = getCurrentEmployee(session);
        Task task = taskService.findTaskById(id);
        //判断该id对应的task是否存在，不存在，则抛出404异常
        if(task == null) {
            throw new NotFoundException();
        }
        //判断该id对应task的employee_id与当前登录员工账户的id是否一样，不一样则无权限删除
        if(!employee.getId().equals(task.getEmployeeId())) {
            throw new ForbiddenException();
        }
        return task;
    }

    /**
     * 逾期事项
     * @return
     */
    @GetMapping("/overdue")
    public String overdueTask(HttpSession session,Model model) {
        Employee employee = getCurrentEmployee(session);
        List<Task> taskList = taskService.findTaskListByEmployeeId(employee.getId());
        //根据task的结束时间与状态查找逾期事项
        List<Task> taskOverdueList = taskService.findOverdueTask(taskList);
        model.addAttribute("taskOverdueList",taskOverdueList);
        return "task/list-overdue";
    }

    /**
     * 根据id删除逾期任务
     */
    @GetMapping("/delete/overdue/{id:\\d+}")
    public String deleteOverdueTask(@PathVariable Integer id,HttpSession session,
                             RedirectAttributes redirectAttributes){
        checkTaskRole(id, session);
        //执行删除操作
        taskService.deleteTaskById(id);
        redirectAttributes.addFlashAttribute("message","您已成功删除逾期任务");
        return "redirect:/task/overdue";
    }





}

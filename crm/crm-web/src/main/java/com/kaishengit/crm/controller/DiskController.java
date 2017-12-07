package com.kaishengit.crm.controller;

import com.kaishengit.crm.entity.Disk;
import com.kaishengit.crm.service.DiskService;
import com.kaishengit.web.result.AjaxResult;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * 公司网盘控制器层
 * Created by zhangyu on 2017/11/16.
 */
@Controller
@RequestMapping("/disk")
public class DiskController {

    @Autowired
    private DiskService diskService;


    @GetMapping
    public String PublicDisk(Model model,
                             @RequestParam(required = false,defaultValue = "0",name = "_")Integer pId) {
        //如果没给定pId的值，默认为0，根据pId获取子文件夹及文件
        List<Disk> diskList = diskService.findDiskByPid(pId);
        //如果给定了pId的值，查找当前pId下的disk对象
        if(pId != 0) {
            Disk disk = diskService.findByPid(pId);
            model.addAttribute("disk",disk);
        }
        model.addAttribute("diskList",diskList);
        return "disk/home";
    }

    /**
     * 新建文件夹
     * 前端页面发送ajax异步请求，需传回json数据
     * @return AjaxResult
     */
    @PostMapping("/new/folder")
    @ResponseBody
    public AjaxResult addNewFolder(Disk disk) {
        //保存新建文件夹
        diskService.addNewFolder(disk);
        //根据pId获取当前路径下最新文件/夹的集合
        List<Disk> diskList = diskService.findDiskByPid(disk.getpId());
        return AjaxResult.successWithData(diskList);
    }

    /**
     * 文件上传
     */
    @PostMapping("/upload")
    @ResponseBody
    public AjaxResult fileUpload(Integer pId, Integer employeeId, MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return AjaxResult.error("请选择要上传的文件");
        }
        //获取文件输入流
        InputStream inputStream = file.getInputStream();
        //获取文件的大小
        long fileSize = file.getSize();
        //获取文件的真正名称
        String fileName = file.getOriginalFilename();

        //进行上传文件
        diskService.uploadNewFile(inputStream,fileSize,fileName,pId,employeeId);
        //获取当前列表最新集合
        List<Disk> diskList = diskService.findDiskByPid(pId);
        return AjaxResult.successWithData(diskList);

    }


    /**
     * 文件下载
     * @param id
     * @param fileName
     * @param response
     */
    @GetMapping("/download")
    public void downloadFile(@RequestParam(required = true,name = "_") Integer id,
                             @RequestParam(required = false,defaultValue = "") String fileName,
                             HttpServletResponse response) {

        try {
            OutputStream outputStream = response.getOutputStream();
            InputStream inputStream = diskService.downloadFile(id);

            //根据fileName是否存在来判断是下载文件还是预览文件
            if(StringUtils.isNotEmpty(fileName)) {
                //下载文件
                //设置MIME type
                response.setContentType("application/octet-stream");
                //设置下载对话框
                fileName = new String(fileName.getBytes("UTF-8"),"ISO8859-1");
                response.addHeader("Content-Disposition","attachment;filename=\""+fileName+"\"");
            }
            IOUtils.copy(inputStream,outputStream);
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }









}

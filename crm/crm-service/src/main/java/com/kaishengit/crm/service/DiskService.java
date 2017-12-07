package com.kaishengit.crm.service;

import com.kaishengit.crm.entity.Disk;

import java.io.InputStream;
import java.util.List;

/**
 * Created by zhangyu on 2017/11/16.
 */

public interface DiskService {

    /**
     * 保存新建文件夹
     * @param disk
     */
    void addNewFolder(Disk disk);

    /**
     * 根据pId获取子文件夹及文件
     * @param pId
     * @return List<Disk>
     */
    List<Disk> findDiskByPid(Integer pId);


    /**
     * 根据ID获取Disk对象
     * @param pId
     * @return
     */
    Disk findByPid(Integer pId);

    /**
     * 文件上传
     * @param inputStream
     * @param fileSize
     * @param fileName
     * @param pId
     * @param employeeId
     */
    void uploadNewFile(InputStream inputStream, long fileSize, String fileName, Integer pId, Integer employeeId);


    /**
     * 文件下载
     * 根据disk的id获取文件的输入流
     * @param id
     * @return
     */
    InputStream downloadFile(Integer id);
}

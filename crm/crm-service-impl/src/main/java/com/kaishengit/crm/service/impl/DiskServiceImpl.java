package com.kaishengit.crm.service.impl;

import com.kaishengit.crm.entity.Disk;
import com.kaishengit.crm.example.DiskExample;
import com.kaishengit.crm.exception.ServiceException;
import com.kaishengit.crm.mapper.DiskMapper;
import com.kaishengit.crm.service.DiskService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by zhangyu on 2017/11/16.
 */
@Service
public class DiskServiceImpl implements DiskService {

    @Autowired
    private DiskMapper diskMapper;

    @Value("${uploadFile.path}")
    private String saveFilePath;

    /**
     * 保存新建文件夹
     * @param disk
     */
    @Override
    public void addNewFolder(Disk disk) {
        disk.setType(Disk.DISK_TYPE_FOLDER);
        disk.setUpdateTime(new Date());
        diskMapper.insertSelective(disk);
    }

    /**
     * 根据pId获取子文件夹及文件
     * @param pId
     * @return List<Disk>
     */
    @Override
    public List<Disk> findDiskByPid(Integer pId) {
        DiskExample diskExample = new DiskExample();
        diskExample.createCriteria().andPIdEqualTo(pId);
        diskExample.setOrderByClause("type asc");
        return diskMapper.selectByExample(diskExample);
    }

    /**
     * 根据ID获取Disk对象
     * @param pId
     * @return
     */
    @Override
    public Disk findByPid(Integer pId) {
        return diskMapper.selectByPrimaryKey(pId);
    }

    /**
     * 文件上传
     * @param inputStream
     * @param fileSize
     * @param fileName
     * @param pId
     * @param employeeId
     */
    @Override
    @Transactional
    public void uploadNewFile(InputStream inputStream, long fileSize, String fileName, Integer pId, Integer employeeId) {
        Disk disk = new Disk();
        disk.setType(Disk.DISK_TYPE_FILE);
        disk.setDownloadCount(0);
        disk.setUpdateTime(new Date());
        disk.setpId(pId);
        disk.setEmployeeId(employeeId);
        disk.setName(fileName);
        //将文件大小转化为可阅读的大小
        disk.setFileSize(FileUtils.byteCountToDisplaySize(fileSize));

        //对上传的文件进行重命名
        String newFileName = UUID.randomUUID() + fileName.substring(fileName.lastIndexOf("."));
        try {
            //本地磁盘
            FileOutputStream outputStream = new FileOutputStream(new File(saveFilePath,newFileName));
            IOUtils.copy(inputStream,outputStream);
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new ServiceException("找不到您要上传的文件夹目录");
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException("上传文件到本地磁盘异常");
        }
        disk.setSaveName(newFileName);
        diskMapper.insertSelective(disk);
    }

    /**
     * 文件下载
     * 根据disk的id获取文件的输入流
     * @param id
     * @return
     */
    @Override
    @Transactional
    public InputStream downloadFile(Integer id) {
        Disk disk = diskMapper.selectByPrimaryKey(id);
        if(disk == null || disk.getType().equals(Disk.DISK_TYPE_FOLDER)) {
            throw new ServiceException("对不起，您要下载的文件不存在或已被删除");
        }
        //更新下载数量
        disk.setDownloadCount(disk.getDownloadCount() + 1);
        diskMapper.updateByPrimaryKeySelective(disk);

        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(new File(saveFilePath,disk.getSaveName()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new ServiceException("下载文件路径异常");
        }
        return inputStream;
    }




}

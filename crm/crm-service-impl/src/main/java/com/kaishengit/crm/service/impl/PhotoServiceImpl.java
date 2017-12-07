package com.kaishengit.crm.service.impl;

import com.google.gson.Gson;
import com.kaishengit.crm.entity.Photo;
import com.kaishengit.crm.example.PhotoExample;
import com.kaishengit.crm.mapper.PhotoMapper;
import com.kaishengit.crm.service.PhotoService;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by zhangyu on 2017/12/6.
 */
@Service
public class PhotoServiceImpl implements PhotoService {

    @Autowired
    private PhotoMapper photoMapper;
    @Value("${qiniu.ak}")
    private String qiniuAk;
    @Value("${qiniu.sk}")
    private String qiniuSK;
    @Value("${qiniu.buket}")
    private String qiniuBuket;

    @Override
    public void savePhoto(Integer employeeId, InputStream inputStream) {

        //上传文件到七牛云

        Configuration configuration = new Configuration(Zone.zone1());
        UploadManager uploadManager = new UploadManager(configuration);
        Auth auth = Auth.create(qiniuAk,qiniuSK);
        String uploadToken = auth.uploadToken(qiniuBuket);
        String key = null;
        try {
            Response response = uploadManager.put(IOUtils.toByteArray(inputStream),null,uploadToken);
            DefaultPutRet defaultPutRet = new Gson().fromJson(response.bodyString(),DefaultPutRet.class);
            key = defaultPutRet.key;
        } catch (IOException e) {
            throw new RuntimeException("上传文件到七牛异常",e);
        }
        Photo photo = new Photo();
        //保存对象
        photo.setEmployeeId(employeeId);
        photo.setAvatar(key);
        photoMapper.insertSelective(photo);
    }

    @Override
    public Photo findByEmployeeId(Integer employeeId) {
        PhotoExample photoExample = new PhotoExample();
        photoExample.createCriteria().andEmployeeIdEqualTo(employeeId);
        List<Photo> photoList = photoMapper.selectByExample(photoExample);
        Photo photo = null;
        if (photoList != null && !photoList.isEmpty()) {
            photo = photoList.get(0);
        }
        return photo;
    }


}

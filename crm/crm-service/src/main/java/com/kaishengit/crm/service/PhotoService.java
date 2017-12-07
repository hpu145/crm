package com.kaishengit.crm.service;

import com.kaishengit.crm.entity.Photo;

import java.io.InputStream;

/**
 * Created by zhangyu on 2017/12/6.
 */
public interface PhotoService {

    void savePhoto(Integer employeeId, InputStream inputStream);

    Photo findByEmployeeId(Integer employeeId);
}

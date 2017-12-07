package com.kaishengit.crm.controller;

import com.kaishengit.crm.entity.Photo;
import com.kaishengit.crm.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by zhangyu on 2017/11/23.
 */
@Controller
public class SettingController {

    @Autowired
    private PhotoService photoService;

    @GetMapping("/settings")
    public String settingUser() {
        return "setting";
    }
    @PostMapping("/settings")
    public String settingUser(Integer employeeId, MultipartFile imige, Model model) {
        if(imige.isEmpty()) {
            photoService.savePhoto(employeeId,null);
        } else {
            try {
                photoService.savePhoto(employeeId,imige.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Photo photo = photoService.findByEmployeeId(employeeId);
        model.addAttribute("photo",photo);
        return "/home";
    }




}

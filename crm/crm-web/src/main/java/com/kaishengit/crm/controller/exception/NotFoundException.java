package com.kaishengit.crm.controller.exception;

import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *404
 * Created by zhangyu on 2017/11/11.
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException{

    public NotFoundException(){}

    public NotFoundException(String message) {
        super(message);
    }



}

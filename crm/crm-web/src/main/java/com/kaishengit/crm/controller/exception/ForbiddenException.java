package com.kaishengit.crm.controller.exception;

import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 403
 * Created by zhangyu on 2017/11/11.
 */
@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException{

    public ForbiddenException() {}

    public ForbiddenException(String message) {
        super(message);
    }
}

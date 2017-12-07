package com.kaishengit.crm.exception;

/**
 * 业务层的异常
 * Created by zhangyu on 2017/11/8.
 */
public class ServiceException extends RuntimeException{

    public ServiceException(){}

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(Throwable th) {
        super(th);
    }

    public ServiceException(String message,Throwable th) {
        super(message,th);
    }



}

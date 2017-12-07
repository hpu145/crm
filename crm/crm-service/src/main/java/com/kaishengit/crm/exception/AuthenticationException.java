package com.kaishengit.crm.exception;

/**
 * Created by zhangyu on 2017/11/7.
 */
public class AuthenticationException extends RuntimeException{

    public AuthenticationException(){}

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(Throwable th) {
        super(th);
    }

    public AuthenticationException(String message,Throwable th) {
        super(message,th);
    }




}

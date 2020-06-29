package com.tsolution.base.exceptionHandle;

import com.tsolution.base.R;

import java.util.HashMap;

public class AppException extends Throwable{
    public Integer code;
    public String message;
    public static HashMap<Integer, Integer> errCode = new HashMap<Integer, Integer>(){{
        put(401, R.string.unAuthorized);
        put(400, R.string.invalidUserOrPass);
        put(404, R.string.notFound);
    }} ;
//    public AppException(Integer code, String message) {
//        this.code = errCode.get(code);
//        this.message = message;
//
//    }
    public AppException(Integer cod, String message) {

        this.code = cod;
        this.message = message;

    }
    public AppException() {
    }
}

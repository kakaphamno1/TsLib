package com.tsolution.base.exceptionHandle;


import java.util.Date;

/**
 * 
 * @author ts-client01
 * Create at 2019-06-27 15:57
 */

public class AndroidCrashLogDto {

    /**
     * 
     */
    public Long id;

    /**
     * 
     */
    public String userName;

    /**
     * 
     */
    public String error;
    public String errorDetail;

    /**
     * 
     */
    public Date createDate;

    public AndroidCrashLogDto(String userName, String error) {

        this.userName = userName;
        this.error = error;

    }
}
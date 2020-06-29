package com.tsolution.base.exceptionHandle;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SOService {
    @POST("crash")
    Call<String> appCrash(@Body AndroidCrashLogDto baseDto);
    @POST("loadConfig")
    Call<HashMap<String, Object>> loadConfig();
}

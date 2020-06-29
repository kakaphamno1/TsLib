package com.tsolution.base;


import androidx.lifecycle.MutableLiveData;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.tsolution.base.exceptionHandle.AppException;
import com.tsolution.base.listener.ResponseResult;

import java.util.Properties;

import lombok.Data;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Data
public class MyCallBackMapper implements Callback<ResponseBody> {
    private ResponseResult func;
    private Class<?> element;
    private JavaType element1;
    private Class type;
    private MutableLiveData<Throwable> appException;
    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()) {
            try {
                ResponseBody responseBody = response.body();
                Class<?> out;
                if (responseBody != null) {
                    String s = responseBody.string();
                    Object value;
                    if(element == null && element1 == null) {

                        value = new Gson().fromJson(s, Properties.class);
                    }else {
                        ObjectMapper objectMapper = new ObjectMapper();
                        if (type == null) {
                            if(element1 != null){
                                value = objectMapper.readValue(s, element1);
                            }else {
                                value = objectMapper.readValue(s, element);
                            }
                        } else {
                            value = objectMapper.readValue(s, objectMapper.getTypeFactory().constructCollectionType(type, element));
                        }
                    }
//                    Object value = objectMapper.
                    //Method m = parent.getClass().getMethod(functionName, List.class);



                    //m.invokeFunc(parent, value);
                    if(func != null) {
                        func.onResponse(call, response, value, null);
                    }


                }
            } catch (Throwable e) {
                if(appException != null){
                    appException.postValue(e);
                }else {
                    if(func != null) {
                        func.onResponse(call, response, null, e);
                    }
                }
            }

        } else {
//                    handleResponseError(serviceName, response.code(), response.body());
            if(appException != null){
                appException.postValue(new AppException(response.code(), response.toString()));
            }else {
                if(func != null) {
                    func.onResponse(call, response, null, new AppException(response.code(), response.toString()));
                }
            }
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        if(appException != null){
            appException.postValue(t);
        }else {
            if(func != null) {
                func.onResponse(call, null, null, t);
            }
        }
    }

    public MyCallBackMapper(MutableLiveData<Throwable> ex, ResponseResult result, Class<?> elem, Class<?> type) {
        this.func = result;
        this.element = elem;
        this.type = type;
        this.appException = ex;
        this.element1 = null;
    }
    public MyCallBackMapper(MutableLiveData<Throwable> ex, ResponseResult result, Class<?> pClass, Class<?> cClass, Class<?> type) {
        this.func = result;
        //this.element1 = elem;
        ObjectMapper objectMapper = new ObjectMapper();
        this.element1 = objectMapper.getTypeFactory().constructParametricType(pClass, cClass);
        this.element = null;
        this.type = type;
        this.appException = ex;
    }
    public MyCallBackMapper(MutableLiveData<Throwable> ex) {
        this.appException = ex;
    }

}

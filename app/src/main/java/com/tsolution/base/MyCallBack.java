package com.tsolution.base;


import androidx.lifecycle.MutableLiveData;

import com.tsolution.base.exceptionHandle.AppException;
import com.tsolution.base.listener.ResponseResult;

import java.io.IOException;

import lombok.Data;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Data
public class MyCallBack<T> implements Callback<T> {
    private ResponseResult func;

    private MutableLiveData<Throwable> appException;

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            try {
                if (func != null) {
                    func.onResponse(call, response, response.body(), null);


                }
            } catch (Throwable e) {
                if (appException != null) {
                    appException.postValue(e);
                } else {
                    if (func != null) {
                        func.onResponse(call, response, null, e);
                    }
                }
            }

        } else {
//                    handleResponseError(serviceName, response.code(), response.body());
            if (appException != null) {
                try {
                    appException.postValue(new AppException(response.code(), response.errorBody().string()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                if (func != null) {
                    func.onResponse(call, response, null, new AppException(response.code(), response.toString()));
                }
            }
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (appException != null) {
            appException.postValue(t);
        } else {
            if (func != null) {
                func.onResponse(call, null, null, t);
            }
        }
    }

    public MyCallBack(MutableLiveData<Throwable> ex, ResponseResult result) {
        this.func = result;

        this.appException = ex;
    }

    public MyCallBack(MutableLiveData<Throwable> ex) {
        this.appException = ex;
    }

}

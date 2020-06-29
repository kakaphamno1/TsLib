package com.tsolution.base;


import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import com.tsolution.base.exceptionHandle.AppException;
import com.tsolution.base.listener.ResponseResult;

import lombok.Data;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Data
public class MyCustomCallBack<T> implements Callback<T> {
    private ResponseResult func;

    @Override
    public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
        if (response.isSuccessful()) {
            try {
                if (func != null) {
                    func.onResponse(call, response, response.body(), null);


                }
            } catch (Throwable e) {
                if (func != null) {
                    func.onResponse(call, response, null, e);
                }
            }

        } else {
            if (func != null) {
                func.onResponse(call, response, null, new AppException(response.code(), response.toString()));
            }
        }
    }

    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
        if (func != null) {
            func.onResponse(call, null, null, t);
        }
    }

    public MyCustomCallBack(ResponseResult result) {
        this.func = result;

    }
}

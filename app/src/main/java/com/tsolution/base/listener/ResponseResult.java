package com.tsolution.base.listener;

import retrofit2.Call;
import retrofit2.Response;

public interface ResponseResult<ResponseBody> extends BaseListener{

    void onResponse(Call<ResponseBody> call, Response<ResponseBody> response, Object body, Throwable t);
}

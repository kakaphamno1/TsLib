package com.tsolution.base.exceptionHandle;

import androidx.lifecycle.MutableLiveData;

import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;

import io.reactivex.observers.DisposableObserver;
import lombok.Getter;
import lombok.Setter;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

@Getter
@Setter
public abstract class CallbackWrapper<T> extends DisposableObserver<T> {
    private final MutableLiveData<String> responseLV = new MutableLiveData<>();

    public CallbackWrapper() {
    }

    protected abstract void onSuccess(T t);

    @Override
    public void onNext(T t) {
        //You can return StatusCodes of different cases from your API and handle it here. I usually include these cases on BaseResponse and iherit it from every Response
        onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof HttpException) {
            ResponseBody responseBody = ((HttpException) e).response().errorBody();
            responseLV.postValue(getErrorMessage(responseBody));
            System.out.println("onUnknownError: " + getErrorMessage(responseBody));
        } else if (e instanceof SocketTimeoutException) {
            System.out.println("TimeOut: ");
            responseLV.postValue("TimeOut");
        } else if (e instanceof IOException) {
            System.out.println("onNetworkError: ");
            responseLV.postValue("onNetworkError");
        } else {
            responseLV.postValue("onUnknownError");
            System.out.println("onUnknownErrorTryCatch: " + e.getMessage());
        }
    }

    @Override
    public void onComplete() {

    }

    private String getErrorMessage(ResponseBody responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody.string());
            return jsonObject.getString("message");
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
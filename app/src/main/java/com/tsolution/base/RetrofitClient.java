package com.tsolution.base;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tsolution.base.listener.ResponseResult;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;
    public static String clientId;
    public static String clientSecret;
    public static String BASE_URL_OAUTH;
    public static String BASE_URL;
    public static String DATE_FORMAT;
    public static String TOKEN = "";
    public static String USER_NAME;
    public static String language = "vi";

    public static void reset() {
        retrofit = null;
    }

    public static void changeLanguage(String lang) {
        retrofit = null;
        language = lang;
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            init(TOKEN);
        }
        return retrofit;
    }


    public static Retrofit getAuthClient() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request.Builder newRequest = request.newBuilder().addHeader("Authorization", TOKEN)
                                .addHeader("Content-Type", "application/json")
                                .addHeader("Accept-Language", language);
                        return chain.proceed(newRequest.build());
                    }
                });
        //Add the interceptor to the client builder.
        GsonBuilder gsonBuilder = new GsonBuilder();
        //gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
        gsonBuilder.setDateFormat(DATE_FORMAT);
        Retrofit ret = new Retrofit.Builder()
                .client(okHttpClientBuilder.build())
                .baseUrl(BASE_URL_OAUTH)
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))

                .build();

        return ret;
    }


    public static Retrofit init(final String accessToken) {

        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    Request.Builder newRequest = request.newBuilder().addHeader("Authorization", accessToken)
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Accept-Language", language);

                    return chain.proceed(newRequest.build());
                });
        //Add the interceptor to the client builder.
        GsonBuilder gsonBuilder = new GsonBuilder();
        //gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
        gsonBuilder.setDateFormat(DATE_FORMAT);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                .build();
        return retrofit;
    }


    public static void requestLogin(MutableLiveData<Throwable> appException, String userName, String password, ResponseResult result) {
        String x = Credentials.basic(RetrofitClient.clientId, RetrofitClient.clientSecret);

        FormBody.Builder builder = new FormBody.Builder();
        builder.add("grant_type", "password");
        builder.add("username", userName);
        builder.add("password", password);

        final Request request = new Request.Builder()
                .url(RetrofitClient.BASE_URL_OAUTH + "/oauth/token").addHeader("origin", "abc")
                .addHeader("Authorization", x)
                .addHeader("Accept", "application/json, text/plain, */*")
                .addHeader("Content-type", "application/x-www-form-urlencoded")
                .addHeader("Accept-Language", language)
                .post(builder.build())
                .build();

        OkHttpClient.Builder builder1 = new OkHttpClient.Builder();

        builder1.build().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                appException.postValue(e);
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    String token = response.body().string();
                    Type type = new TypeToken<Map<String, String>>() {
                    }.getType();
                    Gson gson = new Gson();
                    Map<String, String> myMap = gson.fromJson(token, type);
                    TOKEN = myMap.get("token_type") + " " + myMap.get("access_token");
                    USER_NAME = userName;
                    RetrofitClient.init(TOKEN);
                    result.onResponse(null, null, token, null);
                } else {
                    result.onResponse(null, null, null, null);
                }

            }
        });
    }

}

package com.dicarodev.dicarobank.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DicaroBankApiAdapter {

    private static ApiService apiService;

    public static ApiService getApiService(){

        final String BASE_URL = "http://10.0.2.2:9001/"; // 10.0.2.2 en el emulador hace referencia a localhost

        HttpLoggingInterceptor loginInterceptor = new HttpLoggingInterceptor();
        loginInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(loginInterceptor);

        if (apiService == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();

            apiService = retrofit.create(ApiService.class);
        }

        return apiService;
    }

}

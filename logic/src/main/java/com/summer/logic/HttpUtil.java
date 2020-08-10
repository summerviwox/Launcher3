package com.summer.logic;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpUtil  {

    private static HttpUtil instance;

    private static LauncherService service;

    public static LauncherService getInstance(){
        if(instance==null){
            instance = new HttpUtil();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://222.186.36.75:9999/record-b/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            service = retrofit.create(LauncherService.class);
        }
        return service;
    }

}

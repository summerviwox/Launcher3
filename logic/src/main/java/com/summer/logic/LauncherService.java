package com.summer.logic;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface LauncherService {

    @GET("windows/shutDownWindows")
    Call<Object> shutDownWindows();

    @GET("windows/cancleShutDown")
    Call<Object> cancleShutDown();

    @GET("alarm/selectAll")
    Call<List<Alarm>> selectAll();
}

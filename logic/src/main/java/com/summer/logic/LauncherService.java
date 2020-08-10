package com.summer.logic;

import retrofit2.Call;
import retrofit2.http.GET;

public interface LauncherService {

    @GET("windows/shutDownWindows")
    Call<Object> shutDownWindows();

    @GET("windows/cancleShutDown")
    Call<Object> cancleShutDown();
}

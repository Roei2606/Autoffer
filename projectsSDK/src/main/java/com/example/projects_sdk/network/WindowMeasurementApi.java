package com.example.projects_sdk.network;

import com.example.projects_sdk.models.MeasurementCamera;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface WindowMeasurementApi {

    @Multipart
    @POST("measure")
    Call<MeasurementCamera> measureWindow(@Part MultipartBody.Part file);

}

package com.example.triolingo_mobile.API;
import com.example.triolingo_mobile.Model.Course;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APICourse {
    @GET("/api/Course/get")
    Call<List<Course>> getList();
}

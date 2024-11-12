package com.example.agencedevoyage.Entity;
// ApiService.java
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @POST("log_activity")
    Call<Void> logUserActivity(@Body UserActivity activity);

    @GET("activities")
    Call<List<UserActivity>> getAllActivities();

    @GET("analytics")
    Call<Map<String, Object>> getAnalyticsSummary();}
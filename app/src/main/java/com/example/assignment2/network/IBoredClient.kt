package com.example.assignment2.network

import com.example.assignment2.data.BoredActivity
import retrofit2.http.GET
import retrofit2.http.Query

interface IBoredClient {
    @GET("/api/activity")
    suspend fun getRandom(@Query("type") type: String?): BoredActivity
}
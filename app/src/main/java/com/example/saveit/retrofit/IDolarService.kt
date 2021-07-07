package com.example.saveit.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IDolarService {
    @GET("/api/v7/convert")
    fun getDolarValue(@Query("q") q: String, @Query("compact") compact: String, @Query("apiKey") apiKey: String): Call<Respuesta>
}
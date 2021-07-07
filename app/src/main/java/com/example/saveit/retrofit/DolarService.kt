package com.example.saveit.retrofit

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DolarService : IDolarService {
    override fun getDolarValue(q: String, compact: String, apiKey: String): Call<Respuesta> {
        return getService().create(IDolarService::class.java).getDolarValue(q, compact, apiKey)
    }

    private fun getService(): Retrofit {
        val service = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://free.currconv.com")
            .build()

        return service
    }
}
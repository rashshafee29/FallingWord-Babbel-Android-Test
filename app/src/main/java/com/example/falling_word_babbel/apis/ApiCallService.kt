package com.example.falling_word_babbel.apis

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiCallService(baseUrl: String) {
    private var gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    fun<T> buildService(service: Class<T>): T{
        return retrofit.create(service)
    }
}

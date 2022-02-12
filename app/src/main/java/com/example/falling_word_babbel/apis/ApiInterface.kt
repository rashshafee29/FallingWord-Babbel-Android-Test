package com.example.falling_word_babbel.apis

import com.example.falling_word_babbel.model.WordItemModel
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    @GET("words.json")
    fun getWords(): Call<ArrayList<WordItemModel>>
}
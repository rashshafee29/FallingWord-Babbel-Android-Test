package com.example.falling_word_babbel.repositories

import android.content.Context
import android.telecom.Call
import android.util.Log
import android.widget.Toast
import com.example.falling_word_babbel.apis.ApiInterface

import com.example.falling_word_babbel.apis.ApiCallService

import androidx.lifecycle.MutableLiveData
import com.example.falling_word_babbel.model.WordItemModel
import com.example.falling_word_babbel.utils.Constant
import retrofit2.Callback
import retrofit2.Response
import androidx.lifecycle.LiveData

class WordRepository {
    private var mApiCallService: ApiCallService = ApiCallService(Constant.DATA_URL)
    private var mWordListLiveData: MutableLiveData<ArrayList<WordItemModel>> = MutableLiveData<ArrayList<WordItemModel>>()

    fun fetchData() {
        val apiInterface = mApiCallService.buildService(ApiInterface::class.java)
        val dataCall = apiInterface.getWords()
        dataCall.enqueue(object : Callback<ArrayList<WordItemModel>> {
            override fun onResponse(
                call: retrofit2.Call<ArrayList<WordItemModel>>,
                response: Response<ArrayList<WordItemModel>>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        mWordListLiveData.postValue(response.body())
                    }
                } else {
                    Log.e("RashidShafee", "Not Successful" + response.code())
                }
            }

            override fun onFailure(call: retrofit2.Call<ArrayList<WordItemModel>>, t: Throwable) {
                Log.e("RashidShafee", "Error" + t)
            }

        })
    }

    fun getWordListLiveData(): LiveData<ArrayList<WordItemModel>> {
        return mWordListLiveData
    }
}
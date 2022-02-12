package com.example.falling_word_babbel.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.falling_word_babbel.model.WordItemModel
import com.example.falling_word_babbel.repositories.WordRepository

class WordViewModel : ViewModel() {
    private var mWordRepository: WordRepository = WordRepository()
    private var mWordListLiveData: LiveData<ArrayList<WordItemModel>> = mWordRepository.getWordListLiveData()

    fun fetchData() {
        mWordRepository.fetchData()
    }

    fun getWordListData(): LiveData<ArrayList<WordItemModel>> {
        return mWordListLiveData
    }
}
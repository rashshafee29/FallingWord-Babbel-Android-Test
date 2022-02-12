package com.example.falling_word_babbel.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.example.falling_word_babbel.model.QuestionAnswerModel
import com.example.falling_word_babbel.model.WordItemModel
import java.util.ArrayList

class GameViewModel(private val lifecycleOwner: LifecycleOwner, private val mIsEngToSpa: Boolean): ViewModel() {
    private var gameScore = 0
    private var gameLives = 5
    private var currentQuestionAnswerCount = 0
    private val questionAnswerList = ArrayList<QuestionAnswerModel>()
    val questionAnswerLiveData = MutableLiveData<QuestionAnswerModel>()
    val isGameOverLiveData = MutableLiveData<Boolean>()
    val gameScoreLiveData = MutableLiveData<Int>(gameScore)
    val gameLivesLiveData = MutableLiveData<Int>(gameLives)
    private lateinit var currentQuestionAnswer: QuestionAnswerModel

    init {
        loadData()
    }

    private fun loadData() {
        val wordViewModel = WordViewModel()
        wordViewModel.getWordListData().observe(lifecycleOwner, Observer {
            createQuestionAnswerForGame(it)
            playNextWord()
        })
        wordViewModel.fetchData()
    }

    fun playNextWord() {
        if (checkIfGameEnd()) {
            return
        }
        currentQuestionAnswer = questionAnswerList[currentQuestionAnswerCount]
        currentQuestionAnswerCount++
        questionAnswerLiveData.value = currentQuestionAnswer
    }

    private fun createQuestionAnswerForGame(wordList: ArrayList<WordItemModel>?) {
        wordList!!.shuffle()
        for(word in wordList!!) {
            val translationList: ArrayList<String> = findFiveRandomTranslationWithAnswer(word, wordList)
            translationList.shuffle()
            var question: String = ""
            var answer: String = ""
            if (mIsEngToSpa) {
                question = word.text_eng
                answer = word.text_spa
            } else {
                question = word.text_spa
                answer = word.text_eng
            }
            val questionAnswerModel = QuestionAnswerModel(question, answer, translationList)
            questionAnswerList.add(questionAnswerModel)
        }
    }

    fun noAnswer() {
        gameLives--
        gameLivesLiveData.value = gameLives
        gameScore -= 100
        gameScoreLiveData.value = gameScore
        checkIfGameEnd()
    }

    private fun findFiveRandomTranslationWithAnswer(
        word: WordItemModel,
        wordList: ArrayList<WordItemModel>
    ): ArrayList<String> {
        val translationList = ArrayList<String>()
        if(mIsEngToSpa) {
            translationList.add(word.text_spa)
        } else {
            translationList.add(word.text_eng)
        }
        val randomWords = wordList.asSequence().shuffled().take(4).filter { it != word }.toList()
        for(randomWord in randomWords) {
            if(mIsEngToSpa) {
                translationList.add(randomWord.text_spa)
            } else {
                translationList.add(randomWord.text_eng)
            }
        }
        return translationList
    }


    fun correctSelected(
        mCurrentQuestionAnswerModel: QuestionAnswerModel,
        mCurrentFallingText: String
    ) {
        if(mCurrentQuestionAnswerModel.answer == mCurrentFallingText) {
            gameScore += 100
            gameScoreLiveData.value = gameScore
        } else {
            gameScore -= 50
            gameScoreLiveData.value = gameScore
            gameLives--
            gameLivesLiveData.value = gameLives
        }
        checkIfGameEnd()
    }

    fun wrongSelected(
        mCurrentQuestionAnswerModel: QuestionAnswerModel,
        mCurrentFallingText: String
    ) {
        if(mCurrentQuestionAnswerModel.answer != mCurrentFallingText) {
            gameScore += 100
            gameScoreLiveData.value = gameScore
        } else {
            gameScore -= 50
            gameScoreLiveData.value = gameScore
            gameLives--
            gameLivesLiveData.value = gameLives
        }
        checkIfGameEnd()
    }

    private fun checkIfGameEnd(): Boolean{
        return if(currentQuestionAnswerCount >= questionAnswerList.size || gameLives <=0) {
            isGameOverLiveData.value = true
            gameScoreLiveData.value = gameScore
            true
        } else {
            false
        }
    }

}
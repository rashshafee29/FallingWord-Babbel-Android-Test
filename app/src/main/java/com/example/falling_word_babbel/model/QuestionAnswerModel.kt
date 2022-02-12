package com.example.falling_word_babbel.model

data class QuestionAnswerModel(
    val question: String,
    val answer: String,
    val randomAnswer: ArrayList<String>
)
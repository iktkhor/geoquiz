package com.example.geoquiz

import androidx.lifecycle.ViewModel
import android.util.Log

private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel() {

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true))

    private var is_answered = Array(questionBank.size, {false})
    private var is_cheated = Array(questionBank.size, {false})

    var currentIndex = 0
    var isCheater = false

    private var correctAnswers = 0
    private var wrongAnswers = 0

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrev() {
        currentIndex = if (currentIndex > 0) {
            (currentIndex - 1) % questionBank.size
        } else {
            questionBank.size - 1
        }
    }

    fun getResult(): Float {
        return (correctAnswers.toFloat() / questionBank.size * 100)
    }

    fun addCorrectAnswer() {
        correctAnswers++
    }

    fun addWrongAnswer() {
        wrongAnswers++
    }

    fun flagAsAnswered() {
        is_answered[currentIndex] = true
    }

    fun flagAsCheated() {
        is_cheated[currentIndex] = true
    }

    fun checkIsAnswered(): Boolean {
        return is_answered[currentIndex]
    }

    fun checkIsCheated(): Boolean {
        return is_cheated[currentIndex]
    }

    fun checkIsCompleted(): Boolean {
        return (correctAnswers + wrongAnswers) == questionBank.size
    }

    init {
        Log.d(TAG, "ViewModel instance created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "ViewModel instance about to be destroyed")
    }
}
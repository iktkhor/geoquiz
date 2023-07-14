package com.example.geoquiz

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "CheatViewModel"

class CheatViewModel : ViewModel() {

    var answerIsTrue = false

    init {
        Log.d(TAG, "CheatModel instance created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "CheatModel instance to be destroyed")
    }
}
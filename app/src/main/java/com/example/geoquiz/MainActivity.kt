package com.example.geoquiz

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"

class MainActivity : AppCompatActivity() {

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this)[QuizViewModel::class.java]
    }

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var cheatButton: Button
    private lateinit var questionTextView: TextView

     private val cheatActivityLauncher = registerForActivityResult(
         ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->

             if (result.resultCode == RESULT_OK) {
                 val isCheated = result.data?.getBooleanExtra("answer_shown", false)
                 Log.d(TAG, "$isCheated")
                 if (isCheated != null && isCheated) {
                     quizViewModel.flagAsCheated()
                 }
             }
     }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        quizViewModel.currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?:0
        
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        cheatButton = findViewById(R.id.cheat_button)
        questionTextView = findViewById(R.id.question_text_view)

        trueButton.setOnClickListener {
            checkAnswer(true)
            quizViewModel.flagAsAnswered()
            updateQuestion()
        }

        falseButton.setOnClickListener {
            checkAnswer(false)
            quizViewModel.flagAsAnswered()
            updateQuestion()
        }

        nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }

        prevButton.setOnClickListener {
            quizViewModel.moveToPrev()
            updateQuestion()
        }

        cheatButton.setOnClickListener {
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            cheatActivityLauncher.launch(intent)
        }

        questionTextView.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }

        updateQuestion()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_INDEX, quizViewModel.currentIndex)

    }

    private fun updateQuestion() {
        questionTextView.setText(quizViewModel.currentQuestionText)

        if (!quizViewModel.checkIsAnswered()) {
            enableButtons(true)
        } else {
            enableButtons(false)
        }
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer

        val messageResId = if (quizViewModel.checkIsCheated()) {
            quizViewModel.addWrongAnswer()
            R.string.judgment_toast
        } else if (userAnswer == correctAnswer) {
            quizViewModel.addCorrectAnswer()
            R.string.correct_toast
        } else {
            quizViewModel.addWrongAnswer()
            R.string.incorrect_toast
        }

        val toast = Toast.makeText(applicationContext, messageResId, Toast.LENGTH_SHORT)
        toast.show()

        if (quizViewModel.checkIsCompleted()) {
            val messageText = "Вы набрали " + quizViewModel.getResult().toInt() + "% правильных ответов"
            val resultToast = Toast.makeText(applicationContext, messageText, Toast.LENGTH_LONG)
            resultToast.show()
        }
    }

    private fun enableButtons(bool: Boolean) {
        falseButton.isEnabled = bool
        trueButton.isEnabled = bool
    }
}
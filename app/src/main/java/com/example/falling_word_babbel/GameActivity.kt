package com.example.falling_word_babbel

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import com.example.falling_word_babbel.model.QuestionAnswerModel
import com.example.falling_word_babbel.utils.Constant
import com.example.falling_word_babbel.viewmodels.GameViewModel
import com.github.ybq.android.spinkit.SpinKitView

class GameActivity : AppCompatActivity() {
    private lateinit var gameViewModel: GameViewModel
    private lateinit var mCorrectBtn: Button
    private lateinit var mWrongBtn: Button
    private lateinit var mQuestionText: TextView
    private lateinit var mScoreText: TextView
    private lateinit var mLivesText: TextView
    private lateinit var mCurrentQuestionAnswerModel: QuestionAnswerModel
    private lateinit var mCurrentFallingText: String
    private lateinit var mAnsText: TextView
    private var mIsGameOver: Boolean = false
    private var mCurrentPossibleCount: Int = 0
    private var mFinalScore: Int = 0
    private lateinit var mLoadingAnimation: SpinKitView
    private lateinit var mContainer: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        mLoadingAnimation = findViewById(R.id.id_loading_anim)
        mContainer = findViewById(R.id.id_container)
        mLoadingAnimation.visibility = View.VISIBLE
        mCorrectBtn = findViewById(R.id.id_correct_btn)
        mWrongBtn = findViewById(R.id.id_wrong_btn)
        mQuestionText = findViewById(R.id.id_current_question)
        mScoreText = findViewById(R.id.id_score_text)
        mLivesText = findViewById(R.id.id_lives_cnt)
        mCurrentQuestionAnswerModel = QuestionAnswerModel("", "", ArrayList<String>())
        mCurrentFallingText = ""
        mCurrentPossibleCount = 0
        mAnsText = findViewById(R.id.id_ans_text)
        val isEngToSpa: Boolean = intent.extras!![Constant.EXTRA_KEY] as Boolean
        initViewModel(isEngToSpa)
        mCorrectBtn.setOnClickListener {
            if (!mIsGameOver) {
                gameViewModel.correctSelected(mCurrentQuestionAnswerModel, mCurrentFallingText)
                playGame()
            }
        }

        mWrongBtn.setOnClickListener {
            if(!mIsGameOver) {
                gameViewModel.wrongSelected(mCurrentQuestionAnswerModel, mCurrentFallingText)
                playGame()
            }
        }
    }

    private fun initViewModel(isEngToSpa: Boolean) {
        gameViewModel = GameViewModel(this, isEngToSpa)

        gameViewModel.gameLivesLiveData.observe(this, Observer {
            mLivesText.text = it.toString()
        })

        gameViewModel.gameScoreLiveData.observe(this, Observer {
            mScoreText.text = "Score: $it"
            mFinalScore = it
        })

        gameViewModel.isGameOverLiveData.observe(this, Observer {
            if(it) {
                mIsGameOver = true
                clearAnimation()
                showGameOverDialog()
            }
        })

        gameViewModel.questionAnswerLiveData.observe(this, Observer {
            mLoadingAnimation.visibility = View.GONE
            mContainer.visibility = View.VISIBLE
            mCurrentQuestionAnswerModel = it
            mQuestionText.text = it.question
            playGame()
        })
    }

    private fun showGameOverDialog() {
        val dialog = Dialog(this)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_dialog)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()

        val scoreText = dialog.findViewById<TextView>(R.id.id_dialog_score)
        scoreText.text = mFinalScore.toString()
        val restartBtn = dialog.findViewById<Button>(R.id.id_dialog_btn_restart)
        restartBtn.setOnClickListener {
            finish()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            dialog.dismiss()
        }
    }

    private fun playGame() {
        if (!mIsGameOver) {
            if (mAnsText.animation != null) {
                clearAnimation()
            }
            if(mCurrentPossibleCount >= mCurrentQuestionAnswerModel.randomAnswer.size) {
                mCurrentPossibleCount = 0
                gameViewModel.playNextWord()
            } else {
                mCurrentFallingText = mCurrentQuestionAnswerModel.randomAnswer[mCurrentPossibleCount]
                mCurrentPossibleCount++
                startAnimation()
            }
        }
    }

    private fun startAnimation() {
        val animation: Animation = AnimationUtils.loadAnimation(this, R.anim.top_to_bottom)
        animation.setAnimationListener(object: Animation.AnimationListener{
            override fun onAnimationRepeat(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                gameViewModel.noAnswer()
                playGame()
            }

            override fun onAnimationStart(p0: Animation?) {
            }
        })
        mAnsText.text = mCurrentFallingText
        mAnsText.visibility = View.VISIBLE
        mAnsText.startAnimation(animation)
    }

    private fun clearAnimation() {
        mAnsText.visibility = View.INVISIBLE
        if(mAnsText.animation != null) {
            mAnsText.animation.setAnimationListener(null)
        }
        mAnsText.clearAnimation()
    }
}
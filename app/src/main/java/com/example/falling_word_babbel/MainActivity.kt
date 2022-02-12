package com.example.falling_word_babbel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.falling_word_babbel.model.QuestionAnswerModel
import com.example.falling_word_babbel.model.WordItemModel
import com.example.falling_word_babbel.utils.Constant
import com.example.falling_word_babbel.viewmodels.WordViewModel
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val startGameBtn = findViewById<Button>(R.id.id_start_btn)
        val switchBtn = findViewById<SwitchCompat>(R.id.id_switch)
        val modeText = findViewById<TextView>(R.id.id_mode)
        val tutorialText = findViewById<TextView>(R.id.id_tutorial_text)

        switchBtn.setOnCheckedChangeListener { compoundButton, b ->
            if(compoundButton.isChecked) {
                modeText.text = "SPANISH TO ENGLISH"
            } else {
                modeText.text = "ENGLISH TO SPANISH"
            }
        }

        startGameBtn.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java).apply{
                putExtra(Constant.EXTRA_KEY, !switchBtn.isChecked)
            }
            startActivity(intent)
        }
    }
}
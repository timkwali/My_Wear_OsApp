package com.example.mywearosapp.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.support.wearable.activity.WearableActivity
import android.util.Log
import android.widget.Toast
import androidx.constraintlayout.solver.widgets.Helper
import androidx.wear.activity.ConfirmationActivity
import com.example.mywearosapp.MainActivity
import com.example.mywearosapp.R
import com.example.mywearosapp.data.Note
import com.example.mywearosapp.databinding.ActivityMainBinding
import com.example.mywearosapp.databinding.ActivityNewNoteBinding
import com.example.mywearosapp.utils.NotesUtils
import com.google.android.material.snackbar.Snackbar
import java.util.*

class NewNoteActivity : WearableActivity() {

    private var binding: ActivityNewNoteBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewNoteBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        /** RECORD NOTE */
        displaySpeechScreen()
        binding!!.recordButton.setOnClickListener {
            displaySpeechScreen()
        }

        /** SAVE A NEW NOTE */
        binding!!.saveButton.setOnClickListener {
            val tv = binding!!.noteText.text.toString()
            if(tv.isEmpty()) {
                NotesUtils().displayCancel("Please Add A Note Before Proceeding.", this)
            } else {
                /** SAVE NOTE */
                NotesUtils().saveNote(tv, this)
                NotesUtils().displayConfirmation("Note Saved", this)

                Handler().postDelayed({
                    startActivity(
                        Intent(this, MainActivity::class.java)
                    )
                }, 1500)
            }
        }

        // Enables Always-on
        setAmbientEnabled()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1001 && resultCode == RESULT_OK) {
            /** SHOW RECORDED MESSAGE IN TEXT-VIEW */
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            binding!!.noteText.text = result?.get(0)
        }

    }



    fun displaySpeechScreen() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to add note!")
        startActivityForResult(intent, 1001)
    }


}
package com.example.mywearosapp.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.support.wearable.activity.WearableActivity
import android.view.View
import android.widget.Toast
import androidx.wear.widget.CircularProgressLayout
import com.example.mywearosapp.MainActivity
import com.example.mywearosapp.databinding.ActivityNoteDetailsBinding
import com.example.mywearosapp.utils.*
import com.google.android.material.snackbar.Snackbar
import java.util.*

class NoteDetailsActivity : WearableActivity(),
    CircularProgressLayout.OnTimerFinishedListener,
    View.OnClickListener {

    private var binding: ActivityNoteDetailsBinding? = null
    private lateinit var id: String
    private lateinit var circularProgress: CircularProgressLayout
    private lateinit var tts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteDetailsBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        // Enables Always-on
        setAmbientEnabled()
        /** INITIALISE TTS */
        initialiseTTS()


        id = intent.getStringExtra("id")
        val title = intent.getStringExtra("title")
        val position = intent.getStringExtra("position")

        binding!!.noteTitle.text = title
        binding!!.noteNumber.text = position

        circularProgress = binding!!.circularProgress.apply {
            onTimerFinishedListener = this@NoteDetailsActivity
            setOnClickListener(this@NoteDetailsActivity)
        }

        binding!!.deleteButton.setOnClickListener {
            /** TOGGLE VIEWS VISIBILITY */
            removeView(binding!!.deleteButton)
            removeView(binding!!.noteNumber)
            removeView(binding!!.view)
            removeView(binding!!.noteTitle)
            removeView(binding!!.speakButton)
            showView(binding!!.deletingText)
            showView(binding!!.circularProgress)

            /** START DELETE TIMER */
            circularProgress?.apply {
                totalTime = 2000
                startTimer()
            }
        }

        /** SPEAK CONTENT OF NOTE */
        binding!!.speakButton.setOnClickListener{
            speak()
        }

    }

    override fun onTimerFinished(layout: CircularProgressLayout?) {
        /** TOGGLE VIEWS VISIBILITY */
        removeView(binding!!.deletingText)
        removeView(binding!!.circularProgress)
        showView(binding!!.deleteButton)
        showView(binding!!.noteNumber)
        showView(binding!!.view)
        showView(binding!!.noteTitle)
        showView(binding!!.speakButton)


        /** DELETE NOTE WITH ID */
        NotesUtils().deleteNote(id, this)
//        Toast.makeText(this, "Note Successfully Deleted", Toast.LENGTH_SHORT).show()
        NotesUtils().displayConfirmation("Note Deleted", this)

        Handler().postDelayed( {
            startActivity(
                Intent(this, MainActivity::class.java)
            )
        }, 1500)
    }

    override fun onClick(p0: View?) {
        /** STOP DELETE TIMER */
        circularProgress?.stopTimer()

        /** TOGGLE VIEWS VISIBILITY */
        removeView(binding!!.deletingText)
        removeView(binding!!.circularProgress)
        showView(binding!!.deleteButton)
        showView(binding!!.noteNumber)
        showView(binding!!.view)
        showView(binding!!.noteTitle)
        showView(binding!!.speakButton)

        NotesUtils().displayCancel("Delete Canceled", this)
    }

    override fun onDestroy() {
        super.onDestroy()
        if(tts != null) {
            tts.stop()
            tts.shutdown()
        }
    }

    fun initialiseTTS() {
        tts = TextToSpeech(this, TextToSpeech.OnInitListener { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts.setLanguage(Locale.ENGLISH)
                if (result == TextToSpeech.LANG_MISSING_DATA
                        || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(this, "Language Not Supported!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Initialization Failed!", Toast.LENGTH_SHORT).show()

            }
        })
    }

    fun speak() {
        val text = binding!!.noteTitle.text.toString()
        val pitch: Float = 1.0F
        val speed: Float = 1.0F
        tts.setPitch(pitch)
        tts.setSpeechRate(speed)
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null)
    }
}
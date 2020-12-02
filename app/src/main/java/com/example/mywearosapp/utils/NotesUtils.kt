package com.example.mywearosapp.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.support.wearable.activity.ConfirmationActivity
import android.util.Log
import android.widget.Toast
import com.example.mywearosapp.data.Note

class NotesUtils() {
    fun saveNote(message: String, context: Context) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("Notes", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val id = System.currentTimeMillis().toString()

        editor.putString(id, message)
        editor.apply()

        Log.d("notesp", sharedPreferences.all.toString())
    }

    fun getAllNotes(context: Context): MutableList<Note> {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("Notes", MODE_PRIVATE)
        val allNotes = mutableListOf<Note>()
        val notes = sharedPreferences.all

        return if(notes.isEmpty()){
            allNotes
        } else {
            for(note in notes){
                val note = Note(note.key, note.value.toString())
                allNotes.add(note)
            }
            allNotes
        }
    }

    fun deleteNote(id: String, context: Context) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("Notes", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(id)
            .apply()
    }

    fun displayConfirmation(message: String, context: Context) {
        val intent = Intent(context, ConfirmationActivity::class.java).apply {
            putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, ConfirmationActivity.SUCCESS_ANIMATION)
            putExtra(ConfirmationActivity.EXTRA_MESSAGE, message)
        }
        context.startActivity(intent)
    }

    fun displayCancel(message: String, context: Context) {
        val intent = Intent(context, ConfirmationActivity::class.java).apply {
            putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, ConfirmationActivity.FAILURE_ANIMATION)
            putExtra(ConfirmationActivity.EXTRA_MESSAGE, message)
        }
        context.startActivity(intent)
    }
}
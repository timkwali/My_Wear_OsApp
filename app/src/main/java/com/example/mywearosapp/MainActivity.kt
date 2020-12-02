package com.example.mywearosapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.wearable.activity.WearableActivity
import android.view.View
import android.widget.Toast
import androidx.wear.widget.WearableLinearLayoutManager
import com.example.mywearosapp.adapters.NotesRVAdapter
import com.example.mywearosapp.adapters.OnItemClickListener
import com.example.mywearosapp.data.Note
import com.example.mywearosapp.databinding.ActivityMainBinding
import com.example.mywearosapp.ui.NewNoteActivity
import com.example.mywearosapp.ui.NoteDetailsActivity
import com.example.mywearosapp.utils.NotesUtils

class MainActivity : WearableActivity(), OnItemClickListener {

    private var binding: ActivityMainBinding? = null
    private lateinit var adapter: NotesRVAdapter
//    private val allNotes = mutableListOf<Note>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        val allNotes = NotesUtils().getAllNotes(this)

        /** SHOW NOTES IN RECYCLER VIEW */
        if(allNotes.isEmpty()) {
            binding!!.recordNewNote.visibility = View.VISIBLE
        } else {
            adapter = NotesRVAdapter(allNotes, this)
            binding!!.recyclerView.layoutManager = WearableLinearLayoutManager(this)
            binding!!.recyclerView.isCircularScrollingGestureEnabled = true
            binding!!.recyclerView.isEdgeItemsCenteringEnabled = true
            binding!!.recyclerView.adapter = adapter
        }

        binding!!.addButton.setOnClickListener {
            val intent = Intent(this, NewNoteActivity::class.java)
            startActivity(intent)
        }

        // Enables Always-on
        setAmbientEnabled()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

//    val notesList = listOf<Note>(
//        Note("1jk", "Buy Sugar"),
//        Note("2sdf", "Buy Lemons"),
//        Note("3sfs", "Buy Bread"),
//        Note("4sf", "Buy Tea"),
//        Note("5sf", "Buy Milk"),
//        Note("6sf", "Buy Pillow"),
//        Note("7sf", "Buy Paste"),
//        Note("8sf", "Buy Spray"),
//        Note("9sf", "Buy Fruits"),
//    ).toMutableList()

    override fun onItemClick(note: Note, position: Int) {
        val intent = Intent(this, NoteDetailsActivity()::class.java)
        intent.putExtra("id", note.id)
        intent.putExtra("title", note.title)
        intent.putExtra("position", (position + 1).toString())
        startActivity(intent)
    }

    override fun onExitAmbient() {
        super.onExitAmbient()
    }

    override fun onEnterAmbient(ambientDetails: Bundle?) {
        super.onEnterAmbient(ambientDetails)
    }
}
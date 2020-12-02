package com.example.mywearosapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mywearosapp.data.Note
import com.example.mywearosapp.databinding.NotesTemplateBinding


class NotesRVAdapter(val notes: MutableList<Note>, val clickListener: OnItemClickListener):
    RecyclerView.Adapter<NotesRVAdapter.NotesViewHolder>() {

    inner class NotesViewHolder(private val binding: NotesTemplateBinding):
            RecyclerView.ViewHolder(binding.root) {
        private lateinit var note: Note

        fun bind(note: Note, action: OnItemClickListener) {
            this.note = note
            binding.apply {
                serialNumber.text  = (adapterPosition + 1).toString()
                noteTitle.text = note.title
            }
            itemView.setOnClickListener {
                action.onItemClick(note, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val binding = NotesTemplateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val currentNote = notes[position]
        holder.bind(currentNote, clickListener)
    }

    override fun getItemCount(): Int {
        return notes.size
    }
}

interface OnItemClickListener{
    fun onItemClick(note: Note, position: Int)
}
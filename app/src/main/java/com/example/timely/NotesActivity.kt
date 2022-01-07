package com.example.timely

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.timely.databinding.ActivityNotesBinding

class NotesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotesBinding

//    private val viewModel: NotesViewModel by viewModels {
//        NotesViewModelFactory((application as NoteApplication).repository)
//    }
    override fun onCreate(savedInstanceState: Bundle?) {

    binding = ActivityNotesBinding.inflate(layoutInflater)
    super.onCreate(savedInstanceState)
    setContentView(binding.root)


        val adapter = RecyclerViewAdapter(this)
        binding.myRecyclerView.adapter = adapter

//        viewModel.allNotes.observe(this, { notes ->
//            notes?.let {
//                adapter.updateNotes(notes)
//            }
//        })
//    }


//    fun addNewNote(view: android.view.View) {
//        val noteText = binding.AddNote.text.toString()
//        if (noteText.isNotEmpty()) {
//            viewModel.insert(NoteEntity(note = noteText))
//        }
//    }

//    fun onItemClick(note: NoteEntity) {
//        viewModel.delete(note)
 }

}
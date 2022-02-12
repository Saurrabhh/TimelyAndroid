package com.example.timely.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.timely.NoteEntity
import com.example.timely.viewModel.NotesViewModel
import com.example.timely.RecyclerViewAdapter
import com.example.timely.databinding.FragmentNotesBinding
import com.google.firebase.auth.FirebaseAuth

class NotesFragment : Fragment() {


    private lateinit var binding: FragmentNotesBinding
    private lateinit var viewModel: NotesViewModel
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(this)[NotesViewModel::class.java]
        auth = FirebaseAuth.getInstance()
        KEY.fragmentName = KEY().NOTES
        binding = FragmentNotesBinding.inflate(layoutInflater, container, false)
        return binding  .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = RecyclerViewAdapter(this)
        binding.myRecyclerView.adapter = adapter

        viewModel.allNotes.observe(viewLifecycleOwner, { notes ->
            notes?.let {
                adapter.updateNotes(notes)
            }
        })

        binding.addButton.setOnClickListener {
            val noteText = binding.addNoteView.text.toString()
            if (noteText.isNotEmpty()) {
                viewModel.insert(NoteEntity(note = noteText))
            }
        }
    }

    fun onItemClick(note: NoteEntity) {
        viewModel.delete(note)
    }

}
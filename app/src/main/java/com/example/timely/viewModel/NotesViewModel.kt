package com.example.timely.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.example.timely.NoteEntity
import com.example.timely.repo.NoteRepository
import com.example.timely.db.NoteDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotesViewModel(application: Application) : AndroidViewModel(application) {


    val allNotes: LiveData<List<NoteEntity>>
    private val repository: NoteRepository

    init {
        val userdao = NoteDataBase.getDatabase(application).noteDao()
        repository = NoteRepository(userdao)
        allNotes = repository.allNotes
    }

    fun insert(note: NoteEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(note)
    }

    fun delete(note: NoteEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(note)
    }


}
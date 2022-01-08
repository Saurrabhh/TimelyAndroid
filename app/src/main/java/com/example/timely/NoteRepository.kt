package com.example.timely

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class NoteRepository (private val noteDao: NoteDao) {

    val allNotes: LiveData<List<NoteEntity>> = noteDao.loadAllNotes()

    suspend fun insert(note: NoteEntity) {
        noteDao.insert(note)
    }

    suspend fun delete(note: NoteEntity) {
        noteDao.delete(note)
    }
}
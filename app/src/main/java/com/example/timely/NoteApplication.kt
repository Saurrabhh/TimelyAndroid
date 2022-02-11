package com.example.timely

import android.app.Application
import com.example.timely.db.NoteDataBase
import com.example.timely.repo.NoteRepository

class NoteApplication: Application() {
    private val dataBase by lazy { NoteDataBase.getDatabase(this) }
    val repository by lazy { NoteRepository(dataBase.noteDao()) }
}
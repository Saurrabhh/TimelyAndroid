package com.example.timely.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.timely.NoteEntity

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: NoteEntity)

    @Delete
    suspend fun delete(note: NoteEntity)

    @Query("SELECT * FROM note_table ORDER BY note ASC")
    fun loadAllNotes(): LiveData<List<NoteEntity>>
}
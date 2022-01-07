package com.example.timely.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "user_table")
data class User(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "first_name") val firstName: String?,
    @ColumnInfo(name = "username") val username: String?,
    @ColumnInfo(name = "urn") val urn: String?,
    @ColumnInfo(name = "semester") val semester: String?,
    @ColumnInfo(name = "rollno") val rollno: String?,
    @ColumnInfo(name = "section") val section: String?,
    @ColumnInfo(name = "email") val email: String?,

)

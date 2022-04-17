package com.example.timely.dataClasses


data class Student(
    val name: String ?= null,
    val rollno: String ?= null,
    val time: String ?= null,
    val date: String ?= null,
    var present: Boolean ?= null
)

package com.example.timely.dataClasses

data class User(
    val uid: String ?= null,
    val name: String ?= null,
    val username: String ?= null,
    val urn: String ?= null,
    val semester: String ?= null,
    val rollno: String ?= null,
    val section: String ?= null,
    val email: String ?= null,
    val gender: String ?= null,
    val branch: String ?= null,
    val phoneno: String ?= null,
    val enroll: String ?= null,
    val isteacher: Boolean = false
)



package com.example.timely.data

import androidx.lifecycle.LiveData

class UserRepository(private val userDAO: UserDAO) {

    val readalldata: LiveData<List<User>> = userDAO.readAllData()

    suspend fun addUser(user: User) {
        userDAO.addUser(user)
    }


}
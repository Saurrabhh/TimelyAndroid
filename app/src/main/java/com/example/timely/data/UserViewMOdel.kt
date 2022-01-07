package com.example.timely.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewMOdel(application: Application): AndroidViewModel(application) {

    val  readalldata: LiveData<List<User>>
    private val repository: UserRepository


    init {
        val userDao = UserDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
        readalldata = repository.readalldata

    }

    fun adduser(user: User){
        viewModelScope.launch(Dispatchers.IO){
            repository.addUser(user)
        }
    }



}
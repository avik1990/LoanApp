package com.app.loanserviceapp.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras


class RegisterViewModelFactory(private var repository: RegisterRepository) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModelNotfound")
    }
}

/*
class MainViewModelFactory(private var count:Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(count) as T
        }
        throw IllegalArgumentException("ViewModelNotfound")
    }

}*/

package com.app.loanserviceapp.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.loanserviceapp.register.model.RegisterResponse
import com.app.loanserviceapp.utils.Constants
import com.app.loanserviceapp.utils.Datastore.writeBool
import com.app.loanserviceapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
@Inject constructor(private val repository: LoginRepository,private val context : Context) : ViewModel() {

    private val _responseOTP: MutableLiveData<NetworkResult<RegisterResponse>> = MutableLiveData()
    val responseOTP: LiveData<NetworkResult<RegisterResponse>> = _responseOTP

    fun sendForOTP(userPhone:String) = viewModelScope.launch {
        repository.getLoggedin(userPhone).collect { values ->
            _responseOTP.value = values
        }
    }

    fun saveLoginStatus(isLoggedIn: Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            context.writeBool(Constants.IS_LOGGED_IN, isLoggedIn)
        }
    }
}
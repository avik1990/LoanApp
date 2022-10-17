package com.app.loanserviceapp.password

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.loanserviceapp.password.model.LoginResponse
import com.app.loanserviceapp.register.model.RegisterResponse
import com.app.loanserviceapp.utils.Constants.Companion.IS_LOGGED_IN
import com.app.loanserviceapp.utils.Constants.Companion.USER_ID
import com.app.loanserviceapp.utils.Datastore.writeBool
import com.app.loanserviceapp.utils.Datastore.writeString
import com.app.loanserviceapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordPinViewModel @Inject constructor(
    private val repository: OTPRepository, private val context : Context) : ViewModel() {

    private val _response: MutableLiveData<NetworkResult<RegisterResponse>> = MutableLiveData()
    val response: LiveData<NetworkResult<RegisterResponse>> = _response

    private val _responseLogin: MutableLiveData<NetworkResult<LoginResponse>> = MutableLiveData()
    val responseLogin: LiveData<NetworkResult<LoginResponse>> = _responseLogin

    fun getUserRegistered(userName:String,userPhone:String,userEmail:String,userOtp:String) = viewModelScope.launch {
        repository.getUserRegistered(userName,userPhone,userEmail,userOtp).collect { values ->
            _response.value = values
        }
    }

    fun checkLogin(userPhone:String,userOtp:String) = viewModelScope.launch {
        repository.checkLogin(userPhone,userOtp).collect { values ->
            _responseLogin.value = values
        }
    }

    fun saveLoginStatus(isLoggedIn: Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            context.writeBool(IS_LOGGED_IN, isLoggedIn)
        }
    }

    fun saveUserID(userId: String){
        viewModelScope.launch(Dispatchers.IO) {
            context.writeString(USER_ID, userId)
        }
    }

}
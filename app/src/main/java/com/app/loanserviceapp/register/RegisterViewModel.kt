package com.app.loanserviceapp.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.loanserviceapp.register.model.RegisterResponse
import com.app.loanserviceapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel
@Inject constructor(
    private val repository: RegisterRepository) : ViewModel() {

    private val _response: MutableLiveData<NetworkResult<RegisterResponse>> = MutableLiveData()
    val response: LiveData<NetworkResult<RegisterResponse>> = _response

    private val _responseOTP: MutableLiveData<NetworkResult<RegisterResponse>> = MutableLiveData()
    val responseOTP: LiveData<NetworkResult<RegisterResponse>> = _responseOTP

    fun fetchRegisterResponse(userName:String,userPhone:String,userEmail:String) = viewModelScope.launch {
        repository.getRegistered(userName,userPhone,userEmail).collect { values ->
            _response.value = values
        }
    }

    fun sendForOTP(userPhone:String) = viewModelScope.launch {
        repository.sentOTP(userPhone).collect { values ->
            _responseOTP.value = values
        }
    }
}




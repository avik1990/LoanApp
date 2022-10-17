package com.app.loanserviceapp.verification.documentation

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.loanserviceapp.register.model.RegisterResponse
import com.app.loanserviceapp.utils.Constants
import com.app.loanserviceapp.utils.Datastore.writeBool
import com.app.loanserviceapp.utils.NetworkResult
import com.app.loanserviceapp.verification.documentation.model.UploadFileResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class DocumenViewModel @Inject constructor(private val repository: DocumentUploadRepo,private val context : Context) :
    ViewModel() {

    private val _responseUpload: MutableLiveData<NetworkResult<UploadFileResponse>> = MutableLiveData()
    val responseOTP: LiveData<NetworkResult<UploadFileResponse>> = _responseUpload

    fun uploadFile(file : MultipartBody.Part) = viewModelScope.launch {
        repository.uploadFile(file).collect { values ->
            _responseUpload.value = values
        }
    }

}
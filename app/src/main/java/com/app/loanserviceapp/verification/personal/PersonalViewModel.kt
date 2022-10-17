package com.app.loanserviceapp.verification.personal

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.loanserviceapp.password.OTPRepository
import com.app.loanserviceapp.register.model.RegisterResponse
import com.app.loanserviceapp.utils.NetworkResult
import com.app.loanserviceapp.verification.personal.model.ApplicationResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonalViewModel @Inject constructor(
    private val repository: PersonalRepo, private val context : Context
) : ViewModel() {

    private val _responseSubmit: MutableLiveData<NetworkResult<ApplicationResponse>> = MutableLiveData()
    val responsesubmit: LiveData<NetworkResult<ApplicationResponse>> = _responseSubmit

    fun getSubmittedApplication(UsersID:String,
                          PackageID:String,
                          PackageName:String,
                          InsuranceFee:String,
                          ProcessingFee:String,
                          FullName:String,
                          MobileNumber:String,
                          Age:String,
                          CurrentAddress:String,
                          PermanentAddress:String,
                          EmailAddress:String,
                          PassportImage:String,
                          AadharImage:String,
                          PancardImage:String,
                          IncomeProofImage:String) = viewModelScope.launch {
        repository.submitApplication(UsersID,
            PackageID,
            PackageName,
            InsuranceFee,
            ProcessingFee,
            FullName,
            MobileNumber,
            Age,
            CurrentAddress,
            PermanentAddress,
            EmailAddress,
            PassportImage,
            AadharImage,
            PancardImage,
            IncomeProofImage).collect { values ->
            _responseSubmit.value = values
        }
    }
}
package com.app.loanserviceapp.loanapackages

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.loanserviceapp.loanapackages.model.LoanPckages
import com.app.loanserviceapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoanApprovalViewModel @Inject constructor(
    private val repository: LoanApprovalRepo
) : ViewModel() {

    val _responsePackages: MutableLiveData<NetworkResult<LoanPckages>> = MutableLiveData()
    val responsePa: LiveData<NetworkResult<LoanPckages>> = _responsePackages

    fun getPackagesData() = viewModelScope.launch {
        repository.getPackages().collect { values ->
            _responsePackages.value = values
        }
    }
}
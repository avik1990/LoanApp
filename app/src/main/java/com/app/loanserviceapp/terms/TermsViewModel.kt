package com.app.loanserviceapp.terms

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.loanserviceapp.model.PageDetails
import com.app.loanserviceapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TermsViewModel @Inject constructor(private val repository: TermsRepo) : ViewModel() {

    private val _responseLoanStatus: MutableLiveData<NetworkResult<PageDetails>> = MutableLiveData()
    val responseStatus: LiveData<NetworkResult<PageDetails>> = _responseLoanStatus

    fun getPageContent(pageName: String)= viewModelScope.launch {
        repository.getPageContent(pageName).collect { values ->
            _responseLoanStatus.value = values
        }
    }
}
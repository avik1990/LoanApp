package com.app.loanserviceapp.aboutus

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
class AboutusViewModel @Inject constructor(private val repository: AboutRepo) : ViewModel() {

    private val _responseLoanStatus: MutableLiveData<NetworkResult<PageDetails>> = MutableLiveData()
    val responseStatus: LiveData<NetworkResult<PageDetails>> = _responseLoanStatus

    fun getPageContent(pageName: String)= viewModelScope.launch {
        repository.getPageContent(pageName).collect { values ->
            _responseLoanStatus.value = values
        }
    }
}
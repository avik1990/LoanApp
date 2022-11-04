package com.app.loanserviceapp.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.loanserviceapp.payment.model.PaymentStatusResponse1
import com.app.loanserviceapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel@Inject constructor(
    private val repository: PaymentRepo
) : ViewModel() {

    private val _response: MutableLiveData<NetworkResult<PaymentStatusResponse1>> = MutableLiveData()
    val response: LiveData<NetworkResult<PaymentStatusResponse1>> = _response

    fun sentPaymentStatusResponse(json:String) = viewModelScope.launch {
        repository.sendPaymentStatusToServer(json).collect { values ->
            _response.value = values
        }
    }
}
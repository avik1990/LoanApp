package com.app.loanserviceapp.dashboard

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.loanserviceapp.dashboard.model.LoanApplicationSattus
import com.app.loanserviceapp.dashboard.model.payment.PaymentResponse
import com.app.loanserviceapp.utils.Constants
import com.app.loanserviceapp.utils.Datastore.writeString
import com.app.loanserviceapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: HomeRepo, private val context : Context
) : ViewModel() {

    private val _responseLoanStatus: MutableLiveData<NetworkResult<LoanApplicationSattus>> = MutableLiveData()
    val responseStatus: LiveData<NetworkResult<LoanApplicationSattus>> = _responseLoanStatus

    private val _responsePaymentKeys: MutableLiveData<NetworkResult<PaymentResponse>> = MutableLiveData()
    val responsePayment: LiveData<NetworkResult<PaymentResponse>> = _responsePaymentKeys

    fun getLoanStatusList(userId: String)= viewModelScope.launch {
        repository.getLoanStatusList(userId).collect { values ->
            _responseLoanStatus.value = values
        }
    }
    fun getPaymentKeys()= viewModelScope.launch {
        repository.getPaymentKeys().collect { values ->
            _responsePaymentKeys.value = values
        }
    }

    fun saveMerchnatKey(MerchnatKey: String){
        viewModelScope.launch(Dispatchers.IO) {
            context.writeString(Constants.MerchantKey, MerchnatKey)
        }
    }

    fun saveMerchantSalt(MerchantSalt: String){
        viewModelScope.launch(Dispatchers.IO) {
            context.writeString(Constants.MerchantSalt, MerchantSalt)
        }
    }

}
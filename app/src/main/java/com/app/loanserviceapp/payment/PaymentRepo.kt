package com.app.loanserviceapp.payment

import com.app.loanserviceapp.datasource.RemoteDataSource
import com.app.loanserviceapp.payment.model.PaymentStatusResponse1
import com.app.loanserviceapp.utils.BaseApiResponse
import com.app.loanserviceapp.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PaymentRepo @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : BaseApiResponse() {

    suspend fun sendPaymentStatusToServer(json:String): Flow<NetworkResult<PaymentStatusResponse1>> {
        return flow {
            emit(safeApiCall { remoteDataSource.sentpaymentStatus(json) })
        }.flowOn(Dispatchers.IO)
    }
}
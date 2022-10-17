package com.app.loanserviceapp.dashboard

import com.app.loanserviceapp.dashboard.model.LoanApplicationSattus
import com.app.loanserviceapp.datasource.RemoteDataSource
import com.app.loanserviceapp.utils.BaseApiResponse
import com.app.loanserviceapp.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class HomeRepo @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : BaseApiResponse() {

    suspend fun getLoanStatusList(userId: String): Flow<NetworkResult<LoanApplicationSattus>> {
        return flow {
            emit(safeApiCall { remoteDataSource.getLoanstatus(userId) })
        }.flowOn(Dispatchers.IO)
    }
}
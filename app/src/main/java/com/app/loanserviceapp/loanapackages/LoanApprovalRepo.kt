package com.app.loanserviceapp.loanapackages

import com.app.loanserviceapp.datasource.RemoteDataSource
import com.app.loanserviceapp.loanapackages.model.LoanPckages
import com.app.loanserviceapp.utils.BaseApiResponse
import com.app.loanserviceapp.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LoanApprovalRepo  @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : BaseApiResponse() {

    suspend fun getPackages(): Flow<NetworkResult<LoanPckages>> {
        return flow {
            emit(safeApiCall { remoteDataSource.getPackages() })
        }.flowOn(Dispatchers.IO)
    }
}
package com.app.loanserviceapp.terms

import com.app.loanserviceapp.datasource.RemoteDataSource
import com.app.loanserviceapp.model.PageDetails
import com.app.loanserviceapp.utils.BaseApiResponse
import com.app.loanserviceapp.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TermsRepo  @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : BaseApiResponse() {

    suspend fun getPageContent(pageNme: String): Flow<NetworkResult<PageDetails>> {
        return flow {
            emit(safeApiCall { remoteDataSource.getPageContent(pageNme) })
        }.flowOn(Dispatchers.IO)
    }
}
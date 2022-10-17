package com.app.loanserviceapp.login

import com.app.loanserviceapp.datasource.RemoteDataSource
import com.app.loanserviceapp.register.model.RegisterResponse
import com.app.loanserviceapp.utils.BaseApiResponse
import com.app.loanserviceapp.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class LoginRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : BaseApiResponse() {

    suspend fun getLoggedin(userPhone:String): Flow<NetworkResult<RegisterResponse>> {
        return flow {
            emit(safeApiCall { remoteDataSource.sentOTP(userPhone) })
        }.flowOn(Dispatchers.IO)
    }
}
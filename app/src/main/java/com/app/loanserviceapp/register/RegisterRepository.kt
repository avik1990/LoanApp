package com.app.loanserviceapp.register

import com.app.loanserviceapp.datasource.RemoteDataSource
import com.app.loanserviceapp.register.model.RegisterResponse
import com.app.loanserviceapp.utils.BaseApiResponse
import com.app.loanserviceapp.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RegisterRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : BaseApiResponse() {

    suspend fun getRegistered(userName:String,userPhone:String,userEmail:String): Flow<NetworkResult<RegisterResponse>> {
        return flow {
            emit(safeApiCall { remoteDataSource.getRegistered(userName,userPhone,userEmail) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun sentOTP(userPhone:String): Flow<NetworkResult<RegisterResponse>> {
        return flow {
            emit(safeApiCall { remoteDataSource.sentOTP(userPhone) })
        }.flowOn(Dispatchers.IO)
    }
}
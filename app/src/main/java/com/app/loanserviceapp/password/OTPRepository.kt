package com.app.loanserviceapp.password

import com.app.loanserviceapp.datasource.RemoteDataSource
import com.app.loanserviceapp.password.model.LoginResponse
import com.app.loanserviceapp.register.model.RegisterResponse
import com.app.loanserviceapp.utils.BaseApiResponse
import com.app.loanserviceapp.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class OTPRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : BaseApiResponse() {

    suspend fun getUserRegistered(userName:String,userPhone:String,userEmail:String, otp:String): Flow<NetworkResult<RegisterResponse>> {
        return flow {
            emit(safeApiCall { remoteDataSource.sentValueForRegistration(userName,userPhone,userEmail,otp) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun checkLogin(userPhone:String,otp:String): Flow<NetworkResult<LoginResponse>> {
        return flow {
            emit(safeApiCall { remoteDataSource.checkLogin(userPhone,otp) })
        }.flowOn(Dispatchers.IO)
    }

}
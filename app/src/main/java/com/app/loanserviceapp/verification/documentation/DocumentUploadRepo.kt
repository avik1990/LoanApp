package com.app.loanserviceapp.verification.documentation

import com.app.loanserviceapp.datasource.RemoteDataSource
import com.app.loanserviceapp.register.model.RegisterResponse
import com.app.loanserviceapp.utils.BaseApiResponse
import com.app.loanserviceapp.utils.NetworkResult
import com.app.loanserviceapp.verification.documentation.model.UploadFileResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import javax.inject.Inject

class DocumentUploadRepo @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : BaseApiResponse() {

    suspend fun uploadFile(file : MultipartBody.Part): Flow<NetworkResult<UploadFileResponse>> {
        return flow {
            emit(safeApiCall { remoteDataSource.uploadFile(file) })
        }.flowOn(Dispatchers.IO)
    }
}
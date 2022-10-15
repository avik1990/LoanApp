package com.app.loanserviceapp.verification.personal

import com.app.loanserviceapp.datasource.RemoteDataSource
import com.app.loanserviceapp.utils.BaseApiResponse
import com.app.loanserviceapp.utils.NetworkResult
import com.app.loanserviceapp.verification.personal.model.ApplicationResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PersonalRepo@Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : BaseApiResponse() {

    suspend fun submitApplication(UsersID:String,
                                  PackageID:String,
                                  PackageName:String,
                                  InsuranceFee:String,
                                  ProcessingFee:String,
                                  FullName:String,
                                  MobileNumber:String,
                                  Age:String,
                                  CurrentAddress:String,
                                  PermanentAddress:String,
                                  EmailAddress:String,
                                  PassportImage:String,
                                  AadharImage:String,
                                  PancardImage:String,
                                  IncomeProofImage:String): Flow<NetworkResult<ApplicationResponse>> {
        return flow {
            emit(safeApiCall { remoteDataSource.submitApplication(UsersID,
                PackageID,
                PackageName,
                InsuranceFee,
                ProcessingFee,
                FullName,
                MobileNumber,
                Age,
                CurrentAddress,
                PermanentAddress,
                EmailAddress,
                PassportImage,
                AadharImage,
                PancardImage,
                IncomeProofImage) })
        }.flowOn(Dispatchers.IO)
    }

}
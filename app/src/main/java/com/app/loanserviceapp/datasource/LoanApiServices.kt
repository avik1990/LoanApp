package com.app.loanserviceapp.datasource

import com.app.loanserviceapp.dashboard.model.LoanApplicationSattus
import com.app.loanserviceapp.loanapackages.model.LoanPckages
import com.app.loanserviceapp.password.model.LoginResponse
import com.app.loanserviceapp.register.model.RegisterResponse
import com.app.loanserviceapp.utils.Constants
import com.app.loanserviceapp.verification.documentation.model.UploadFileResponse
import com.app.loanserviceapp.verification.personal.model.ApplicationResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface LoanApiServices {

    @FormUrlEncoded
    @POST(Constants.validateRegistration)
    suspend fun getUserRegistration(@Field("FullName") FullName: String,
                                    @Field("MobileNumber") MobileNumber: String,
                                    @Field("EmailAddress") EmailAddress: String): Response<RegisterResponse>

    @FormUrlEncoded
    @POST(Constants.OTP_URL)
    suspend fun getOTP(@Field("MobileNumber") MobileNumber: String,
    ): Response<RegisterResponse>

    @FormUrlEncoded
    @POST(Constants.REGISTRATION_URL)
    suspend fun sentRegistration(@Field("FullName") FullName: String,
                                    @Field("MobileNumber") MobileNumber: String,
                                    @Field("EmailAddress") EmailAddress: String,
                                    @Field("OTPNumber") OTPNumber: String,): Response<RegisterResponse>

    @FormUrlEncoded
    @POST(Constants.CHECK_LOGIN)
    suspend fun checkLogin(@Field("MobileNumber") MobileNumber: String,
                                 @Field("OTPNumber") OTPNumber: String): Response<LoginResponse>


    @GET(Constants.GET_PACKAGES)
    suspend fun getPackages(): Response<LoanPckages>

    @GET(Constants.LOAN_STATUS)
    suspend fun getLoanStatus(@Query("UsersID") usersID: String): Response<LoanApplicationSattus>

    @Multipart
    @POST("uploadFileInServer")
    suspend fun uploadImage(@Part filePart: MultipartBody.Part): Response<UploadFileResponse>

    @FormUrlEncoded
    @POST(Constants.SUBMIT_APPLICATION)
    suspend fun submitApplication(@Field("UsersID") UsersID: String,
                                  @Field("PackageID") PackageID: String,
                                  @Field("PackageName") PackageName: String,
                                  @Field("InsuranceFee") InsuranceFee: String,
                                  @Field("ProcessingFee") ProcessingFee: String,
                                  @Field("FullName") FullName: String,
                                  @Field("MobileNumber") MobileNumber: String,
                                  @Field("Age") Age: String,
                                  @Field("CurrentAddress") CurrentAddress: String,
                                  @Field("PermanentAddress") PermanentAddress: String,
                                  @Field("EmailAddress") EmailAddress: String,
                                  @Field("PassportImage") PassportImage: String,
                                  @Field("AadharImage") AadharImage: String,
                                  @Field("PancardImage") PancardImage: String,
                                  @Field("IncomeProofImage") IncomeProofImage: String,): Response<ApplicationResponse>


    /*UsersID:1
    PackageID:1
    PackageName:Pack Name
    InsuranceFee:10

    ProcessingFee:20
    FullName:Sanjoy
    MobileNumber:1234567890
    Age:81
    CurrentAddress:Ad-1
    PermanentAddress:Ad-2
    EmailAddress:sanjoy@gmail.com
    PassportImage:abcd.jpg
    AadharImage:efgh.jpg
    PancardImage:ijk.jpg
    IncomeProofImage:mno.jpg*/

}
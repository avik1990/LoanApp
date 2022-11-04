package com.app.loanserviceapp.datasource

import okhttp3.MultipartBody
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val dogService: LoanApiServices) {

    suspend fun getRegistered(userName:String,userPhone:String,userEmail:String) =
        dogService.getUserRegistration(userName,userPhone,userEmail)

    suspend fun sentOTP(userPhone:String) =
        dogService.getOTP(userPhone)

    suspend fun checkloginValidation(userPhone:String) =
        dogService.getloginValidation(userPhone)

    suspend fun sentValueForRegistration(userName:String,userPhone:String,userEmail:String,otp:String) =
        dogService.sentRegistration(userName,userPhone,userEmail,otp)

    suspend fun checkLogin(userPhone:String,otp:String) =
        dogService.checkLogin(userPhone,otp)

    suspend fun getPackages() =
        dogService.getPackages()

    suspend fun getLoanstatus(usersID: String) =
        dogService.getLoanStatus(usersID)

    suspend fun uploadFile(file : MultipartBody.Part) =
        dogService.uploadImage(file)

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
                                  IncomeProofImage:String) =
        dogService.submitApplication(UsersID,
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
            IncomeProofImage)

    suspend fun getPageContent(pageName: String) =
        dogService.getPageContent(pageName)

    suspend fun sentpaymentStatus(json:String) =
        dogService.sentPaymentStatus(json)
}

package com.app.loanserviceapp.utils

class Constants {

    companion object {
        const val BASE_URL = "http://app.indianloanservice.in/api/"
        const val REGISTRATION_URL = "Jsondata/userRegistration"
        const val validateRegistration = "Jsondata/validateRegistration"
        const val OTP_URL = "Jsondata/sendOTP"
        const val GET_PACKAGES = "Jsondata/getPackages"
        const val SUBMIT_APPLICATION = "Jsondata/submitApplication"
        const val CHECK_LOGIN = "Jsondata/checkLogin"
        const val LOAN_STATUS = "Jsondata/getMyDashboard"
        const val UPLOAD_FILE = "Jsondata/uploadFileInServer"
        const val PAGE_CONTENT = "Jsondata/getPage"
        const val PAYMENT_STATUS = "payumoney/order_success"
        const val loginValidation = "Jsondata/loginValidation"
        //const val LOGIN_URL = ""
        const val PREFERENCE_NAME = "MyDataStore"
        const val IS_LOGGED_IN = "Loggedin"
        const val USER_ID = "USERID"
        const val USER_PHONE = "PHONE"
        const val USER_EMAIL = "EMAIL"
        const val USER_NAME = "USERNAME"

        const val surl = "https://www.payumoney.com/mobileapp/payumoney/success.php"
        const val furl = "https://www.payumoney.com/mobileapp/payumoney/failure.php"

        //Test Key and Salt
        const val testKey = "DbisRvr5"
        const val testSalt = "08CLIO7ZCW"
        /**
         * Enter below keys when integrating Multi Currency Payments.
         * To get these credentials, please reach out to your Key Account Manager at PayU
         * */
        const val merchantAccessKey = "<Please_add_your_merchant_access_key>"
        const val merchantSecretKey = "<Please_add_your_merchant_secret_key>"


        //Prod Key and Salt
        const val prodKey = "0MQaQP"
        const val prodSalt = "<Please_add_salt_here>"

    }
    enum class PAYMENTSTUATS {
        PEND, APPROVED, FAILED, CANCELED
    }


}
package com.app.loanserviceapp.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

class Constants {

    companion object {
        const val BASE_URL = "http://indianloanservice.in/api/Jsondata/"
        const val REGISTRATION_URL = "userRegistration"
        const val validateRegistration = "validateRegistration"
        const val OTP_URL = "sendOTP"
        const val GET_PACKAGES = "getPackages"
        const val SUBMIT_APPLICATION = "submitApplication"
        const val CHECK_LOGIN = "checkLogin"
        const val LOAN_STATUS = "getMyDashboard"
        const val PAGE_CONTENT = "getPage"

        //const val LOGIN_URL = ""

        const val PREFERENCE_NAME = "MyDataStore"
        const val IS_LOGGED_IN = "Loggedin"
        const val USER_ID = "USERID"
        const val USER_NAME = "USERNAME"

    }


}
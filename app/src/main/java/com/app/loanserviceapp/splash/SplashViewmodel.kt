package com.app.loanserviceapp.splash

import android.app.Application
import androidx.lifecycle.ViewModel
import com.app.loanserviceapp.utils.Constants.Companion.IS_LOGGED_IN
import com.app.loanserviceapp.utils.Datastore.readBool
import com.app.loanserviceapp.utils.Datastore.readString

class SplashViewmodel(val appContext: Application) : ViewModel() {

    fun getSavedLoggedinStatus(){
      //  val getUserName = appContext.readBool(IS_LOGGED_IN).
     //   val getUserName = appContext.readString(IS_LOGGED_IN).asLiveData()
    }
}
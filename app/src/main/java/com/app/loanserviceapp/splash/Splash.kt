package com.app.loanserviceapp.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.app.loanserviceapp.R
import com.app.loanserviceapp.main.MainActivity
import com.app.loanserviceapp.utils.Constants.Companion.IS_LOGGED_IN
import com.app.loanserviceapp.utils.Datastore.readBool
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.observeOn
import kotlinx.coroutines.launch

class Splash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)

            Handler().postDelayed({
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }, 3000)

    }
}
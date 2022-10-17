package com.app.loanserviceapp.main

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.datastore.dataStore
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.app.loanserviceapp.R
import com.app.loanserviceapp.databinding.ActivityMain2Binding
import com.app.loanserviceapp.utils.Constants.Companion.IS_LOGGED_IN
import com.app.loanserviceapp.utils.Datastore
import com.app.loanserviceapp.utils.Datastore.Clear
import com.app.loanserviceapp.utils.Datastore.readBool
import com.app.loanserviceapp.utils.permissions.PermissionManager
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMain2Binding
    private lateinit var navController : NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    var getUserStatus = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        PermissionManager.requestAllRequiredPermissions(this)
        binding.navView.itemIconTintList=null

       runBlocking {
           val getUserName = applicationContext.readBool(IS_LOGGED_IN)
           getUserStatus = getUserName.first()
       }


        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_contact, R.id.nav_aboutus,R.id.nav_logout
            ), binding.drawerLayout
        )

        navController = findNavController(R.id.nav_host_fragment_content_main)
        binding.navView.setupWithNavController(navController)

        binding.drawerMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(Gravity.LEFT);
        }

        navController.addOnDestinationChangedListener {
                controller, destination, arguments ->
            Log.e("HelloFragment",navController.currentDestination?.label.toString())
            binding.drawerMenu.visibility= View.VISIBLE
            when {
                navController.currentDestination?.label.toString() == "Login" -> {
                    binding.drawerMenu.visibility= View.INVISIBLE
                }
                navController.currentDestination?.label.toString().equals("Register") -> {
                    binding.drawerMenu.visibility=View.INVISIBLE
                }
                navController.currentDestination?.label.toString().equals("Passcode") -> {
                    binding.drawerMenu.visibility=View.INVISIBLE
                }
                navController.currentDestination?.label.toString().equals("Passcode") -> {
                    binding.drawerMenu.visibility=View.INVISIBLE
                }
                navController.currentDestination?.label.toString().equals("Terms") -> {
                    binding.imgProfile.visibility=View.VISIBLE
                    binding.imgNotification.visibility=View.VISIBLE
                }
                navController.currentDestination?.label.toString().equals("Dashboard") -> {
                    binding.imgProfile.visibility=View.VISIBLE
                    binding.imgNotification.visibility=View.VISIBLE
                }
                navController.currentDestination?.label.toString().equals("Dashboard") -> {
                binding.imgProfile.visibility=View.VISIBLE
                binding.imgNotification.visibility=View.VISIBLE
                }
                navController.currentDestination?.label.toString().equals("Personal") -> {
                binding.imgProfile.visibility=View.VISIBLE
                binding.imgNotification.visibility=View.VISIBLE
            }

            }
        }

        //Toast.makeText(applicationContext,getUserStatus.toString(),Toast.LENGTH_SHORT).show()
        if(getUserStatus){
            navController.navigate(
                R.id.action_Login_to_DashboardFragment
            )
        }

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_logout -> {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Log out")
                    builder.setMessage("Are you sure to logout?")

                    builder.setPositiveButton("Yes") { dialog, which ->
                        runBlocking {
                            applicationContext.Clear()
                        }
                    }

                    builder.setNegativeButton(android.R.string.no) { dialog, which ->
                        dialog.dismiss()
                    }

                    builder.show()
                    true
                }
                else -> {
                    false
                }
            }
        }


    }




}
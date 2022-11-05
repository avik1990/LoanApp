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
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.*
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.app.loanserviceapp.R
import com.app.loanserviceapp.aboutus.AboutusFragment
import com.app.loanserviceapp.contact.ContactFragment
import com.app.loanserviceapp.dashboard.Dashboard
import com.app.loanserviceapp.databinding.ActivityMain2Binding
import com.app.loanserviceapp.databinding.NavHeaderMainBinding
import com.app.loanserviceapp.login.Login
import com.app.loanserviceapp.terms.TermsFragment
import com.app.loanserviceapp.utils.Constants
import com.app.loanserviceapp.utils.Constants.Companion.IS_LOGGED_IN
import com.app.loanserviceapp.utils.Datastore
import com.app.loanserviceapp.utils.Datastore.Clear
import com.app.loanserviceapp.utils.Datastore.readBool
import com.app.loanserviceapp.utils.Datastore.readString
import com.app.loanserviceapp.utils.permissions.PermissionManager
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    var getUserStatus = false
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navGraph: NavGraph
    var pageName = ""
    var userName = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        PermissionManager.requestAllRequiredPermissions(this)
        binding.navView.itemIconTintList = null

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        navController = navHostFragment.navController

        navGraph = navController.navInflater.inflate(R.navigation.nav_graph)

        runBlocking {
            val getUserName = applicationContext.readString(Constants.USER_NAME)
            userName = getUserName.first().toString()

            val getLoggedinStatus = applicationContext.readBool(IS_LOGGED_IN)
            getUserStatus = getLoggedinStatus.first()

            val headerview = binding.navView.getHeaderView(0)
            val NavDrawheaderviewRef = NavHeaderMainBinding.bind(headerview)
            NavDrawheaderviewRef.tvUserName.text = "Hello $userName"
        }


        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_contact, R.id.nav_aboutus, R.id.nav_logout
            ), binding.drawerLayout
        )

        binding.navView.setupWithNavController(navController)

        binding.drawerMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(Gravity.LEFT);
        }

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            Log.e("HelloFragment", navController.currentDestination?.label.toString())
            pageName = navController.currentDestination?.label.toString()
            binding.drawerMenu.visibility = View.VISIBLE
            when {
                navController.currentDestination?.label.toString() == "Login" -> {
                    binding.drawerMenu.visibility = View.INVISIBLE
                }
                navController.currentDestination?.label.toString().equals("Register") -> {
                    binding.drawerMenu.visibility = View.INVISIBLE
                }
                navController.currentDestination?.label.toString().equals("Passcode") -> {
                    binding.drawerMenu.visibility = View.INVISIBLE
                }
                navController.currentDestination?.label.toString().equals("Passcode") -> {
                    binding.drawerMenu.visibility = View.INVISIBLE
                }
                navController.currentDestination?.label.toString().equals("Terms") -> {
                    binding.imgProfile.visibility = View.VISIBLE
                    binding.imgNotification.visibility = View.VISIBLE
                }
                navController.currentDestination?.label.toString().equals("Dashboard") -> {
                    binding.imgProfile.visibility = View.VISIBLE
                    binding.imgNotification.visibility = View.VISIBLE
                }
                navController.currentDestination?.label.toString().equals("Dashboard") -> {
                    binding.imgProfile.visibility = View.VISIBLE
                    binding.imgNotification.visibility = View.VISIBLE
                }
                navController.currentDestination?.label.toString().equals("Personal") -> {
                    binding.imgProfile.visibility = View.VISIBLE
                    binding.imgNotification.visibility = View.VISIBLE
                }
                navController.currentDestination?.label.toString().equals("Confirmed") -> {
                    binding.imgProfile.visibility = View.VISIBLE
                    binding.imgNotification.visibility = View.VISIBLE
                    binding.drawerMenu.visibility = View.INVISIBLE
                }
            }
        }

        //setup startdestination
        setNavigationGraph()
        setupNavigationClick()
    }

    private fun setupNavigationClick() {
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_logout -> {
                    binding.drawerLayout.closeDrawer(Gravity.LEFT)
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Log out")
                    builder.setMessage("Are you sure to Log out?")

                    builder.setPositiveButton("Yes") { dialog, which ->
                        runBlocking {
                            applicationContext.Clear()
                        }
                        dialog.dismiss()
                        finishAffinity()
                    }

                    builder.setNegativeButton(android.R.string.no) { dialog, which ->
                        dialog.dismiss()
                    }

                    builder.show()
                    true
                }
                R.id.nav_aboutus -> {
                    binding.drawerLayout.closeDrawer(Gravity.LEFT)
                    var termsFragment = AboutusFragment()
                    supportFragmentManager.beginTransaction()
                        .addToBackStack(termsFragment.javaClass.name)
                        .add(R.id.nav_host_fragment_content_main, termsFragment)
                        .commit()
                    true
                }
                R.id.nav_home -> {
                    binding.drawerLayout.closeDrawer(Gravity.LEFT)
                    var termsFragment = Dashboard()
                    supportFragmentManager.beginTransaction()
                        .addToBackStack(termsFragment.javaClass.name)
                        .add(R.id.nav_host_fragment_content_main, termsFragment)
                        .commit()
                    true
                }
                R.id.nav_contact -> {
                    binding.drawerLayout.closeDrawer(Gravity.LEFT)
                    var termsFragment = ContactFragment()
                    supportFragmentManager.beginTransaction()
                        .addToBackStack(termsFragment.javaClass.name)
                        .add(R.id.nav_host_fragment_content_main, termsFragment)
                        .commit()
                    true
                }
                else ->
                    false
            }
        }
    }

    private fun setNavigationGraph() {
        if (getUserStatus) {
            navGraph.setStartDestination(R.id.DashboardFragment)
        } else {
            navGraph.setStartDestination(R.id.Login)
        }
        navController.graph = navGraph
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (pageName == "Personal") {
            navController.navigate(R.id.action_PersonalFragment_to_DashboardFragment)
        }
    }
}
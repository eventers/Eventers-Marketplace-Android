package com.eventersapp.marketplace

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.eventersapp.marketplace.util.SharedPref

class HostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host)
        setupUI()
    }

    private fun setupUI() {
        val navHost =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        val navController = navHost!!.navController
        if (SharedPref.getStringPref(this, SharedPref.KEY_USER_DATA) != "")
            navController.navigate(R.id.action_signupScreenFragment_to_dashboardFragment)
    }

}

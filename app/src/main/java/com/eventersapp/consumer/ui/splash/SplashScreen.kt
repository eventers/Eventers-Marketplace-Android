package com.eventersapp.consumer.ui.splash

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.eventersapp.consumer.HostActivity
import com.eventersapp.consumer.R
import com.eventersapp.consumer.util.goToActivity

class SplashScreen : AppCompatActivity() {

    companion object {
        private const val DELAY_TIME_IN_MILLS = 2500L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        goToNextRoute()
    }

    private fun goToNextRoute() {
        Handler().postDelayed({
            goToActivity(HostActivity::class.java)
            finish()
        }, DELAY_TIME_IN_MILLS)
    }
}

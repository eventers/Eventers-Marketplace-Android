package com.eventersapp.marketplace.ui.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eventersapp.marketplace.HostActivity
import com.eventersapp.marketplace.R
import com.eventersapp.marketplace.ui.viewmodel.SplashState
import com.eventersapp.marketplace.ui.viewmodel.SplashViewModel
import com.eventersapp.marketplace.util.goToActivity
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein

class SplashScreen : AppCompatActivity(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModel: SplashViewModel by lazy {
        ViewModelProvider(this).get(SplashViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        viewModel.splashStateLiveData.observe(this, Observer {
            when (it) {
                is SplashState.SplashScreen -> {
                    goToNextRoute()
                }
            }
        })
    }

    private fun goToNextRoute() {
        goToActivity(HostActivity::class.java)
        finish()
    }
}

package com.eventersapp.marketplace

import android.app.Application
import com.eventersapp.marketplace.data.local.AppDatabase
import com.eventersapp.marketplace.data.network.ApiInterface
import com.eventersapp.marketplace.data.network.BlogApiInterface
import com.eventersapp.marketplace.data.network.NetworkConnectionInterceptor
import com.eventersapp.marketplace.data.repositories.*
import com.eventersapp.marketplace.ui.viewmodelfactory.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class EventersAlgorandBlockchainApplication : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@EventersAlgorandBlockchainApplication))

        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { ApiInterface(instance()) }
        bind() from singleton { BlogApiInterface(instance()) }
        bind() from singleton { SignupRepository(instance()) }
        bind() from provider { SignupViewModelFactory(instance()) }
        bind() from singleton { PhoneOTPRepository(instance()) }
        bind() from provider { PhoneOTPViewModelFactory(instance()) }
        bind() from singleton { ProfileRepository(instance(), instance()) }
        bind() from provider { ProfileViewModelFactory(instance()) }
        bind() from singleton {
            BlogRepository(
                instance()
            )
        }
        bind() from provider { BlogViewModelFactory(instance()) }
        bind() from singleton { CreateEventRepository(instance()) }
        bind() from provider { CreateEventViewModelFactory(instance()) }
        bind() from singleton { MyEventRepository(instance()) }
        bind() from provider { MyEventViewModelFactory(instance()) }
        bind() from singleton { AllEventRepository(instance()) }
        bind() from provider { AllEventViewModelFactory(instance()) }
        bind() from singleton { ResellOrSendEventRepository(instance()) }
        bind() from provider { ResellOrSendEventViewModelFactory(instance()) }
        bind() from singleton { BuyEventRepository(instance()) }
        bind() from provider { BuyEventViewModelFactory(instance()) }
        bind() from provider { AppDatabase(instance()) }
        bind() from singleton { AccountDetailsRepository(instance()) }
        bind() from provider { AccountDetailsViewModelFactory(instance()) }
        bind() from singleton { AccountSettingsRepository(instance()) }
        bind() from provider { AccountSettingsViewModelFactory(instance()) }
        bind() from singleton { RecoverPassphraseRepository(instance()) }
        bind() from provider { RecoverPassphraseViewModelFactory(instance()) }
    }

}

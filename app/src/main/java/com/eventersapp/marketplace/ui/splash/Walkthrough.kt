package com.eventersapp.marketplace.ui.splash

import android.os.Bundle
import com.chyrta.onboarder.OnboarderActivity
import com.chyrta.onboarder.OnboarderPage
import com.eventersapp.marketplace.HostActivity
import com.eventersapp.marketplace.R
import com.eventersapp.marketplace.util.goToActivity


class Walkthrough : OnboarderActivity() {

    private var onboarderPages: List<OnboarderPage>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onboarderPages = ArrayList()
        val onboarderPage1 =
            OnboarderPage(
                "Event App",
                "All event suites like guest management, invites, website and app in one place. It is part of Organiser toolkit",
                R.drawable.onboard_mobile_img
            )
        val onboarderPage2 =
            OnboarderPage(
                "Personalised Website",
                "Your stories and memories in one place, to share with the world",
                R.drawable.onboard_couple_img
            )
        val onboarderPage3 =
            OnboarderPage(
                "Event Shelf",
                "Manage all your event invites in one place with smart notification",
                R.drawable.onboard_calender_img
            )

        onboarderPage1.setTitleColor(R.color.black)
        onboarderPage1.setDescriptionColor(R.color.black)
        onboarderPage2.setTitleColor(R.color.black)
        onboarderPage2.setDescriptionColor(R.color.black)
        onboarderPage3.setTitleColor(R.color.black)
        onboarderPage3.setDescriptionColor(R.color.black)
        onboarderPage1.setBackgroundColor(R.color.white)
        onboarderPage2.setBackgroundColor(R.color.white)
        onboarderPage3.setBackgroundColor(R.color.white)
        (onboarderPages as ArrayList<OnboarderPage>).add(onboarderPage1)
        (onboarderPages as ArrayList<OnboarderPage>).add(onboarderPage2)
        (onboarderPages as ArrayList<OnboarderPage>).add(onboarderPage3)
        setActiveIndicatorColor(R.color.colorAccent)
        shouldUseFloatingActionButton(true)
        setOnboardPagesReady(onboarderPages)
        setSkipButtonTitle("Skip")
    }

    override fun onFinishButtonPressed() {
        goToActivity(HostActivity::class.java)
    }
}

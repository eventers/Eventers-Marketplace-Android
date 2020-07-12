package com.eventersapp.consumer.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.eventersapp.consumer.R
import com.eventersapp.consumer.databinding.FragmentHomeBinding
import com.eventersapp.consumer.ui.blockchain.AllEventFragment
import com.eventersapp.consumer.ui.blockchain.MyEventFragment
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein

class HomeFragment : Fragment(), KodeinAware {

    private val tabTitles = arrayOf(
        "My Events",
        "All Events"
    )

    override val kodein by closestKodein()
    private lateinit var dataBind: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dataBind = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container,
            false
        )
        return dataBind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        (activity as AppCompatActivity?)!!.setSupportActionBar(dataBind.toolbar)
        setTabAdapter()
    }

    private fun setTabAdapter() {
        val addPublicEventTabAdapter =
            AddPublicEventTabAdapter(childFragmentManager)
        dataBind.eventViewPager.offscreenPageLimit = 1
        dataBind.eventViewPager.adapter = addPublicEventTabAdapter
        dataBind.eventTabs.setupWithViewPager(dataBind.eventViewPager)
    }


    // <editor-fold desc="Inner Classes">

    inner class AddPublicEventTabAdapter(fm: FragmentManager) :
        FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> {
                    val myEventFragment = MyEventFragment()
                    myEventFragment
                }
                1 -> {
                    val allEventFragment = AllEventFragment()
                    allEventFragment
                }
                else -> {
                    val myEventFragment = MyEventFragment()
                    myEventFragment
                }
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return tabTitles[position]
        }

        override fun getCount(): Int {
            return tabTitles.size
        }

    }

    //</editor-fold>


}

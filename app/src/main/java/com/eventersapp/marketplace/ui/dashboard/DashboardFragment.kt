package com.eventersapp.marketplace.ui.dashboard

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.ImageViewCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.eventersapp.marketplace.R
import com.eventersapp.marketplace.databinding.DashboardFragmentBinding
import com.eventersapp.marketplace.ui.viewmodel.DashboardViewModel
import com.eventersapp.marketplace.util.color


class DashboardFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = DashboardFragment()
    }

    private lateinit var dataBind: DashboardFragmentBinding
    private var startingPosition = 0
    private val viewModel: DashboardViewModel by lazy {
        ViewModelProvider(this).get(DashboardViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fragment = HomeFragment()
        loadFragment(fragment, 0)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBind = DataBindingUtil.inflate(
            inflater,
            R.layout.dashboard_fragment,
            container,
            false
        )
        return dataBind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }


    override fun onClick(view: View) {
        loadFragmentFromBottomNav(view.id)
    }


    private fun setupUI() {
        dataBind.navHome.setOnClickListener(this)
        dataBind.navScan.setOnClickListener(this)
        dataBind.navBlog.setOnClickListener(this)
        dataBind.navProfile.setOnClickListener(this)
        dataBind.fabSelectService.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_createEventFragment)
        }
        when (viewModel.bottomNavPosition) {
            0 -> ImageViewCompat.setImageTintList(
                dataBind.navHome,
                ColorStateList.valueOf(requireActivity().color(R.color.colorAccent))
            )
            1 -> ImageViewCompat.setImageTintList(
                dataBind.navScan,
                ColorStateList.valueOf(requireActivity().color(R.color.colorAccent))
            )
            2 -> ImageViewCompat.setImageTintList(
                dataBind.navBlog,
                ColorStateList.valueOf(requireActivity().color(R.color.colorAccent))
            )
            3 -> ImageViewCompat.setImageTintList(
                dataBind.navProfile,
                ColorStateList.valueOf(requireActivity().color(R.color.colorAccent))
            )
        }

    }

    private fun loadFragment(fragment: Fragment?, newPosition: Int): Boolean {
        if (fragment != null) {
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
            if (startingPosition > newPosition) {
                transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
            }
            if (startingPosition < newPosition) {
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            transaction.replace(R.id.frame_container, fragment)
            transaction.commit()
            startingPosition = newPosition
            viewModel.bottomNavPosition = startingPosition
            return true
        }

        return false
    }

    private fun loadFragmentFromBottomNav(itemId: Int): Boolean {
        var fragment: Fragment? = null
        var position = 0

        when (itemId) {

            R.id.nav_home -> {
                resetBottomNavActionButtonColor()
                ImageViewCompat.setImageTintList(
                    dataBind.navHome,
                    ColorStateList.valueOf(requireActivity().color(R.color.colorAccent))
                )
                fragment = HomeFragment()
                position = 0
            }
            R.id.nav_scan -> {
                resetBottomNavActionButtonColor()
                ImageViewCompat.setImageTintList(
                    dataBind.navScan,
                    ColorStateList.valueOf(requireActivity().color(R.color.colorAccent))
                )
                fragment = ScanFragment()
                position = 1
            }
            R.id.nav_blog -> {
                resetBottomNavActionButtonColor()
                ImageViewCompat.setImageTintList(
                    dataBind.navBlog,
                    ColorStateList.valueOf(requireActivity().color(R.color.colorAccent))
                )
                fragment = BlogFragment()
                position = 2
            }

            R.id.nav_profile -> {
                resetBottomNavActionButtonColor()
                ImageViewCompat.setImageTintList(
                    dataBind.navProfile,
                    ColorStateList.valueOf(requireActivity().color(R.color.colorAccent))
                )
                fragment = ProfileFragment()
                position = 3
            }

        }

        return loadFragment(fragment, position)
    }

    private fun resetBottomNavActionButtonColor() {
        ImageViewCompat.setImageTintList(
            dataBind.navHome,
            ColorStateList.valueOf(requireActivity().color(R.color.gray))
        )
        ImageViewCompat.setImageTintList(
            dataBind.navScan,
            ColorStateList.valueOf(requireActivity().color(R.color.gray))
        )
        ImageViewCompat.setImageTintList(
            dataBind.navBlog,
            ColorStateList.valueOf(requireActivity().color(R.color.gray))
        )
        ImageViewCompat.setImageTintList(
            dataBind.navProfile,
            ColorStateList.valueOf(requireActivity().color(R.color.gray))
        )
    }


}

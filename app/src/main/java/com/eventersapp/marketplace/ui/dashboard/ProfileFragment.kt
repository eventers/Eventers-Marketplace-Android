package com.eventersapp.marketplace.ui.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.eventersapp.marketplace.R
import com.eventersapp.marketplace.databinding.FragmentProfileBinding
import com.eventersapp.marketplace.ui.viewmodel.ProfileViewModel
import com.eventersapp.marketplace.ui.viewmodelfactory.ProfileViewModelFactory
import com.eventersapp.marketplace.util.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class ProfileFragment : Fragment(), KodeinAware, View.OnClickListener {

    companion object {
        private const val ADDRESS = "<Algorand-URL>"//Replace with Algorand address url
    }

    override val kodein by closestKodein()
    private lateinit var dataBind: FragmentProfileBinding
    private val factory: ProfileViewModelFactory by instance()
    private val viewModel: ProfileViewModel by lazy {
        ViewModelProvider(requireActivity(), factory).get(ProfileViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDeviceInfo()
        if (viewModel.isProfileApiCalled) {
            viewModel.getLoadedProfileData()
        } else {
            getJWTToken()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dataBind = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_profile,
            container,
            false
        )
        dataBind.viewmodel = viewModel
        return dataBind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        initializeObserver()
        setupAPICall()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.input_profile_address -> {
                requireContext().openInBrowser(ADDRESS + dataBind.inputProfileAddress.text)
            }
            R.id.text_account_settings -> {
                findNavController().navigate(R.id.action_dashboardFragment_to_accountSettingsFragment)
            }
        }
    }

    private fun setupUI() {
        fetchSelectedAddressFromDb()
        dataBind.inputProfileAddress.setOnClickListener(this)
        dataBind.textAccountSettings.setOnClickListener(this)
    }

    private fun initializeObserver() {
        viewModel.accountAddressLiveData.observe(viewLifecycleOwner, Observer {
            if (it.isNotBlank()) {
                dataBind.inputProfileAddress.setText(it)
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun setupAPICall() {
        viewModel.logoutLiveData.observe(viewLifecycleOwner, EventObserver { state ->
            when (state) {
                is State.Loading -> {
                }
                is State.Success -> {
                    requireActivity().showToast("Logout")
                    SharedPref.clearPref(requireContext())
                    findNavController().navigate(R.id.action_dashboardFragment_to_signupScreenFragment)
                }
                is State.Error -> {
                    AppUtils.hideProgressBar()
                    requireActivity().showToast(state.message)
                }
            }
        })

        viewModel.profileDetailLiveData.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is State.Loading -> {
                    AppUtils.showProgressBar(requireContext())
                }
                is State.Success -> {
                    var googleImageUrl = ""
                    var fbImageUrl = ""
                    var phoneNumber = ""
                    var accountAddress = ""

                    state.data.data.user.let {
                        googleImageUrl = it.gImageUrl
                        fbImageUrl = it.fbImageUrl
                        phoneNumber = it.phoneCountryCode + it.phoneNumber
                    }

                    when {
                        googleImageUrl != null -> {
                            AppUtils.setGlideRoundedImage(
                                dataBind.imageProfileUser,
                                googleImageUrl
                            )
                            state.data.data.user.let {
                                dataBind.textUserName.text = it.gName
                                dataBind.inputProfileEmail.setText(it.gEmail)
                                dataBind.inputProfilePhoneNumber.setText(phoneNumber)
                            }

                        }
                        fbImageUrl != null -> {
                            AppUtils.setGlideRoundedImage(
                                dataBind.imageProfileUser,
                                fbImageUrl
                            )
                            state.data.data.user.let {
                                dataBind.textUserName.text = it.fbName
                                dataBind.inputProfileEmail.setText(it.fbEmail)
                                dataBind.inputProfilePhoneNumber.setText(phoneNumber)
                            }
                        }
                        else -> {
                            dataBind.imageProfileUser.setImageResource(R.drawable.user)
                            state.data.data.user.let {
                                dataBind.inputProfilePhoneNumber.setText(phoneNumber)
                            }
                        }
                    }



                    AppUtils.hideProgressBar()
                }
                is State.Error -> {
                    AppUtils.hideProgressBar()
                    requireActivity().showToast(state.message)
                }
            }
        })
    }

    private fun setDeviceInfo() {
        viewModel.setDeviceInfo(requireContext().deviceId())
    }

    private fun getJWTToken() {
        val userId = AppUtils.getUserPreference(requireContext())?.data?.user?.userId?.toInt()
        viewModel.getJWTToken(userId ?: -1)
    }

    private fun fetchSelectedAddressFromDb() {
        viewModel.fetchSelectedAddressFromDb()
    }


}

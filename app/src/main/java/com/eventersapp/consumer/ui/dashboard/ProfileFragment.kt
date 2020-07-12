package com.eventersapp.consumer.ui.dashboard

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
import com.eventersapp.consumer.R
import com.eventersapp.consumer.databinding.FragmentProfileBinding
import com.eventersapp.consumer.ui.viewmodel.ProfileViewModel
import com.eventersapp.consumer.ui.viewmodelfactory.ProfileViewModelFactory
import com.eventersapp.consumer.util.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class ProfileFragment : Fragment(), KodeinAware {

    companion object {
        private const val ADDRESS = "https://goalseeker.purestake.io/algorand/testnet/account/"
    }

    override val kodein by closestKodein()
    private lateinit var dataBind: FragmentProfileBinding
    private val factory: ProfileViewModelFactory by instance()
    private val viewModel: ProfileViewModel by lazy {
        ViewModelProvider(this, factory).get(ProfileViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDeviceInfo()
        getJWTToken()
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
        setupAPICall()
    }

    private fun setupUI() {
        dataBind.inputProfileAddress.setOnClickListener {
            requireContext().openInBrowser(ADDRESS + dataBind.inputProfileAddress.text)
        }
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
                        accountAddress = it.accountAddress

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
                                dataBind.inputProfileAddress.setText(accountAddress)
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

    private fun getDeviceInfo() {
        viewModel.getDeviceInfo(requireContext().deviceId())
    }

    private fun getJWTToken() {
        val userId = AppUtils.getUserPreference(requireContext())!!.data.user.userId.toInt()
        viewModel.getJWTToken(userId)
    }

}

package com.eventersapp.consumer.ui.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.eventersapp.consumer.R
import com.eventersapp.consumer.databinding.PhoneOtpFragmentBinding
import com.eventersapp.consumer.ui.viewmodel.PhoneOTPViewModel
import com.eventersapp.consumer.ui.viewmodelfactory.PhoneOTPViewModelFactory
import com.eventersapp.consumer.util.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class PhoneOTPFragment : Fragment(), KodeinAware {

    override val kodein by closestKodein()
    private lateinit var dataBind: PhoneOtpFragmentBinding
    private val factory: PhoneOTPViewModelFactory by instance()
    private val viewModel: PhoneOTPViewModel by lazy {
        ViewModelProvider(this, factory).get(PhoneOTPViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val phoneNumber = requireArguments().getString("phone_number")!!
        val countryCode = requireArguments().getString("country_code")!!
        getDeviceInfo()
        sendVerificationCode(Pair(countryCode, phoneNumber))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBind = DataBindingUtil.inflate(
            inflater,
            R.layout.phone_otp_fragment,
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

    private fun setupUI() {
        addTextChangedListenerOnEditText()
    }

    private fun initializeObserver() {
        viewModel.messageLiveData.observe(viewLifecycleOwner, EventObserver {
            if (it.isNotBlank()) {
                dataBind.rootLayout.snackbar(it)
            }
        })
    }

    private fun setupAPICall() {
        viewModel.otpStatusLiveData.observe(viewLifecycleOwner, EventObserver { state ->
            when (state) {
                is State.Loading -> {
                    AppUtils.showProgressBar(requireContext())
                }
                is State.Success -> {
                    AppUtils.hideProgressBar()
                    requireActivity().showToast("OTP has been sent to the given phone number")
                }
                is State.Error -> {
                    AppUtils.hideProgressBar()
                    dataBind.rootLayout.snackbar(state.message)
                }
            }
        })

        viewModel.otpVerificationStatusLiveData.observe(viewLifecycleOwner, EventObserver { state ->
            when (state) {
                is State.Loading -> {
                    AppUtils.showProgressBar(requireContext())
                }
                is State.Success -> {
                    requireActivity().showToast("OTP verification successful")
                    connect()
                }
                is State.Error -> {
                    AppUtils.hideProgressBar()
                    dataBind.rootLayout.snackbar(state.message)
                }
            }
        })

        viewModel.connectLiveData.observe(viewLifecycleOwner, EventObserver { state ->
            when (state) {
                is State.Loading -> {
                }
                is State.Success -> {
                    AppUtils.hideProgressBar()
                    requireActivity().showToast("Login Successful")
                    SharedPref.setObjectPref(
                        requireContext(),
                        SharedPref.KEY_USER_DATA,
                        state.data
                    )
                    findNavController().navigate(R.id.action_phoneOTPFragment_to_dashboardFragment)
                }
                is State.Error -> {
                    AppUtils.hideProgressBar()
                    dataBind.rootLayout.snackbar(state.message)
                }
            }
        })

        viewModel.verifiedNumberLiveData.observe(viewLifecycleOwner, EventObserver { state ->
            when (state) {
                is State.Loading -> {
                }
                is State.Success -> {
                    AppUtils.hideProgressBar()
                    requireActivity().showToast("Login Successful")
                    SharedPref.setObjectPref(
                        requireContext(),
                        SharedPref.KEY_USER_DATA,
                        state.data
                    )
                    findNavController().navigate(R.id.action_phoneOTPFragment_to_dashboardFragment)
                }
                is State.Error -> {
                    AppUtils.hideProgressBar()
                    dataBind.rootLayout.snackbar(state.message)
                }
            }
        })
    }

    private fun sendVerificationCode(
        number: Pair<String, String>
    ) {
        viewModel.sendVerificationCode(number)
    }

    private fun connect() {
        val userId = SharedPref.getStringPref(requireContext(), SharedPref.KEY_USER_ID)
        val provider = SharedPref.getStringPref(requireContext(), SharedPref.KEY_PROVIDER)
        var id = -1
        if (userId != "") {
            id = userId.toInt()
        }
        viewModel.getJWTToken(provider, id)
    }


    private fun getDeviceInfo() {
        viewModel.getDeviceInfo(requireContext().deviceId(), requireContext().manufacturer())
    }

    private fun addTextChangedListenerOnEditText() {
        dataBind.inputOtpBox1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                if (editable.length == 1)
                    dataBind.inputOtpBox2.requestFocus()
            }
        })
        dataBind.inputOtpBox2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                if (editable.length == 1)
                    dataBind.inputOtpBox3.requestFocus()
            }
        })
        dataBind.inputOtpBox3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                if (editable.length == 1)
                    dataBind.inputOtpBox4.requestFocus()
            }
        })
        dataBind.inputOtpBox4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                if (editable.length == 1)
                    dataBind.inputOtpBox5.requestFocus()
            }
        })
        dataBind.inputOtpBox5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                if (editable.length == 1)
                    dataBind.inputOtpBox6.requestFocus()
            }
        })
    }

}

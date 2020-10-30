package com.eventersapp.marketplace.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.eventersapp.marketplace.R
import com.eventersapp.marketplace.databinding.PhoneNumberFragmentBinding
import com.eventersapp.marketplace.ui.viewmodel.PhoneNumberViewModel
import com.eventersapp.marketplace.util.EventObserver
import com.eventersapp.marketplace.util.snackbar
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein


class PhoneNumberFragment : Fragment(), KodeinAware {

    override val kodein by closestKodein()
    private lateinit var dataBind: PhoneNumberFragmentBinding
    private val viewModel: PhoneNumberViewModel by lazy {
        ViewModelProvider(this).get(PhoneNumberViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBind = DataBindingUtil.inflate(
            inflater,
            R.layout.phone_number_fragment,
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
    }


    private fun setupUI() {
        dataBind.countryCodePicker.setOnCountryChangeListener {
            viewModel.setCountryCode(it.phoneCode)
        }
    }

    private fun initializeObserver() {
        viewModel.messageLiveData.observe(viewLifecycleOwner, EventObserver {
            if (it.isNotBlank()) {
                dataBind.rootLayout.snackbar(it)
            }
        })
    }

}

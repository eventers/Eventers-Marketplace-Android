package com.eventersapp.marketplace.ui.accountsettings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.eventersapp.marketplace.R
import com.eventersapp.marketplace.databinding.FragmentAccountDetailsBinding
import com.eventersapp.marketplace.ui.viewmodel.AccountDetailsViewModel
import com.eventersapp.marketplace.ui.viewmodelfactory.AccountDetailsViewModelFactory
import com.eventersapp.marketplace.util.EventObserver
import com.eventersapp.marketplace.util.showToast
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class AccountDetailsFragment : Fragment(), KodeinAware {

    override val kodein by closestKodein()
    private lateinit var dataBind: FragmentAccountDetailsBinding
    private val factory: AccountDetailsViewModelFactory by instance()
    private val viewModel: AccountDetailsViewModel by lazy {
        ViewModelProvider(this, factory).get(AccountDetailsViewModel::class.java)
    }

    private var accountAddress = ""
    private var passphrase = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { arguments ->
            accountAddress = arguments.getString("account_address") ?: ""
            passphrase = arguments.getString("account_passphrase") ?: ""
        }
        Log.i("Info", "Account Address = $accountAddress Passphrase = $passphrase")
        viewModel.setAccountAddress(accountAddress)
        viewModel.setPassphrase(passphrase)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dataBind = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_account_details,
            container,
            false
        )
        dataBind.lifecycleOwner = viewLifecycleOwner
        dataBind.viewmodel = viewModel
        return dataBind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        initializeObserver()
    }


    private fun setupUI() {
        (activity as AppCompatActivity?)?.setSupportActionBar(dataBind.toolbar)
        dataBind.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        dataBind.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun initializeObserver() {
        viewModel.messageLiveData.observe(viewLifecycleOwner, EventObserver {
            if (it.isNotBlank()) {
                requireContext().showToast(it)
            }
        })
    }

}

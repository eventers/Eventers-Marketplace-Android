package com.eventersapp.marketplace.ui.accountsettings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.eventersapp.marketplace.R
import com.eventersapp.marketplace.databinding.FragmentAccountSettingsBinding
import com.eventersapp.marketplace.ui.adapter.CustomAdapterAccount
import com.eventersapp.marketplace.ui.viewmodel.AccountSettingsViewModel
import com.eventersapp.marketplace.ui.viewmodelfactory.AccountSettingsViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class AccountSettingsFragment : Fragment(), KodeinAware, View.OnClickListener {

    override val kodein by closestKodein()
    private lateinit var dataBind: FragmentAccountSettingsBinding
    private lateinit var customAdapterAccount: CustomAdapterAccount
    private val factory: AccountSettingsViewModelFactory by instance()
    private val viewModel: AccountSettingsViewModel by lazy {
        ViewModelProvider(this, factory).get(AccountSettingsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        customAdapterAccount = CustomAdapterAccount(viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dataBind = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_account_settings,
            container,
            false
        )
        return dataBind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        initializeObserver()
    }

    override fun onStart() {
        super.onStart()
        fetchAccounts()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.fab_add_new_account -> {
                findNavController().navigate(R.id.action_accountSettingsFragment_to_addNewAccountFragment)
            }
        }
    }


    private fun setupUI() {
        (activity as AppCompatActivity?)?.setSupportActionBar(dataBind.toolbar)
        dataBind.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        dataBind.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        val mLayoutManager = LinearLayoutManager(activity)
        dataBind.recyclerViewAccount.apply {
            layoutManager = mLayoutManager
            itemAnimator = DefaultItemAnimator()
            adapter = customAdapterAccount
        }
        dataBind.fabAddNewAccount.setOnClickListener(this)
    }

    private fun initializeObserver() {
        viewModel.accountListLiveData.observe(viewLifecycleOwner, Observer {
            customAdapterAccount.submitList(it)
        })
    }

    private fun fetchAccounts() {
        viewModel.fetchAccounts()
    }
}

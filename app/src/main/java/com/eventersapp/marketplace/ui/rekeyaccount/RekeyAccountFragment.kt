package com.eventersapp.marketplace.ui.rekeyaccount

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
import com.eventersapp.marketplace.data.model.Account
import com.eventersapp.marketplace.databinding.FragmentRekeyAccountBinding
import com.eventersapp.marketplace.ui.adapter.CustomAdapterRekeyAccount
import com.eventersapp.marketplace.ui.viewmodel.AccountSettingsViewModel
import com.eventersapp.marketplace.ui.viewmodelfactory.AccountSettingsViewModelFactory
import com.eventersapp.marketplace.util.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class RekeyAccountFragment : Fragment(), KodeinAware, View.OnClickListener {

    override val kodein by closestKodein()
    private lateinit var dataBind: FragmentRekeyAccountBinding
    private lateinit var customAdapterRekeyAccount: CustomAdapterRekeyAccount
    private val factory: AccountSettingsViewModelFactory by instance()
    private val viewModel: AccountSettingsViewModel by lazy {
        ViewModelProvider(this, factory).get(AccountSettingsViewModel::class.java)
    }
    private lateinit var myAccount: Account

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments?.getSerializable("account_detail") != null)
            myAccount = arguments?.getSerializable("account_detail") as Account
        customAdapterRekeyAccount = CustomAdapterRekeyAccount()
        fetchRekeyAccounts()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dataBind = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_rekey_account,
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

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.fab_add_new_account -> {
                findNavController().navigate(R.id.action_rekeyAccountFragment_to_addNewAccountFragment)
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
            adapter = customAdapterRekeyAccount
        }
        dataBind.fabAddNewAccount.setOnClickListener(this)
        dataBind.recyclerViewAccount.addOnItemTouchListener(
            RecyclerTouchListener(
                activity,
                dataBind.recyclerViewAccount,
                object : RecyclerTouchListener.ClickListener {

                    override fun onClick(view: View?, position: Int) {
                        viewModel.setRekeyAccountAdapterPosition(position)
                        viewModel.rekeyAccount(
                            myAccount.passphrase,
                            customAdapterRekeyAccount.currentList[position].accountAddress
                        )
                    }

                    override fun onLongClick(view: View?, position: Int) {

                    }
                })
        )
    }

    private fun initializeObserver() {
        viewModel.rekeyAccountListLiveData.observe(viewLifecycleOwner, EventObserver {
            customAdapterRekeyAccount.submitList(it)
        })
        viewModel.rekeyAccountLiveData.observe(viewLifecycleOwner, EventObserver { state ->
            when (state) {
                is State.Loading -> {
                    AppUtils.showProgressBar(requireContext())
                }
                is State.Success -> {
                    viewModel.updateRekeyAccountDetail(
                        myAccount.id,
                        myAccount.name,
                        myAccount.accountAddress,
                        customAdapterRekeyAccount.currentList[viewModel.getRekeyAccountAdapterPosition()].passphrase,
                        myAccount.isSelected
                    )
                }
                is State.Error -> {
                    AppUtils.hideProgressBar()
                    requireActivity().showToast(state.message)
                }
            }
        })
        viewModel.updateRekeyAccountLiveData.observe(viewLifecycleOwner, EventObserver { state ->
            when (state) {
                is State.Loading -> {

                }
                is State.Success -> {
                    AppUtils.hideProgressBar()
                    requireActivity().showToast("${myAccount.name} is successfully rekey to ${customAdapterRekeyAccount.currentList[viewModel.getRekeyAccountAdapterPosition()].name}")
                    findNavController().popBackStack()
                }
                is State.Error -> {
                    AppUtils.hideProgressBar()
                    requireActivity().showToast(state.message)
                }
            }
        })
    }

    private fun fetchRekeyAccounts() {
        viewModel.fetchRekeyAccounts(myAccount)
    }

}

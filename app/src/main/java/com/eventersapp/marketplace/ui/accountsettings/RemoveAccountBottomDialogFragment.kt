package com.eventersapp.marketplace.ui.accountsettings

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.eventersapp.marketplace.R
import com.eventersapp.marketplace.data.model.Account
import com.eventersapp.marketplace.databinding.RemoveAccountBottomSheetBinding
import com.eventersapp.marketplace.ui.viewmodel.AccountSettingsViewModel
import com.eventersapp.marketplace.ui.viewmodelfactory.AccountSettingsViewModelFactory
import com.eventersapp.marketplace.util.EventObserver
import com.eventersapp.marketplace.util.showToast
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class RemoveAccountBottomDialogFragment : BottomSheetDialogFragment(), KodeinAware,
    View.OnClickListener {

    override val kodein by closestKodein()
    private lateinit var dataBind: RemoveAccountBottomSheetBinding
    private val factory: AccountSettingsViewModelFactory by instance()
    private val viewModel: AccountSettingsViewModel by lazy {
        ViewModelProvider(this, factory).get(AccountSettingsViewModel::class.java)
    }

    private lateinit var myAccount: Account

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments?.getSerializable("account_detail") != null)
            myAccount = arguments?.getSerializable("account_detail") as Account
    }

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        dataBind = DataBindingUtil.inflate(
            inflater,
            R.layout.remove_account_bottom_sheet,
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
            R.id.button_remove_account -> {
                viewModel.deleteAccount(
                    myAccount.id,
                    myAccount.name,
                    myAccount.accountAddress,
                    myAccount.passphrase,
                    myAccount.isSelected
                )

            }
            R.id.button_keep_it -> {
                dismiss()
            }
        }
    }

    private fun setupUI() {
        dataBind.buttonRemoveAccount.setOnClickListener(this)
        dataBind.buttonKeepIt.setOnClickListener(this)
    }

    private fun initializeObserver() {
        viewModel.deleteAccountStatusLiveData.observe(viewLifecycleOwner, EventObserver {
            requireContext().showToast("Account deleted successfully")
            dismiss()
            findNavController().popBackStack()
        })
    }

}
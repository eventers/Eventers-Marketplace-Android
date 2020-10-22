package com.eventersapp.marketplace.ui.accountsettings

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.eventersapp.marketplace.R
import com.eventersapp.marketplace.databinding.AccountSettingsMoreOptionBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AccountSettingsMoreOptionBottomDialogFragment : BottomSheetDialogFragment(),
    View.OnClickListener {


    private lateinit var dataBind: AccountSettingsMoreOptionBottomSheetBinding

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme)
    }

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        dataBind = DataBindingUtil.inflate(
            inflater,
            R.layout.account_settings_more_option_bottom_sheet,
            container,
            false
        )
        return dataBind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.text_close_bottom_sheet -> {
                dismiss()
            }
            R.id.text_rekey_account -> {
                dismiss()
                findNavController().navigate(R.id.action_accountSettingsMoreOptionBottomDialogFragment_to_rekeyAccountFragment)
            }
            R.id.text_view_passphrase -> {
                dismiss()
                findNavController().navigate(R.id.action_accountSettingsMoreOptionBottomDialogFragment_to_viewPassphraseBottomDialogFragment)
            }
            R.id.text_edit_account_name -> {
                dismiss()
                findNavController().navigate(R.id.action_accountSettingsMoreOptionBottomDialogFragment_to_editAccountNameBottomDialogFragment)
            }
            R.id.text_remove_account -> {
                dismiss()
                findNavController().navigate(R.id.action_accountSettingsMoreOptionBottomDialogFragment_to_removeAccountBottomDialogFragment)
            }
        }
    }

    private fun setupUI() {
        dataBind.textCloseBottomSheet.setOnClickListener(this)
        dataBind.textRekeyAccount.setOnClickListener(this)
        dataBind.textViewPassphrase.setOnClickListener(this)
        dataBind.textEditAccountName.setOnClickListener(this)
        dataBind.textRemoveAccount.setOnClickListener(this)
    }


}
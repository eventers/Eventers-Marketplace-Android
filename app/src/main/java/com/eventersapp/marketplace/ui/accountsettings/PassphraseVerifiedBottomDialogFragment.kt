package com.eventersapp.marketplace.ui.accountsettings

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.eventersapp.marketplace.R
import com.eventersapp.marketplace.databinding.PassphraseVerifiedBottomSheetBinding
import com.eventersapp.marketplace.ui.viewmodel.BackupPassphraseViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PassphraseVerifiedBottomDialogFragment : BottomSheetDialogFragment() {


    private lateinit var dataBind: PassphraseVerifiedBottomSheetBinding
    private val viewModel: BackupPassphraseViewModel by lazy {
        ViewModelProvider(requireActivity()).get(BackupPassphraseViewModel::class.java)
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme).apply {
            isCancelable = false
            setCanceledOnTouchOutside(false)
        }
    }

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        dataBind = DataBindingUtil.inflate(
            inflater,
            R.layout.passphrase_verified_bottom_sheet,
            container,
            false
        )
        return dataBind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        dataBind.buttonAccept.setOnClickListener {
            dismiss()
            findNavController().navigate(
                R.id.action_passphraseVerifiedBottomDialogFragment_to_accountDetailsFragment,
                bundleOf(
                    "account_address" to viewModel.getAccountAddress(),
                    "account_passphrase" to viewModel.getPassphrase()
                )
            )
        }
    }


}
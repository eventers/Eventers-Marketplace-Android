package com.eventersapp.marketplace.ui.accountsettings

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.eventersapp.marketplace.R
import com.eventersapp.marketplace.databinding.ViewPassphraseBottomSheetBinding
import com.eventersapp.marketplace.ui.adapter.CustomAdapterBackupPassphrase
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ViewPassphraseBottomDialogFragment : BottomSheetDialogFragment(),
    View.OnClickListener {


    private lateinit var dataBind: ViewPassphraseBottomSheetBinding
    private lateinit var customAdapterBackupPassphrase: CustomAdapterBackupPassphrase

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
            R.layout.view_passphrase_bottom_sheet,
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
            R.id.text_show_qr -> {
                //showPassphraseQRCode()
            }
            R.id.text_share_passphrase -> {
                //sharePassphrase()
            }
        }
    }

    private fun setupUI() {
        val mLayoutManager = GridLayoutManager(activity, 3)
        dataBind.recyclerViewToMnemonic.apply {
            layoutManager = mLayoutManager
            itemAnimator = DefaultItemAnimator()
            adapter = customAdapterBackupPassphrase
        }
        dataBind.textShowQr.setOnClickListener(this)
        dataBind.textSharePassphrase.setOnClickListener(this)
    }

    /*private fun sharePassphrase() {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        val shareBody = viewModel.getPassphrase()
        sharingIntent.putExtra(
            Intent.EXTRA_SUBJECT,
            "Backup Passphrase"
        )
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
        startActivity(Intent.createChooser(sharingIntent, "Share via"))
    }

    private fun showPassphraseQRCode() {
        findNavController().navigate(R.id.action_backupPassphraseFragment_to_showPassphraseQRCodeBottomDialogFragment,
            bundleOf("passphrase" to viewModel.getPassphrase())
        )

    }*/

}
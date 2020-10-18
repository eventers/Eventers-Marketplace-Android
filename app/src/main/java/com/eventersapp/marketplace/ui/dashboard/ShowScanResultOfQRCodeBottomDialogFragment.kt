package com.eventersapp.marketplace.ui.dashboard

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import com.eventersapp.marketplace.R
import com.eventersapp.marketplace.databinding.ShowScanResultOfQrCodeBottomSheetBinding
import com.eventersapp.marketplace.util.copyToClipboard
import com.eventersapp.marketplace.util.showToast
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class ShowScanResultOfQRCodeBottomDialogFragment : BottomSheetDialogFragment(),
    View.OnClickListener {

    companion object {
        fun newInstance(): ShowScanResultOfQRCodeBottomDialogFragment {
            return ShowScanResultOfQRCodeBottomDialogFragment()
        }
    }

    private lateinit var dataBind: ShowScanResultOfQrCodeBottomSheetBinding
    private var scanResult = ""

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scanResult = arguments?.getString("scan_result") ?: ""
    }

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        dataBind = DataBindingUtil.inflate(
            inflater,
            R.layout.show_scan_result_of_qr_code_bottom_sheet,
            container,
            false
        )
        return dataBind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.text_close_bottom_sheet -> {
                dismiss()
            }
            R.id.button_copy_result -> {
                requireContext().copyToClipboard(scanResult)
                requireContext().showToast("Text copied to clipboard")
                dismiss()
            }
        }
    }

    private fun setupUI() {
        dataBind.textScanResult.text = scanResult
        dataBind.textCloseBottomSheet.setOnClickListener(this)
        dataBind.buttonCopyResult.setOnClickListener(this)
    }


}
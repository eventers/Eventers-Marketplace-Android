package com.eventersapp.marketplace.ui.accountsettings

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import com.eventersapp.marketplace.R
import com.eventersapp.marketplace.databinding.EditAccountNameBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EditAccountNameBottomDialogFragment : BottomSheetDialogFragment(),
    View.OnClickListener {


    private lateinit var dataBind: EditAccountNameBottomSheetBinding

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
            R.layout.edit_account_name_bottom_sheet,
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
            R.id.button_done -> {

            }
        }
    }

    private fun setupUI() {
        dataBind.textCloseBottomSheet.setOnClickListener(this)
        dataBind.buttonDone.setOnClickListener(this)
    }


}
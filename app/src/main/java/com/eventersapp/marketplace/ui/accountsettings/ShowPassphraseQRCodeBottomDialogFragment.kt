package com.eventersapp.marketplace.ui.accountsettings

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import com.eventersapp.marketplace.R
import com.eventersapp.marketplace.databinding.ShowPassphraseQrCodeBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder


class ShowPassphraseQRCodeBottomDialogFragment : BottomSheetDialogFragment(),
    View.OnClickListener {

    companion object {
        fun newInstance(): ShowPassphraseQRCodeBottomDialogFragment {
            return ShowPassphraseQRCodeBottomDialogFragment()
        }
    }

    private lateinit var dataBind: ShowPassphraseQrCodeBottomSheetBinding
    private lateinit var bitmap: Bitmap
    private var passphrase = ""

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        passphrase = arguments?.getString("passphrase") ?: ""
    }

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        dataBind = DataBindingUtil.inflate(
            inflater,
            R.layout.show_passphrase_qr_code_bottom_sheet,
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
            R.id.text_close_bottom_sheet -> dismiss()
            R.id.button_share_qr_code -> {
                val bitmapPath: String =
                    MediaStore.Images.Media.insertImage(
                        requireContext().contentResolver,
                        bitmap,
                        "Passphrase QR",
                        null
                    )
                val bitmapUri: Uri = Uri.parse(bitmapPath)
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "image/png"
                intent.putExtra(Intent.EXTRA_STREAM, bitmapUri)
                startActivity(Intent.createChooser(intent, "Share Passphrase QR"))
            }
        }
    }

    private fun setupUI() {
        dataBind.textCloseBottomSheet.setOnClickListener(this)
        dataBind.buttonShareQrCode.setOnClickListener(this)
        generatePassphraseQRCode()
    }

    private fun generatePassphraseQRCode() {
        val text = passphrase
        val multiFormatWriter = MultiFormatWriter();
        try {
            val bitMatrix: BitMatrix =
                multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200)
            val barcodeEncoder = BarcodeEncoder()
            bitmap = barcodeEncoder.createBitmap(bitMatrix)
            dataBind.imageQrCode.setImageBitmap(bitmap);
        } catch (e: WriterException) {
            e.printStackTrace();
        }
    }
}
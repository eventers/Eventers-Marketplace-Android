package com.eventersapp.marketplace.ui.dashboard

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.budiyev.android.codescanner.*
import com.eventersapp.marketplace.R
import com.eventersapp.marketplace.databinding.FragmentScanBinding
import com.eventersapp.marketplace.util.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class ScanFragment : Fragment() {

    private lateinit var dataBind: FragmentScanBinding
    private lateinit var codeScanner: CodeScanner
    private var permissionsToRequest: ArrayList<String>? = null
    private val permissions = ArrayList<String>()
    private val ALL_PERMISSIONS_RESULT = 101

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBind = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_scan,
            container,
            false
        )
        return dataBind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addPermissions()
        setUpScanner()
    }

    override fun onResume() {
        super.onResume()
        openScanner()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun addPermissions() {
        permissions.add(Manifest.permission.CAMERA)
        permissionsToRequest = findUnAskedPermissions(permissions)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest!!.size > 0) {
                requestPermissions(permissionsToRequest!!.toTypedArray(), ALL_PERMISSIONS_RESULT)
            }
        }
    }

    private fun setUpScanner() {
        codeScanner = CodeScanner(requireContext(), dataBind.scannerView)

        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        codeScanner.decodeCallback = DecodeCallback {
            CoroutineScope(Dispatchers.Main).launch {
                requireContext().showToast("Scan result: ${it.text}")
            }
        }

        codeScanner.errorCallback = ErrorCallback {
            CoroutineScope(Dispatchers.Main).launch {
                requireContext().showToast("Camera initialization error: ${it.message}")
            }
        }

        dataBind.scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    private fun openScanner() {
        codeScanner.startPreview()
    }

    private fun findUnAskedPermissions(wanted: ArrayList<String>): ArrayList<String> {
        val result = ArrayList<String>()

        for (perm in wanted) {
            if (!hasPermission(perm)) {
                result.add(perm)
            }
        }

        return result
    }

    private fun hasPermission(permission: String): Boolean {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return checkSelfPermission(
                    requireContext(),
                    permission
                ) == android.content.pm.PackageManager.PERMISSION_GRANTED
            }
        }
        return true
    }

    private fun canMakeSmores(): Boolean {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1
    }


}

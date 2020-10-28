package com.eventersapp.marketplace.ui.accountsettings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.eventersapp.marketplace.R
import com.eventersapp.marketplace.databinding.FragmentBackupPassphraseBinding
import com.eventersapp.marketplace.ui.adapter.CustomAdapterBackupPassphrase
import com.eventersapp.marketplace.ui.viewmodel.BackupPassphraseViewModel
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein

class BackupPassphraseFragment : Fragment(), KodeinAware, View.OnClickListener {

    override val kodein by closestKodein()
    private lateinit var dataBind: FragmentBackupPassphraseBinding
    private lateinit var customAdapterBackupPassphrase: CustomAdapterBackupPassphrase
    private val viewModel: BackupPassphraseViewModel by lazy {
        ViewModelProvider(requireActivity()).get(BackupPassphraseViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        customAdapterBackupPassphrase = CustomAdapterBackupPassphrase()
        generatePassPhrase()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dataBind = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_backup_passphrase,
            container,
            false
        )
        dataBind.viewmodel = viewModel
        return dataBind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        initializeObserver()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.text_show_qr -> {
                showPassphraseQRCode()
            }
            R.id.text_share_passphrase -> {
                sharePassphrase()
            }
        }
    }

    private fun setupUI() {
        (activity as AppCompatActivity?)?.setSupportActionBar(dataBind.toolbar)
        dataBind.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        dataBind.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        val mLayoutManager = GridLayoutManager(activity, 3)
        dataBind.recyclerViewToMnemonic.apply {
            layoutManager = mLayoutManager
            itemAnimator = DefaultItemAnimator()
            adapter = customAdapterBackupPassphrase
        }
        dataBind.textShowQr.setOnClickListener(this)
        dataBind.textSharePassphrase.setOnClickListener(this)
    }

    private fun initializeObserver() {
        viewModel.passphraseListLiveData.observe(viewLifecycleOwner, Observer {
            customAdapterBackupPassphrase.setData(it)
        })

    }

    private fun generatePassPhrase() {
        viewModel.generatePassPhrase()
    }

    private fun sharePassphrase() {
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
        findNavController().navigate(
            R.id.action_backupPassphraseFragment_to_showPassphraseQRCodeBottomDialogFragment,
            bundleOf("passphrase" to viewModel.getPassphrase())
        )

    }
}

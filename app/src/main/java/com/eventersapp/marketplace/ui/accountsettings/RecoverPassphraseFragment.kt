package com.eventersapp.marketplace.ui.accountsettings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.eventersapp.marketplace.R
import com.eventersapp.marketplace.databinding.FragmentRecoverPassphraseBinding
import com.eventersapp.marketplace.ui.viewmodel.RecoverPassphraseViewModel
import com.eventersapp.marketplace.ui.viewmodelfactory.RecoverPassphraseViewModelFactory
import com.eventersapp.marketplace.util.EventObserver
import com.eventersapp.marketplace.util.snackbar
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class RecoverPassphraseFragment : Fragment(), KodeinAware {


    override val kodein by closestKodein()
    private lateinit var dataBind: FragmentRecoverPassphraseBinding
    private val factory: RecoverPassphraseViewModelFactory by instance()
    private val viewModel: RecoverPassphraseViewModel by lazy {
        ViewModelProvider(this, factory).get(RecoverPassphraseViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dataBind = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_recover_passphrase,
            container,
            false
        )
        dataBind.lifecycleOwner = viewLifecycleOwner
        dataBind.viewmodel = viewModel
        return dataBind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        initializeObserver()
    }

    private fun setupUI() {
        (activity as AppCompatActivity?)?.setSupportActionBar(dataBind.toolbar)
        dataBind.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        dataBind.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun initializeObserver() {
        viewModel.messageLiveData.observe(viewLifecycleOwner, EventObserver {
            if (it.isNotBlank()) {
                dataBind.rootLayout.snackbar(it)
            }
        })
        viewModel.showErrorDialogLiveData.observe(viewLifecycleOwner, EventObserver {
            if (it.isNotBlank()) {
                showErrorDialogBox(it)
            }
        })
    }

    private fun showErrorDialogBox(errorMessage: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Error")
        builder.setMessage(errorMessage)
        builder.setPositiveButton("OK") { dialog, which ->
            dialog.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }
}

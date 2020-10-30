package com.eventersapp.marketplace.ui.accountsettings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.eventersapp.marketplace.R
import com.eventersapp.marketplace.databinding.FragmentAddNewAccountBinding
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein


class AddNewAccountFragment : Fragment(), KodeinAware, View.OnClickListener {


    override val kodein by closestKodein()
    private lateinit var dataBind: FragmentAddNewAccountBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dataBind = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add_new_account,
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
            R.id.button_create_account -> {
                findNavController().navigate(R.id.action_addNewAccountFragment_to_backupPassphraseFragment)
            }
            R.id.button_recover_passphrase -> {
                findNavController().navigate(R.id.action_addNewAccountFragment_to_recoverPassphraseFragment)
            }
        }
    }


    private fun setupUI() {
        (activity as AppCompatActivity?)?.setSupportActionBar(dataBind.toolbar)
        dataBind.toolbar.setNavigationIcon(R.drawable.ic_close)
        dataBind.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        dataBind.buttonCreateAccount.setOnClickListener(this)
        dataBind.buttonRecoverPassphrase.setOnClickListener(this)

    }


}

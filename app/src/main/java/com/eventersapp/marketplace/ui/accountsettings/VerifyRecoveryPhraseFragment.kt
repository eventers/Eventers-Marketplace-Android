package com.eventersapp.marketplace.ui.accountsettings

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.eventersapp.marketplace.R
import com.eventersapp.marketplace.databinding.FragmentVerifyRecoveryPhraseBinding
import com.eventersapp.marketplace.ui.adapter.CustomAdapterVerifyRecoveryPhrase
import com.eventersapp.marketplace.ui.viewmodel.BackupPassphraseViewModel
import com.eventersapp.marketplace.util.EventObserver
import com.eventersapp.marketplace.util.RecyclerTouchListener
import com.eventersapp.marketplace.util.hide
import com.eventersapp.marketplace.util.show
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein


class VerifyRecoveryPhraseFragment : Fragment(), KodeinAware {


    override val kodein by closestKodein()
    private lateinit var dataBind: FragmentVerifyRecoveryPhraseBinding
    private lateinit var customAdapterVerifyRecoveryPhrase: CustomAdapterVerifyRecoveryPhrase
    private val viewModel: BackupPassphraseViewModel by lazy {
        ViewModelProvider(requireActivity()).get(BackupPassphraseViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        customAdapterVerifyRecoveryPhrase = CustomAdapterVerifyRecoveryPhrase()
        showRandomPassphraseList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dataBind = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_verify_recovery_phrase,
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
        val mLayoutManager = GridLayoutManager(activity, 3)
        dataBind.recyclerViewToMnemonic.apply {
            layoutManager = mLayoutManager
            itemAnimator = DefaultItemAnimator()
            adapter = customAdapterVerifyRecoveryPhrase
        }
        dataBind.recyclerViewToMnemonic.addOnItemTouchListener(
            RecyclerTouchListener(
                activity,
                dataBind.recyclerViewToMnemonic,
                object : RecyclerTouchListener.ClickListener {

                    override fun onClick(view: View?, position: Int) {
                        viewModel.checkPassphraseWord(customAdapterVerifyRecoveryPhrase.getList()[position])
                    }

                    override fun onLongClick(view: View?, position: Int) {

                    }
                })
        )

    }

    private fun initializeObserver() {
        viewModel.randomPassphraseListLiveData.observe(viewLifecycleOwner, Observer {
            customAdapterVerifyRecoveryPhrase.setData(it)
        })
        viewModel.messageLiveData.observe(viewLifecycleOwner, EventObserver {
            if (it.isNotBlank()) {
                if (it == "Correct choice")
                    dataBind.textStatus.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.green)
                    )
                else
                    dataBind.textStatus.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.red
                        )
                    )
                dataBind.textStatus.show()
                dataBind.textStatus.text = it
                Handler(Looper.getMainLooper()).postDelayed({
                    dataBind.textStatus.hide()
                }, 1500L)
            }
        })
        viewModel.questionCountLiveData.observe(viewLifecycleOwner, EventObserver {
            if (it > 3) {
                findNavController().navigate(R.id.action_verifyRecoveryPhraseFragment_to_passphraseVerifiedBottomDialogFragment)
            }
        })
    }

    private fun showRandomPassphraseList() {
        viewModel.showRandomPassphraseList(1)
    }

}

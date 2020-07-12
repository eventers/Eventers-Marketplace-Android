package com.eventersapp.consumer.ui.blockchain

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.eventersapp.consumer.R
import com.eventersapp.consumer.data.model.MyEventListResponse
import com.eventersapp.consumer.databinding.FragmentResellOrSendEventBinding
import com.eventersapp.consumer.ui.viewmodel.ResellOrSendEventViewModel
import com.eventersapp.consumer.ui.viewmodelfactory.ResellOrSendEventViewModelFactory
import com.eventersapp.consumer.util.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class ResellOrSendEventFragment : Fragment(), KodeinAware {


    override val kodein by closestKodein()
    private lateinit var dataBind: FragmentResellOrSendEventBinding
    private val factory: ResellOrSendEventViewModelFactory by instance()
    private val viewModel: ResellOrSendEventViewModel by lazy {
        ViewModelProvider(this, factory).get(ResellOrSendEventViewModel::class.java)
    }
    private lateinit var myEvent: MyEventListResponse.Data

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myEvent =
            requireArguments().getSerializable("my_event_data") as MyEventListResponse.Data
        getUserInfo(myEvent)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dataBind = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_resell_or_send_event,
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
        setupAPICall()
    }

    private fun setupUI() {
        (activity as AppCompatActivity?)!!.setSupportActionBar(dataBind.toolbar)
        dataBind.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        dataBind.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        dataBind.buttonResell.setOnClickListener {
            showResellTicketDialogBox()
        }
        dataBind.buttonSend.setOnClickListener {
            showSendTicketDialogBox()
        }
    }

    private fun initializeObserver() {
        viewModel.messageLiveData.observe(viewLifecycleOwner, EventObserver {
            if (it.isNotBlank()) {
                dataBind.rootLayout.snackbar(it)
            }
        })
    }

    private fun setupAPICall() {
        viewModel.resellEventLiveData.observe(viewLifecycleOwner, EventObserver { state ->
            when (state) {
                is State.Loading -> {
                    AppUtils.showProgressBar(requireContext())
                }
                is State.Success -> {
                    AppUtils.hideProgressBar()
                    requireActivity().showToast("Price changed successfully")
                    findNavController().navigate(
                        R.id.action_resellOrSendEventFragment_to_dashboardFragment
                    )
                }
                is State.Error -> {
                    AppUtils.hideProgressBar()
                    requireActivity().showToast(state.message)
                }
            }
        })

        viewModel.sendEventLiveData.observe(viewLifecycleOwner, EventObserver { state ->
            when (state) {
                is State.Loading -> {
                    AppUtils.showProgressBar(requireContext())
                }
                is State.Success -> {
                    AppUtils.hideProgressBar()
                    requireActivity().showToast("Event send to another user successfully")
                    findNavController().popBackStack()
                }
                is State.Error -> {
                    AppUtils.hideProgressBar()
                    requireActivity().showToast(state.message)
                }
            }
        })
    }

    private fun getUserInfo(myEvent: MyEventListResponse.Data) {
        val userData = AppUtils.getUserPreference(requireContext())
        viewModel.getUserInfo(myEvent, userData, requireContext().deviceId())
    }

    private fun showResellTicketDialogBox() {
        val viewGroup = requireView().findViewById<ViewGroup>(android.R.id.content)
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.show_resell_ticket_dialog, viewGroup, false)
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogView)
        val alertDialog: AlertDialog = builder.create()
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
        val inputResellAmount = dialogView.findViewById<EditText>(R.id.input_resell_amount)
        val buttonResell = dialogView.findViewById<Button>(R.id.button_resell)
        buttonResell.setOnClickListener {
            if (inputResellAmount.text.isNotEmpty()) {
                viewModel.resellAmount(inputResellAmount.text.toString().toInt())
                alertDialog.dismiss()
            } else {
                dataBind.rootLayout.snackbar("Please enter resell amount")
            }
        }
    }

    private fun showSendTicketDialogBox() {
        val viewGroup = requireView().findViewById<ViewGroup>(android.R.id.content)
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.show_send_ticket_dialog, viewGroup, false)
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogView)
        val alertDialog: AlertDialog = builder.create()
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
        val inputUserId = dialogView.findViewById<EditText>(R.id.input_user_id)
        val buttonSend = dialogView.findViewById<Button>(R.id.button_send)
        buttonSend.setOnClickListener {
            if (inputUserId.text.isNotEmpty()) {
                viewModel.sendToUser(inputUserId.text.toString().toInt())
                alertDialog.dismiss()
            } else {
                dataBind.rootLayout.snackbar("Please enter user id")
            }
        }

    }

}

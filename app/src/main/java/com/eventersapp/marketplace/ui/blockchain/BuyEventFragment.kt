package com.eventersapp.marketplace.ui.blockchain

import android.annotation.SuppressLint
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
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.eventersapp.marketplace.R
import com.eventersapp.marketplace.data.model.AllEventListResponse
import com.eventersapp.marketplace.databinding.FragmentBuyEventBinding
import com.eventersapp.marketplace.ui.adapter.CustomAdapterBuyEvent
import com.eventersapp.marketplace.ui.viewmodel.BuyEventViewModel
import com.eventersapp.marketplace.ui.viewmodelfactory.BuyEventViewModelFactory
import com.eventersapp.marketplace.util.*
import com.eventersapp.marketplace.util.AppConstants.BUY_TICKET
import com.eventersapp.marketplace.util.AppConstants.RESELL_BUY_TICKET
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class BuyEventFragment : Fragment(), KodeinAware {

    override val kodein by closestKodein()
    private lateinit var dataBind: FragmentBuyEventBinding
    private lateinit var customAdapterBuyEvent: CustomAdapterBuyEvent
    private val factory: BuyEventViewModelFactory by instance()
    private val viewModel: BuyEventViewModel by lazy {
        ViewModelProvider(this, factory).get(BuyEventViewModel::class.java)
    }
    private lateinit var allEvent: AllEventListResponse.Data


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments?.getSerializable("all_event_data") != null)
            allEvent =
                arguments?.getSerializable("all_event_data") as AllEventListResponse.Data
        customAdapterBuyEvent = CustomAdapterBuyEvent(this)
        setUserInfo(allEvent)
        getAllBuyEvents(allEvent)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dataBind = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_buy_event,
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

    @SuppressLint("SetTextI18n")
    private fun setupUI() {
        (activity as AppCompatActivity?)?.setSupportActionBar(dataBind.toolbar)
        dataBind.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        dataBind.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        allEvent.publicEvent.let {
            dataBind.textEventTitle.text = it.eventTitle
            dataBind.textEventDescription.text = it.eventDescription
            dataBind.textDateTime.text = AppUtils.changeDateFormat(
                it.dateTime,
                AppConstants.DISPLAY_DATE_FORMAT,
                AppConstants.API_DATE_FORMAT
            )
            if (it.eventImage != null && it.eventImage.isNotEmpty())
                AppUtils.setGlideImage(dataBind.imageEventImage, it.eventImage)
        }
        if (allEvent.eventTicket == null)
            dataBind.textBuyResellTicket.visibility = View.GONE

        val mLayoutManager = LinearLayoutManager(activity)
        mLayoutManager.reverseLayout = true
        mLayoutManager.stackFromEnd = true
        dataBind.recyclerViewBuyEvent.apply {
            isNestedScrollingEnabled = false
            layoutManager = mLayoutManager
            itemAnimator = DefaultItemAnimator()
            adapter = customAdapterBuyEvent
        }
        dataBind.textTicketPrice.text = "$ ${allEvent.publicEvent.ticketPrice}"
        dataBind.textBuyTicket.setOnClickListener {
            showPayNowDialog(0, BUY_TICKET)
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
        viewModel.buyNormalTicketEventLiveData.observe(viewLifecycleOwner, EventObserver { state ->
            when (state) {
                is State.Loading -> {
                    AppUtils.showProgressBar(requireContext())
                }
                is State.Success -> {
                    AppUtils.hideProgressBar()
                    requireActivity().showToast("Ticket buy successfully")
                    findNavController().navigate(R.id.action_buyEventFragment_to_dashboardFragment)
                }
                is State.Error -> {
                    AppUtils.hideProgressBar()
                    requireActivity().showToast(state.message)
                }
            }
        })

        viewModel.buyResellTicketEventLiveData.observe(viewLifecycleOwner, EventObserver { state ->
            when (state) {
                is State.Loading -> {
                    AppUtils.showProgressBar(requireContext())
                }
                is State.Success -> {
                    AppUtils.hideProgressBar()
                    requireActivity().showToast("Resell ticket buy successfully")
                    findNavController().navigate(R.id.action_buyEventFragment_to_dashboardFragment)
                }
                is State.Error -> {
                    AppUtils.hideProgressBar()
                    requireActivity().showToast(state.message)
                }
            }
        })
    }


    private fun getAllBuyEvents(allEvent: AllEventListResponse.Data) {
        if (allEvent.eventTicket != null)
            customAdapterBuyEvent.setData(allEvent.eventTicket)
    }


    private fun setUserInfo(allEvent: AllEventListResponse.Data) {
        val userData = AppUtils.getUserPreference(requireContext())
        viewModel.setUserInfo(allEvent, userData, requireContext().deviceId())
    }

    fun showPayNowDialog(ticketEventId: Int, ticket: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setIcon(R.drawable.eventers_app_logo)
        builder.setTitle("PAY NOW")
        builder.setMessage("Pay for the event by clicking on the button")
        builder.setPositiveButton("Pay now") { dialog, which ->
            if (ticket == BUY_TICKET)
                viewModel.buyNormalTicket()
            else if (ticket == RESELL_BUY_TICKET)
                viewModel.buyResellTicket(ticketEventId)
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.cancel()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

}
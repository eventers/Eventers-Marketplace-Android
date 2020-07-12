package com.eventersapp.consumer.ui.blockchain

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.eventersapp.consumer.R
import com.eventersapp.consumer.databinding.FragmentMyEventBinding
import com.eventersapp.consumer.ui.adapter.CustomAdapterMyEvent
import com.eventersapp.consumer.ui.viewmodel.MyEventViewModel
import com.eventersapp.consumer.ui.viewmodelfactory.MyEventViewModelFactory
import com.eventersapp.consumer.util.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class MyEventFragment : Fragment(), KodeinAware {


    override val kodein by closestKodein()
    private lateinit var dataBind: FragmentMyEventBinding
    private lateinit var customAdapterMyEvent: CustomAdapterMyEvent
    private val factory: MyEventViewModelFactory by instance()
    private val viewModel: MyEventViewModel by lazy {
        ViewModelProvider(this, factory).get(MyEventViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        customAdapterMyEvent = CustomAdapterMyEvent()
        getUserInfo()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dataBind = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_my_event,
            container,
            false
        )
        return dataBind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupAPICall()
    }

    private fun setupUI() {
        val mLayoutManager = LinearLayoutManager(activity)
        mLayoutManager.reverseLayout = true
        mLayoutManager.stackFromEnd = true
        dataBind.recyclerViewMyEvent.apply {
            isNestedScrollingEnabled = false
            layoutManager = mLayoutManager
            itemAnimator = DefaultItemAnimator()
            adapter = customAdapterMyEvent
        }
        dataBind.recyclerViewMyEvent.addOnItemTouchListener(
            RecyclerTouchListener(
                activity,
                dataBind.recyclerViewMyEvent,
                object : RecyclerTouchListener.ClickListener {

                    override fun onClick(view: View?, position: Int) {
                        findNavController().navigate(
                            R.id.action_dashboardFragment_to_resellOrSendEventFragment,
                            bundleOf("my_event_data" to customAdapterMyEvent.getListData()[position])
                        )
                    }

                    override fun onLongClick(view: View?, position: Int) {

                    }
                })
        )
    }


    private fun setupAPICall() {
        viewModel.myEventListLiveData.observe(viewLifecycleOwner, EventObserver { state ->
            when (state) {
                is State.Loading -> {
                    AppUtils.showProgressBar(requireContext())
                }
                is State.Success -> {
                    if (state.data.isNotEmpty()) {
                        dataBind.recyclerViewMyEvent.visibility = View.VISIBLE
                        dataBind.textClickOnPlus.visibility = View.GONE
                        customAdapterMyEvent.setData(state.data)
                    } else {
                        dataBind.recyclerViewMyEvent.visibility = View.GONE
                        dataBind.textClickOnPlus.visibility = View.VISIBLE
                    }
                    AppUtils.hideProgressBar()
                }
                is State.Error -> {
                    AppUtils.hideProgressBar()
                    requireActivity().showToast(state.message)
                }
            }
        })
    }

    private fun getUserInfo() {
        val userData = AppUtils.getUserPreference(requireContext())
        viewModel.getUserInfo(userData?.data?.user?.userId!!.toInt())
    }

}

package com.eventersapp.marketplace.ui.blockchain

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
import com.eventersapp.marketplace.R
import com.eventersapp.marketplace.databinding.FragmentMyEventBinding
import com.eventersapp.marketplace.ui.adapter.CustomAdapterMyEvent
import com.eventersapp.marketplace.ui.viewmodel.MyEventViewModel
import com.eventersapp.marketplace.ui.viewmodelfactory.MyEventViewModelFactory
import com.eventersapp.marketplace.util.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class MyEventFragment : Fragment(), KodeinAware {


    override val kodein by closestKodein()
    private lateinit var dataBind: FragmentMyEventBinding
    private lateinit var customAdapterMyEvent: CustomAdapterMyEvent
    private val factory: MyEventViewModelFactory by instance()
    private val viewModel: MyEventViewModel by lazy {
        ViewModelProvider(requireActivity(), factory).get(MyEventViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        customAdapterMyEvent = CustomAdapterMyEvent()
        if (viewModel.isMyEventApiCalled) {
            viewModel.getLoadedMyEventsList()
        } else {
            getMyEventList()
        }

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
        dataBind.myEventsSwipeRefreshLayout.setOnRefreshListener {
            val userData = AppUtils.getUserPreference(requireContext())
            viewModel.refreshMyEventListData(userData?.data?.user?.userId?.toInt() ?: -1)
        }
    }


    private fun setupAPICall() {
        viewModel.myEventListLiveData.observe(viewLifecycleOwner, EventObserver { state ->
            when (state) {
                is State.Loading -> {
                    if (!dataBind.myEventsSwipeRefreshLayout.isRefreshing)
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
                    if (dataBind.myEventsSwipeRefreshLayout.isRefreshing)
                        dataBind.myEventsSwipeRefreshLayout.isRefreshing = false
                    else
                        AppUtils.hideProgressBar()
                }
                is State.Error -> {
                    AppUtils.hideProgressBar()
                    requireActivity().showToast(state.message)
                }
            }
        })
    }

    private fun getMyEventList() {
        val userData = AppUtils.getUserPreference(requireContext())
        viewModel.getMyEventList(userData?.data?.user?.userId?.toInt() ?: -1)
    }

}

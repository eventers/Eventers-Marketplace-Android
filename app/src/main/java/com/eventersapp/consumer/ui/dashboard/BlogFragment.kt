package com.eventersapp.consumer.ui.dashboard

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eventersapp.consumer.R
import com.eventersapp.consumer.databinding.FragmentBlogBinding
import com.eventersapp.consumer.ui.adapter.CustomAdapterBlog
import com.eventersapp.consumer.ui.viewmodel.BlogViewModel
import com.eventersapp.consumer.ui.viewmodelfactory.BlogViewModelFactory
import com.eventersapp.consumer.util.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class BlogFragment : Fragment(), KodeinAware {


    override val kodein by closestKodein()
    private lateinit var dataBind: FragmentBlogBinding
    private lateinit var customAdapterBlog: CustomAdapterBlog
    private val factory: BlogViewModelFactory by instance()
    private val viewModel: BlogViewModel by lazy {
        ViewModelProvider(this, factory).get(BlogViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        customAdapterBlog = CustomAdapterBlog()
        getBlogList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBind = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_blog,
            container,
            false
        )
        return dataBind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        initializeObserver()
        setupAPICall()
    }

    private fun setupUI() {
        val mLayoutManager = LinearLayoutManager(activity)
        dataBind.recyclerViewBlogPosts.apply {
            isNestedScrollingEnabled = false
            layoutManager = mLayoutManager
            itemAnimator = DefaultItemAnimator()
            adapter = customAdapterBlog
        }
        initScrollListener(dataBind.recyclerViewBlogPosts)

    }

    private fun initializeObserver() {
        viewModel.messageLiveData.observe(viewLifecycleOwner, EventObserver {
            if (it.isNotBlank()) {
                dataBind.rootLayout.snackbar(it)
            }
        })
        viewModel.loadMoreListLiveData.observe(viewLifecycleOwner, Observer {
            if (it) {
                customAdapterBlog.setData(null)
                Handler().postDelayed({
                    viewModel.loadMore()
                }, 2000)
            }
        })
    }

    private fun setupAPICall() {
        viewModel.blogListLiveData.observe(viewLifecycleOwner, EventObserver { state ->
            when (state) {
                is State.Loading -> {
                    AppUtils.showProgressBar(requireContext())
                }
                is State.Success -> {
                    customAdapterBlog.setData(state.data)
                    AppUtils.hideProgressBar()
                }
                is State.Error -> {
                    AppUtils.hideProgressBar()
                    requireActivity().showToast(state.message)

                }
            }
        })
    }

    private fun initScrollListener(recyclerViewGlobalContentPosts: RecyclerView) {
        recyclerViewGlobalContentPosts.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                val visibleItemCount = layoutManager!!.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                viewModel.checkForLoadMoreItems(
                    visibleItemCount,
                    totalItemCount,
                    firstVisibleItemPosition
                )
            }

        })
    }


    private fun getBlogList() {
        viewModel.getBlogList()
    }


}

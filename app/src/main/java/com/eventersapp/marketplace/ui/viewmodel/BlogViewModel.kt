package com.eventersapp.marketplace.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eventersapp.marketplace.data.model.BlogListResponse
import com.eventersapp.marketplace.data.repositories.BlogRepository
import com.eventersapp.marketplace.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BlogViewModel(private val repository: BlogRepository) :
    ViewModel() {

    private var page = 1
    private var totalRecordCount = 0
    private var blogList = ArrayList<BlogListResponse.Post?>()
    var isBlogApiCalled = false

    private val _messageLiveData = MutableLiveData<Event<String>>()
    val messageLiveData: LiveData<Event<String>>
        get() = _messageLiveData

    private val _loadMoreListLiveData = MutableLiveData<Boolean>()
    val loadMoreListLiveData: LiveData<Boolean>
        get() = _loadMoreListLiveData

    private val _blogListLiveData =
        MutableLiveData<Event<State<ArrayList<BlogListResponse.Post?>>>>()
    val blogListLiveData: LiveData<Event<State<ArrayList<BlogListResponse.Post?>>>>
        get() = _blogListLiveData

    private lateinit var blogListResponse: BlogListResponse


    fun getBlogList() {
        if (page == 1) {
            blogList.clear()
            _blogListLiveData.postValue(Event(State.loading()))
        } else {
            if (blogList.isNotEmpty() && blogList.last() == null)
                blogList.removeAt(blogList.size - 1)
        }
        viewModelScope.launch(Dispatchers.IO) {

            try {
                blogListResponse =
                    repository.blog(page, AppConstants.BLOG_KEY)
                withContext(Dispatchers.Main) {
                    isBlogApiCalled = true
                    blogList.addAll(blogListResponse.posts)
                    totalRecordCount = blogListResponse.meta.pagination.total
                    _blogListLiveData.postValue(
                        Event(
                            State.success(
                                blogList
                            )
                        )
                    )

                    _loadMoreListLiveData.value = false
                }
            } catch (e: ApiException) {
                withContext(Dispatchers.Main) {
                    _blogListLiveData.postValue(Event(State.error(e.message ?: "")))
                    _loadMoreListLiveData.value = false
                }
            } catch (e: NoInternetException) {
                withContext(Dispatchers.Main) {
                    _blogListLiveData.postValue(Event(State.error(e.message ?: "")))
                    _loadMoreListLiveData.value = false
                }
            }
        }


    }

    fun refreshBlogListData() {
        page = 1
        getBlogList()
    }


    fun checkForLoadMoreItems(
        visibleItemCount: Int,
        totalItemCount: Int,
        firstVisibleItemPosition: Int
    ) {
        if (!_loadMoreListLiveData.value!! && (totalItemCount < totalRecordCount)) {
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                _loadMoreListLiveData.value = true
            }
        }

    }

    fun loadMore() {
        page++
        getBlogList()
    }

    fun getLoadedBlogList() {
        _blogListLiveData.postValue(
            Event(
                State.success(
                    blogList
                )
            )
        )
    }

}
package com.eventersapp.marketplace.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.eventersapp.marketplace.R
import com.eventersapp.marketplace.data.model.BlogListResponse
import com.eventersapp.marketplace.databinding.ListItemBlogPostsBinding
import com.eventersapp.marketplace.util.AppUtils
import com.eventersapp.marketplace.util.LoadingViewHolder

class CustomAdapterBlog : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_ITEM = 1
        private const val TYPE_LOADING = 2
    }

    private var blogList = ArrayList<BlogListResponse.Post?>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_ITEM -> {
                val binding: ListItemBlogPostsBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context), R.layout.list_item_blog_posts, parent,
                    false
                )
                ItemViewHolder(binding)
            }
            else -> {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_lazy_loading, parent, false)
                LoadingViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ItemViewHolder -> {
                blogList[position]?.let { holder.bindItems(it) }
            }
            is LoadingViewHolder -> {
                holder.showLoadingView()
            }
        }
    }

    override fun getItemCount(): Int {
        return blogList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            blogList[position] == null -> TYPE_LOADING
            else -> TYPE_ITEM
        }
    }

    fun setData(newGlobalContent: ArrayList<BlogListResponse.Post?>?) {
        if (newGlobalContent != null) {
            if (blogList.isNotEmpty())
                blogList.removeAt(blogList.size - 1)
            blogList.clear()
            blogList.addAll(newGlobalContent)
        } else {
            blogList.add(newGlobalContent)
        }
        notifyDataSetChanged()
    }

    fun getListData() = blogList

    inner class ItemViewHolder(val binding: ListItemBlogPostsBinding) :
        RecyclerView.ViewHolder(binding.root) {


        @SuppressLint("SetTextI18n")
        fun bindItems(blog: BlogListResponse.Post) {
            binding.apply {
                AppUtils.setGlideImage(imageBlogItemPoster, blog.featureImage)
                textBlogItemTime.text = "${blog.readingTime} min"
                textBlogItemTitle.text = blog.title
                textBlogItemDescription.text = blog.excerpt
                AppUtils.setGlideRoundedImage(
                    imageBlogItemUser,
                    "https://inspire.eventersapp.com/content/images/size/w100/2020/02/nitika-1.jpeg"
                )
                textBlogItemUserName.text = "Nitika Garg"
            }

        }

    }

}
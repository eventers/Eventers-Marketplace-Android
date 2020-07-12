package com.eventersapp.consumer.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.eventersapp.consumer.R
import com.eventersapp.consumer.data.model.BlogListResponse
import com.eventersapp.consumer.util.AppUtils
import com.eventersapp.consumer.util.show

class CustomAdapterBlog : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_ITEM = 1
        private const val TYPE_LOADING = 2
    }

    private var blogList = ArrayList<BlogListResponse.Post?>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_ITEM -> {
                val v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_blog_posts, parent, false)
                ItemViewHolder(v)
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

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imageBlogItemPoster: ImageView =
            itemView.findViewById(R.id.image_blog_item_poster)
        private val textBlogItemTime: TextView = itemView.findViewById(R.id.text_blog_item_time)
        private val textBlogItemTitle: TextView = itemView.findViewById(R.id.text_blog_item_title)
        private val textBlogItemDescription: TextView =
            itemView.findViewById(R.id.text_blog_item_description)
        private val imageBlogItemUser: ImageView = itemView.findViewById(R.id.image_blog_item_user)
        private val textBlogUserName: TextView =
            itemView.findViewById(R.id.text_blog_item_user_name)


        @SuppressLint("SetTextI18n")
        fun bindItems(blog: BlogListResponse.Post) {
            AppUtils.setGlideImage(imageBlogItemPoster, blog.featureImage)
            textBlogItemTime.text = "${blog.readingTime} min"
            textBlogItemTitle.text = blog.title
            textBlogItemDescription.text = blog.excerpt
            AppUtils.setGlideRoundedImage(
                imageBlogItemUser,
                "https://inspire.eventersapp.com/content/images/size/w100/2020/02/nitika-1.jpeg"
            )
            textBlogUserName.text = "Nitika Garg"
        }

    }

    inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progress_bar)

        fun showLoadingView() {
            progressBar.show()
        }
    }

}
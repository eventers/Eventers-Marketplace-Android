package com.eventersapp.consumer.data.model


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class BlogListResponse(
    @SerializedName("meta")
    val meta: Meta,
    @SerializedName("posts")
    val posts: List<Post>
) {
    @Keep
    data class Meta(
        @SerializedName("pagination")
        val pagination: Pagination
    ) {
        @Keep
        data class Pagination(
            @SerializedName("limit")
            val limit: Int,
            @SerializedName("next")
            val next: Int,
            @SerializedName("page")
            val page: Int,
            @SerializedName("pages")
            val pages: Int,
            @SerializedName("prev")
            val prev: Any,
            @SerializedName("total")
            val total: Int
        )
    }

    @Keep
    data class Post(
        @SerializedName("canonical_url")
        val canonicalUrl: Any,
        @SerializedName("codeinjection_foot")
        val codeinjectionFoot: Any,
        @SerializedName("codeinjection_head")
        val codeinjectionHead: Any,
        @SerializedName("comment_id")
        val commentId: String,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("custom_excerpt")
        val customExcerpt: Any,
        @SerializedName("custom_template")
        val customTemplate: Any,
        @SerializedName("email_subject")
        val emailSubject: Any,
        @SerializedName("excerpt")
        val excerpt: String,
        @SerializedName("feature_image")
        val featureImage: String,
        @SerializedName("featured")
        val featured: Boolean,
        @SerializedName("html")
        val html: String,
        @SerializedName("id")
        val id: String,
        @SerializedName("meta_description")
        val metaDescription: Any,
        @SerializedName("meta_title")
        val metaTitle: Any,
        @SerializedName("og_description")
        val ogDescription: Any,
        @SerializedName("og_image")
        val ogImage: Any,
        @SerializedName("og_title")
        val ogTitle: Any,
        @SerializedName("published_at")
        val publishedAt: String,
        @SerializedName("reading_time")
        val readingTime: Int,
        @SerializedName("send_email_when_published")
        val sendEmailWhenPublished: Boolean,
        @SerializedName("slug")
        val slug: String,
        @SerializedName("title")
        val title: String,
        @SerializedName("twitter_description")
        val twitterDescription: Any,
        @SerializedName("twitter_image")
        val twitterImage: Any,
        @SerializedName("twitter_title")
        val twitterTitle: Any,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("url")
        val url: String,
        @SerializedName("uuid")
        val uuid: String,
        @SerializedName("visibility")
        val visibility: String
    )
}
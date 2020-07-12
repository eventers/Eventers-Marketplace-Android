package com.eventersapp.consumer.data.repositories

import com.eventersapp.consumer.data.model.BlogListResponse
import com.eventersapp.consumer.data.network.BlogApiInterface
import com.eventersapp.consumer.data.network.SafeApiRequest

class BlogRepository(
    private val api: BlogApiInterface
) : SafeApiRequest() {

    suspend fun blog(
        pageNo: Int,
        key: String
    ): BlogListResponse {

        return apiRequest { api.getBlogData(pageNo, key) }
    }


}
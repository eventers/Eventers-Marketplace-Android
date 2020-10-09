package com.eventersapp.marketplace.data.repositories

import com.eventersapp.marketplace.data.model.BlogListResponse
import com.eventersapp.marketplace.data.network.BlogApiInterface
import com.eventersapp.marketplace.data.network.SafeApiRequest

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
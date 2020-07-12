package com.eventersapp.consumer.data.network

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor to add auth token to requests
 */
class AuthInterceptor(context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        requestBuilder.addHeader("User-Agent", "Android")

        return chain.proceed(requestBuilder.build())
    }
}
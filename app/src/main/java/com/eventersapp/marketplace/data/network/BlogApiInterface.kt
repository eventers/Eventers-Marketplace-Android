package com.eventersapp.marketplace.data.network

import com.eventersapp.marketplace.data.model.BlogListResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface BlogApiInterface {

    //<editor-fold desc="Get Requests">
    @GET("ghost/api/v3/content/posts")
    suspend fun getBlogData(
        @Query("page") page: Int,
        @Query("key") key: String
    ): Response<BlogListResponse>

    //</editor-fold>

    companion

    object {
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ): BlogApiInterface {

            val WS_SERVER_URL = "https://eventers.ghost.io/"
            val okkHttpclient = OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor)
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .client(okkHttpclient)
                .baseUrl(WS_SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(BlogApiInterface::class.java)
        }
    }
}
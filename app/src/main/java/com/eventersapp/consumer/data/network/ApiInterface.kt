package com.eventersapp.consumer.data.network

import com.eventersapp.consumer.BuildConfig
import com.eventersapp.consumer.data.model.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface ApiInterface {

    //<editor-fold desc="Put/Patch Requests">
    @PATCH("user/device")
    suspend fun logoutData(@Body body: LogoutPostBody): Response<LogoutResponse>

    @PATCH("public_event")
    suspend fun resellTicketData(@Body body: ResellTicketEventPostBody): Response<ResellTicketEventResponse>

    @PATCH("public_event")
    suspend fun sendTicketData(@Body body: SendTicketEventPostBody): Response<SendTicketEventResponse>

    @PATCH("public_event")
    suspend fun buyNormalTicketData(@Body body: BuyNormalTicketEventPostBody): Response<BuyTicketEventResponse>

    @PATCH("public_event")
    suspend fun buyResellTicketData(@Body body: BuyResellTicketEventPostBody): Response<BuyTicketEventResponse>

    //</editor-fold>

    //<editor-fold desc="Post Requests">
    @POST("user/connect")
    suspend fun connectData(@Body body: ConnectPostBody): Response<ConnectResponse>

    @POST("user/connect/verify")
    suspend fun verifiedNumberData(@Body body: VerifiedNumberPostBody): Response<VerifiedNumberResponse>

    @POST("public_event")
    suspend fun createEventData(@Body body: CreateEventPostBody): Response<CreateEventResponse>

    //</editor-fold>

    //<editor-fold desc="Get Requests">
    @GET("public_event/{id}")
    suspend fun getMyEventData(
        @Path(value = "id") userId: Int
    ): Response<MyEventListResponse>

    @GET("public_event")
    suspend fun getAllEventData(
    ): Response<AllEventListResponse>

    @GET("user/{id}")
    suspend fun getProfileData(
        @Path(value = "id") userId: Int, @Query("token_id") token: String
    ): Response<ProfileResponse>


    //</editor-fold>

    companion

    object {
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor,
            authInterceptor: AuthInterceptor
        ): ApiInterface {

            val WS_SERVER_URL = BuildConfig.end_point
            val okkHttpclient = OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor)
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(authInterceptor)
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .client(okkHttpclient)
                .baseUrl(WS_SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiInterface::class.java)
        }
    }
}

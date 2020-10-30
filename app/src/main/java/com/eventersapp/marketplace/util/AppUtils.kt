package com.eventersapp.marketplace.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import androidx.browser.customtabs.CustomTabsIntent
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.eventersapp.marketplace.R
import com.eventersapp.marketplace.data.model.ConnectResponse
import com.google.firebase.auth.FirebaseAuth
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


object AppUtils {

    private const val DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'"

    fun showProgressBar(requireContext: Context) {
        if (!ProgressBar.getInstance().isDialogShowing()) {
            ProgressBar.getInstance()
                .showProgress(
                    requireContext,
                    false
                )
        }
    }

    fun hideProgressBar() {
        ProgressBar.getInstance().dismissProgress()
    }

    fun getUserPreference(context: Context): ConnectResponse? {
        return SharedPref.getObjectPref(
            context,
            SharedPref.KEY_USER_DATA,
            ConnectResponse::class.java
        )
    }

    fun setGlideImage(image: ImageView, url: String) {
        val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
        Glide.with(image).load(url)
            .placeholder(R.drawable.light_blue_square_rounded_shape_bg)
            .centerCrop()
            .apply(requestOptions)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(image)
    }

    fun setGlideRoundedImage(image: ImageView, url: String) {
        val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)

        Glide.with(image).load(url)
            .thumbnail(0.5f)
            .placeholder(R.drawable.light_blue_oval_shape_bg)
            .apply(requestOptions)
            .apply(RequestOptions().circleCrop())
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(image)
    }

    @SuppressLint("SimpleDateFormat")
    fun getDateTime(): String {
        val df: DateFormat = SimpleDateFormat(DATE_PATTERN)
        val date: String = df.format(Calendar.getInstance().time)
        Log.i("Info", "Date and Time = $date")
        return date
    }

    @SuppressLint("changeDateFormat", "SimpleDateFormat")
    fun changeDateFormat(
        strDate: String?,
        newFormat: String,
        vararg currentPossibleFormats: String
    ): String? {
        var formattedDate = ""
        if (strDate != null) {
            for (currentFormat in currentPossibleFormats) {
                try {
                    val oldFormat = SimpleDateFormat(currentFormat)
                    val date = oldFormat.parse(strDate) ?: ""
                    val dateFormat = SimpleDateFormat(newFormat)
                    formattedDate = dateFormat.format(date)
                    if (formattedDate.isNotBlank()) {
                        break
                    }
                } catch (e: ParseException) {
                    e.printStackTrace()
                    continue
                }
            }
        }
        return if (formattedDate.contains("am"))
            formattedDate.replace("am", "AM")
        else
            formattedDate.replace("pm", "PM")
    }

    fun getFirebaseProvider(auth: FirebaseAuth): String {
        var provider = ""
        for (userInfo in auth.currentUser?.providerData!!) {
            provider = when (userInfo.providerId) {
                "facebook.com" -> AppConstants.FACEBOOK
                "google.com" -> AppConstants.GOOGLE
                else -> AppConstants.PHONE
            }
        }
        return provider
    }

    //decode base64 string to image
    fun base64StringToImage(eventImage: String): Bitmap? {
        val imageBytes = Base64.decode(eventImage, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    fun openCustomChromeTab(context: Context, url: String) {
        val builder: CustomTabsIntent.Builder =
            CustomTabsIntent.Builder()
        val customTabsIntent: CustomTabsIntent = builder.build()
        customTabsIntent.launchUrl(
            context,
            Uri.parse(url)
        )

    }
}
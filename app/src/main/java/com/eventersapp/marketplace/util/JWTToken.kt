package com.eventersapp.marketplace.util

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

suspend fun getFirebaseUserToken(): String = suspendCancellableCoroutine { cont ->
    FirebaseAuth.getInstance().currentUser?.getIdToken(true)?.addOnCompleteListener {
        if (it.isSuccessful) {
            cont.resume(it.result?.token!!)
        } else {
            cont.resumeWithException(it.exception!!.fillInStackTrace())
        }
    }
}
package com.shimmer.store.di


import com.shimmer.store.ui.mainActivity.MainActivity.Companion.PACKAGE_NAME
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.SIGNATURE_NAME
import com.shimmer.store.ui.onBoarding.login.LoginVM.Companion.storeToken
import okhttp3.Interceptor


/**
 * Status Code Interceptor
 * */
object NetworkInterceptor {
    val interceptor = Interceptor { chain ->
        var request = chain.request()
        request = request.newBuilder().apply {
//            header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
//            header("Authorization","Bearer "+ storeToken)
            header("Content-Type", "application/json;charset=utf-8")
            header("User-Agent","Mozilla/5.0")
            header("X-Android-Package", PACKAGE_NAME)
            header("X-Android-Cert", SIGNATURE_NAME)
            method(request.method, request.body)
        }.build()
        val response = chain.proceed(request)
        response
    }

}
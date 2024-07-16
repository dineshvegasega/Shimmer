package com.shimmer.store.ui.onBoarding.splash

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonElement
import com.shimmer.store.ItemSS
import com.shimmer.store.models.BaseResponseDC
import com.shimmer.store.networking.ApiInterface
import com.shimmer.store.networking.CallHandler
import com.shimmer.store.networking.Repository
import com.shimmer.store.networking.getJsonRequestBody
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SplashVM @Inject constructor(private val repository: Repository) : ViewModel() {

//    fun liveNoticeSecond(jsonObject: JSONObject) = viewModelScope.launch {
//        repository.callApi(
//            callHandler = object : CallHandler<Response<JsonElement>> {
//                override suspend fun sendRequest(apiInterface: ApiInterface) =
//                    apiInterface.login(requestBody = jsonObject.getJsonRequestBody())
//                override fun success(response: Response<JsonElement>) {
//                    if (response.isSuccessful){
//                        //itemLiveNoticeResultSecond.value =  response.body() as BaseResponseDC<Any>
//                    }
//
//                    Log.e("TAG", "success: ${response.body()}")
//                }
//
//                override fun error(message: String) {
//                    super.error(message)
////                    showSnackBar(message)
//                }
//
//                override fun loading() {
//                    super.loading()
//                }
//            }
//        )
//    }

    fun adminToken(jsonObject: JSONObject, callBack: String.() -> Unit) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<JsonElement>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.adminToken("mageplaza", "WeLoveMagento123")
                override fun success(response: Response<JsonElement>) {
                    if (response.isSuccessful){
                        callBack(response.body().toString())
                    }
                    Log.e("TAG", "successA: ${response.body()}")
                }

                override fun error(message: String) {
                    super.error(message)
//                    showSnackBar(message)
                    callBack(message.toString())
                    Log.e("TAG", "successB: ${message}")
                }

                override fun loading() {
                    super.loading()
                }
            }
        )
    }


}
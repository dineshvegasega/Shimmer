package com.shimmer.store.ui.main.complaintsFeedback.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonElement
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
class HistoryVM @Inject constructor(private val repository: Repository): ViewModel() {
    val adapter by lazy { HistoryAdapter(this) }


//    private var itemHistoryResult = MutableLiveData<BaseResponseDC<Any>>()
//    val itemHistory : LiveData<BaseResponseDC<Any>> get() = itemHistoryResult
//    fun history(jsonObject: JSONObject) = viewModelScope.launch {
//        repository.callApi(
//            callHandler = object : CallHandler<Response<BaseResponseDC<JsonElement>>> {
//                override suspend fun sendRequest(apiInterface: ApiInterface) =
//                    apiInterface.complaintFeedbackHistory(requestBody = jsonObject.getJsonRequestBody())
//                override fun success(response: Response<BaseResponseDC<JsonElement>>) {
//                    if (response.isSuccessful){
//                        itemHistoryResult.value = response.body() as BaseResponseDC<Any>
//                    }
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
//
//
//
//    private var itemHistoryResultSecond = MutableLiveData<BaseResponseDC<Any>>()
//    val itemHistorySecond : LiveData<BaseResponseDC<Any>> get() = itemHistoryResultSecond
//    fun historySecond(jsonObject: JSONObject) = viewModelScope.launch {
//        repository.callApi(
//            callHandler = object : CallHandler<Response<BaseResponseDC<JsonElement>>> {
//                override suspend fun sendRequest(apiInterface: ApiInterface) =
//                    apiInterface.complaintFeedbackHistory(requestBody = jsonObject.getJsonRequestBody())
//                override fun success(response: Response<BaseResponseDC<JsonElement>>) {
//                    if (response.isSuccessful){
//                        itemHistoryResultSecond.value =  response.body() as BaseResponseDC<Any>
//                    }
//                }
//
//                override fun error(message: String) {
////                    super.error(message)
////                    showSnackBar(message)
//                }
//
//                override fun loading() {
//                    super.loading()
//                }
//            }
//        )
//    }



}
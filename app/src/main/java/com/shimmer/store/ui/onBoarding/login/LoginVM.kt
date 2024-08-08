package com.shimmer.store.ui.onBoarding.login

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.shimmer.store.ItemSS
import com.shimmer.store.R
import com.shimmer.store.datastore.DataStoreKeys.ADMIN_TOKEN
import com.shimmer.store.datastore.DataStoreKeys.LOGIN_DATA
import com.shimmer.store.datastore.DataStoreKeys.STORE_DETAIL
import com.shimmer.store.datastore.DataStoreKeys.WEBSITE_ID
import com.shimmer.store.datastore.DataStoreUtil.saveData
import com.shimmer.store.datastore.DataStoreUtil.saveObject
import com.shimmer.store.models.ItemStore
import com.shimmer.store.networking.ApiInterface
import com.shimmer.store.networking.CallHandler
import com.shimmer.store.networking.Repository
import com.shimmer.store.networking.getJsonRequestBody
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.loginType
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.storeWebUrl
import com.shimmer.store.utils.showSnackBar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LoginVM @Inject constructor(private val repository: Repository) : ViewModel() {

    companion object {
        var storeToken = ""
    }

    fun websiteUrl(adminToken: String, jsonObject: JSONObject, view: View, callBack: String.() -> Unit) =
        viewModelScope.launch {
            repository.callApi(
                callHandler = object : CallHandler<Response<JsonElement>> {
                    override suspend fun sendRequest(apiInterface: ApiInterface) =
                        apiInterface.websiteUrl(jsonObject.getString("username"))
                    override fun success(response: Response<JsonElement>) {
                        if (response.isSuccessful) {
                            try {
//                                val token = response.body().toString()
//                                    .substring(1, response.body().toString().length - 1)
                                  Log.e("TAG", "successAA: ${response.body().toString()}")
                                //val token = response.body().toString().substring(1, response.body().toString().length - 1)
//                                //callBack(token)
//                                storeToken = token

                                val json = JSONObject(response.body().toString())
                                val website_id = json.getString("website_id")
                                saveData(WEBSITE_ID, website_id)
                                storeWebUrl = website_id

                                customerLoginToken(adminToken, jsonObject, view){
                                    Log.e("TAG", "itAAA "+this)
                                }

//                                customerDetail(adminToken, view, callBack)
                            } catch (e: Exception) {
                            }
                        }
                    }

                    override fun error(message: String) {
//                        Log.e("TAG", "successAA: ${message}")
//                        super.error(message)
                        showSnackBar(message)
//                        callBack(message.toString())
                    }

                    override fun loading() {
                        super.loading()
                    }
                }
            )
        }


    fun customerLoginToken(adminToken: String, jsonObject: JSONObject, view: View, callBack: String.() -> Unit) =
        viewModelScope.launch {
            repository.callApi(
                callHandler = object : CallHandler<Response<JsonElement>> {
                    override suspend fun sendRequest(apiInterface: ApiInterface) =
                        apiInterface.customerLoginToken("Bearer " +adminToken, storeWebUrl,  requestBody = jsonObject.getJsonRequestBody())
                    override fun success(response: Response<JsonElement>) {
                        if (response.isSuccessful) {
                            try {
                                val token = response.body().toString()
                                    .substring(1, response.body().toString().length - 1)
                                //  Log.e("TAG", "successAA: ${token}")
                                //callBack(token)
                                storeToken = token
                                customerDetail(token, view, callBack)
                            } catch (e: Exception) {
                            }
                        }
                    }

                    override fun error(message: String) {
//                        Log.e("TAG", "successAA: ${message}")
//                        super.error(message)
                        showSnackBar(message)
//                        callBack(message.toString())
                    }

                    override fun loading() {
                        super.loading()
                    }
                }
            )
        }


    fun customerDetail(token: String, view: View, callBack: String.() -> Unit) =
        viewModelScope.launch {
            repository.callApi(
                callHandler = object : CallHandler<Response<JsonElement>> {
                    override suspend fun sendRequest(apiInterface: ApiInterface) =
                        apiInterface.customerDetail("Bearer " + token, storeWebUrl)

                    override fun success(response: Response<JsonElement>) {
                        if (response.isSuccessful) {
                            try {
//                            val token = response.body().toString().substring(1, response.body().toString().length - 1)
                                Log.e("TAG", "customerDetail: ${response.body().toString()}")
                                //callBack(response.body().toString())

                                saveData(STORE_DETAIL, token)
                                saveObject(
                                    LOGIN_DATA,
                                    Gson().fromJson(
                                        response.body()!!.toString(),
                                        ItemStore::class.java
                                    )
                                )
                                loginType = "vendor"
                                view.findNavController().navigate(R.id.action_login_to_home)
                            } catch (e: Exception) {
                            }
                        }
                    }

                    override fun error(message: String) {
                        super.error(message)
//                    showSnackBar(message)
                        callBack(message.toString())
                    }

                    override fun loading() {
                        super.loading()
                    }
                }
            )
        }


//
//
//    fun customerDetail(jsonObject: JSONObject) = viewModelScope.launch {
//        repository.callApi(
//            callHandler = object : CallHandler<Response<JsonElement>> {
//                override suspend fun sendRequest(apiInterface: ApiInterface) =
//                    apiInterface.adminToken(requestBody = jsonObject.getJsonRequestBody())
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
//
//                    Log.e("TAG", "success: ${message}")
//                }
//
//                override fun loading() {
//                    super.loading()
//                }
//            }
//        )
//    }
}
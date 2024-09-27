package com.shimmer.store.ui.onBoarding.forgotPassword

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonElement
import com.shimmer.store.datastore.DataStoreKeys.WEBSITE_ID
import com.shimmer.store.datastore.DataStoreUtil.saveData
import com.shimmer.store.networking.ApiInterface
import com.shimmer.store.networking.CallHandler
import com.shimmer.store.networking.Repository
import com.shimmer.store.networking.getJsonRequestBody
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.storeWebUrl
import com.shimmer.store.utils.showSnackBar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordVM @Inject constructor(private val repository: Repository) : ViewModel() {


    fun adminToken(jsonObject: JSONObject, callBack: String.() -> Unit) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<JsonElement>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.adminToken(requestBody = jsonObject.getJsonRequestBody())
                override fun success(response: Response<JsonElement>) {
                    if (response.isSuccessful){
//                        Log.e("TAG", "successAA: ${response.body().toString()}")
                        try {
                            val token = response.body().toString().substring(1, response.body().toString().length - 1)
                            callBack(token)
                        }catch (e : Exception){
                        }
                    }
                }

                override fun error(message: String) {
                    super.error(message)
//                    showSnackBar(message)
                    Log.e("TAG", "successBB: ${message.toString()}")
                    callBack(message.toString())
                }

                override fun loading() {
                    super.loading()
                }
            }
        )
    }


    fun websiteUrl(adminToken: String, jsonObject: JSONObject, callBack: String.() -> Unit) =
        viewModelScope.launch {
            repository.callApi(
                callHandler = object : CallHandler<Response<JsonElement>> {
                    override suspend fun sendRequest(apiInterface: ApiInterface) =
                        apiInterface.websiteUrl(jsonObject.getString("emailmobile"))
                    override fun success(response: Response<JsonElement>) {
                        if (response.isSuccessful) {
                            try {
//                                val token = response.body().toString()
//                                    .substring(1, response.body().toString().length - 1)
                                Log.e("TAG", "successAA: ${response.body().toString()}")
                                //val token = response.body().toString().substring(1, response.body().toString().length - 1)

//                                storeToken = token

                                val json = JSONObject(response.body().toString())
                                val website_id = json.getString("website_id")
                                saveData(WEBSITE_ID, website_id)
                                storeWebUrl = website_id
                                callBack(response.body().toString())
                            } catch (e: Exception) {
                            }
                        }
                    }

                    override fun error(message: String) {
                        showSnackBar(message)
                    }

                    override fun loading() {
                        super.loading()
                    }
                }
            )
        }


    fun sendOTP(adminToken: String, jsonObject: JSONObject, callBack: String.() -> Unit) =
        viewModelScope.launch {
            repository.callApi(
                callHandler = object : CallHandler<Response<JsonElement>> {
                    override suspend fun sendRequest(apiInterface: ApiInterface) =
                        apiInterface.sendOTP(storeWebUrl, requestBody = jsonObject.getJsonRequestBody())
                    override fun success(response: Response<JsonElement>) {
                        if (response.isSuccessful) {
                            try {
//                                val token = response.body().toString()
//                                    .substring(1, response.body().toString().length - 1)
                                Log.e("TAG", "successAA: ${response.body().toString()}")
                                val token = response.body().toString().substring(1, response.body().toString().length - 1).toString().replace("\\" ,"")
                                val json = JSONObject(token)
                                if (json.has("status")){
                                    showSnackBar("Something went wrong!")
                                    callBack(response.body().toString())
                                } else {
                                    val successmsg = json.getString("successmsg")
                                    showSnackBar(successmsg)
                                    callBack(response.body().toString())
                                }
                            } catch (e: Exception) {
                            }
                        }
                    }

                    override fun error(message: String) {
                        showSnackBar(message)
                    }

                    override fun loading() {
                        super.loading()
                    }
                }
            )
        }



    fun verifyOTP(adminToken: String, jsonObject: JSONObject, callBack: String.() -> Unit) =
        viewModelScope.launch {
            repository.callApi(
                callHandler = object : CallHandler<Response<JsonElement>> {
                    override suspend fun sendRequest(apiInterface: ApiInterface) =
                        apiInterface.verifyOTP(storeWebUrl, requestBody = jsonObject.getJsonRequestBody())
                    override fun success(response: Response<JsonElement>) {
                        if (response.isSuccessful) {
                            try {
                                val token = response.body().toString().substring(1, response.body().toString().length - 1).toString().replace("\\" ,"")
                                val json = JSONObject(token)
                                if (json.has("status")){
                                    val errormsg = json.getString("errormsg")
                                    showSnackBar(errormsg)
                                } else {
                                    if (json.has("success")){
                                        val success = json.getString("success")
                                        if (success == "true"){
                                            val successmsg = json.getString("successmsg")
                                            showSnackBar(successmsg)
                                            callBack(response.body().toString())
                                        } else {
                                            val errormsg = json.getString("errormsg")
                                            showSnackBar(errormsg)
                                        }
                                    }

                                }
                            } catch (e: Exception) {
                            }
                        }
                    }

                    override fun error(message: String) {
                        showSnackBar(message)
                    }

                    override fun loading() {
                        super.loading()
                    }
                }
            )
        }
}
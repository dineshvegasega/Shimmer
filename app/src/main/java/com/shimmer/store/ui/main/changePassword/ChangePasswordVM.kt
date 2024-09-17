package com.shimmer.store.ui.main.changePassword

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonElement
import com.shimmer.store.datastore.db.CartModel
import com.shimmer.store.networking.ApiInterface
import com.shimmer.store.networking.CallHandler
import com.shimmer.store.networking.Repository
import com.shimmer.store.networking.getJsonRequestBody
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.db
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.storeWebUrl
import com.shimmer.store.ui.onBoarding.login.LoginVM.Companion.storeToken
import com.shimmer.store.utils.showSnackBar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ChangePasswordVM @Inject constructor(private val repository: Repository) : ViewModel() {

    fun getCartCount(callBack: Int.() -> Unit){
        viewModelScope.launch {
            val userList: List<CartModel> ?= db?.cartDao()?.getAll()
            var countBadge = 0
            userList?.forEach {
                countBadge += it.quantity
            }
            callBack(countBadge)
        }
    }





    fun customerLoginToken(
        adminToken: String,
        jsonObject: JSONObject,
        callBack: String.() -> Unit
    ) =
        viewModelScope.launch {
            repository.callApi(
                callHandler = object : CallHandler<Response<JsonElement>> {
                    override suspend fun sendRequest(apiInterface: ApiInterface) =
                        apiInterface.customerLoginToken(
                            "Bearer " + adminToken,
                            storeWebUrl,
                            requestBody = jsonObject.getJsonRequestBody()
                        )

                    @SuppressLint("SuspiciousIndentation")
                    override fun success(response: Response<JsonElement>) {
                        if (response.isSuccessful) {
                            try {
                                val jsonObject = response.body().toString()
                                    .substring(1, response.body().toString().length - 1).toString()
                                    .replace("\\", "")
                                Log.e("TAG", "successAAB: ${jsonObject}")
                                if (JSONObject(jsonObject).getString("success") == "true"){
                                    storeToken = JSONObject(jsonObject).getString("token")
                                    callBack(storeToken)
                                } else {
                                    showSnackBar("Invalid Credentials")
                                }
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






    fun resetPassword(adminToken: String, jsonObject: JSONObject, callBack: String.() -> Unit) =
        viewModelScope.launch {
            repository.callApi(
                callHandler = object : CallHandler<Response<JsonElement>> {
                    override suspend fun sendRequest(apiInterface: ApiInterface) =
                        apiInterface.customerResetPassword("Bearer " +adminToken, storeWebUrl,  requestBody = jsonObject.getJsonRequestBody())
                    override fun success(response: Response<JsonElement>) {
                        if (response.isSuccessful) {
                            try {
                                val jsonObject = response.body().toString().substring(1, response.body().toString().length - 1).toString().replace("\\", "")
                                Log.e("TAG", "successAAB: ${jsonObject}")
                                callBack(jsonObject)
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
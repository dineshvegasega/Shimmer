package com.shimmer.store.ui.onBoarding.resetPassword

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonElement
import com.shimmer.store.databinding.LoaderBinding
import com.shimmer.store.datastore.DataStoreKeys.WEBSITE_ID
import com.shimmer.store.datastore.DataStoreUtil.saveData
import com.shimmer.store.networking.ApiInterface
import com.shimmer.store.networking.CallHandler
import com.shimmer.store.networking.Repository
import com.shimmer.store.networking.getJsonRequestBody
import com.shimmer.store.ui.mainActivity.MainActivity
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.storeWebUrl
import com.shimmer.store.ui.onBoarding.login.LoginVM.Companion.storeToken
import com.shimmer.store.utils.mainThread
import com.shimmer.store.utils.showSnackBar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ResetPasswordVM @Inject constructor(private val repository: Repository) : ViewModel() {

    var alertDialog: AlertDialog? = null
    init {
        val alert = AlertDialog.Builder(MainActivity.activity.get())
        val binding =
            LoaderBinding.inflate(LayoutInflater.from(MainActivity.activity.get()), null, false)
        alert.setView(binding.root.rootView)
        alert.setCancelable(false)
        alertDialog = alert.create()
        alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun show() {
        mainThread {
            if (alertDialog != null) {
                alertDialog?.dismiss()
                alertDialog?.show()
            }
        }
    }

    fun hide() {
        mainThread {
            if (alertDialog != null) {
                alertDialog?.dismiss()
            }
        }
    }


    fun adminToken(jsonObject: JSONObject, callBack: String.() -> Unit) = viewModelScope.launch {
        repository.callApiWithoutLoader(
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
            repository.callApiWithoutLoader(
                callHandler = object : CallHandler<Response<JsonElement>> {
                    override suspend fun sendRequest(apiInterface: ApiInterface) =
                        apiInterface.websiteUrl(jsonObject.getString("mobilenumber"))
                    override fun success(response: Response<JsonElement>) {
                        if (response.isSuccessful) {
                            try {
                                Log.e("TAG", "successAA: ${response.body().toString()}")
                                val json = JSONObject(response.body().toString())
                                val status = json.getString("status")
                                if (status == "true") {
                                    val website_id = json.getString("website_id")
                                    saveData(WEBSITE_ID, website_id)
                                    storeWebUrl = website_id
                                    callBack(response.body().toString())
                                } else {
                                    hide()
                                    showSnackBar("Invalid Credentials")
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


    fun resetPassword(adminToken: String, jsonObject: JSONObject, callBack: String.() -> Unit) =
        viewModelScope.launch {
            repository.callApiWithoutLoader(
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
package com.shimmer.store.ui.main.searchOrder

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.shimmer.store.R
import com.shimmer.store.databinding.ItemSearchOrderBinding
import com.shimmer.store.datastore.db.SearchModel
import com.shimmer.store.genericAdapter.GenericAdapter
import com.shimmer.store.models.myOrdersDetail.ItemOrderDetail
import com.shimmer.store.networking.ApiInterface
import com.shimmer.store.networking.CallHandler
import com.shimmer.store.networking.Repository
import com.shimmer.store.utils.changeDateFormat
import com.shimmer.store.utils.getPatternFormat
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SearchOrderVM @Inject constructor(private val repository: Repository) : ViewModel() {

    val searchOrderAdapter = object : GenericAdapter<ItemSearchOrderBinding, SearchOrderModel>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemSearchOrderBinding.inflate(inflater, parent, false)

        override fun onBindHolder(
            binding: ItemSearchOrderBinding,
            dataClass: SearchOrderModel,
            position: Int
        ) {
            binding.apply {
                //textItem.text = dataClass.search_name
//                textDesc.visibility =
//                    if (dataClass.isCollapse == true) View.VISIBLE else View.GONE
//                ivHideShow.setImageDrawable(
//                    ContextCompat.getDrawable(
//                        root.context,
//                        if (dataClass.isCollapse == true) R.drawable.baseline_remove_24 else R.drawable.baseline_add_24
//                    )
//                )


                textTitle.text = "Order No: " + dataClass.orderNo
                textNoOfOrders.text = "No Of Products: " + dataClass.noofItems
                val date =
                    dataClass.dateTime.changeDateFormat("yyyy-MM-dd HH:mm:ss", "dd-MMM-yyyy")
                textDate.text = "Date: " + date
                textAmount.text =
                    "Total Amount: ₹ " + getPatternFormat("1", dataClass.base_subtotal)

                root.singleClick {
                    root.findNavController()
                        .navigate(R.id.action_searchOrder_to_orderDetail, Bundle().apply {
                            putString("from", "orderHistory")
                            putString("_id", "" + dataClass.orderNo)
                        })
                }


//
//                ivCross.singleClick {
//                    mainThread {
//                        db?.searchDao()?.delete(dataClass)
//                        searchDelete.value = true
//                    }
//                }
            }
        }
    }


    fun orderHistoryListDetail(
        adminToken: String,
        id: String,
        callBack: ItemOrderDetail.() -> Unit
    ) =
        viewModelScope.launch {
            repository.callApi(
                callHandler = object : CallHandler<Response<ItemOrderDetail>> {
                    override suspend fun sendRequest(apiInterface: ApiInterface) =
                        apiInterface.orderHistoryListDetail("Bearer " + adminToken, id)

                    @SuppressLint("SuspiciousIndentation")
                    override fun success(response: Response<ItemOrderDetail>) {
                        if (response.isSuccessful) {
                            try {
                                Log.e("TAG", "successAA: ${response.body().toString()}")
//                                val jsonObject = response.body().toString().substring(1, response.body().toString().length - 1).toString().replace("\\", "")
//                                Log.e("TAG", "successAAB: ${jsonObject}")
                                callBack(response.body()!!)
                            } catch (e: Exception) {
                            }
                        }
                    }

                    override fun error(message: String) {
                        Log.e("TAG", "successEE: ${message}")
//                        super.error(message)
//                        showSnackBar(message)
                    }

                    override fun loading() {
                        super.loading()
                    }
                }
            )
        }

//
//    fun orderHistoryListDetail(id: String, callBack: JsonElement.() -> Unit) =
//        viewModelScope.launch {
//            repository.callApi(
//                callHandler = object : CallHandler<Response<JsonElement>> {
//                    override suspend fun sendRequest(apiInterface: ApiInterface) =
//                        apiInterface.orderHistoryDetail(id)
//                    @SuppressLint("SuspiciousIndentation")
//                    override fun success(response: Response<JsonElement>) {
//                        if (response.isSuccessful) {
//                            try {
//                                Log.e("TAG", "successAA: ${response.body().toString()}")
//                                val jsonObject = response.body().toString().substring(1, response.body().toString().length - 1).toString().replace("\\n", "").replace("\\", "").trim()
//                                Log.e("TAG", "successAAB: ${jsonObject}")
////                                val item = Gson().fromJson<>(response.body()?.get(0))
//
//                                var ddd = JSONObject(jsonObject).getString("checkout_buyer_email")
//                                Log.e("TAG", "jsonObjectAA: ${ddd}")
////                                val jsonObjectAA = Gson().fromJson(Gson().toJson(jsonObject), ItemOrderDetail::class.java)
////                                Log.e("TAG", "jsonObjectAA: ${jsonObjectAA}")
//                                callBack(response.body()!!)
//                            } catch (e: Exception) {
//                            }
//                        }
//                    }
//
//                    override fun error(message: String) {
//                        Log.e("TAG", "successEE: ${message}")
////                        super.error(message)
////                        showSnackBar(message)
//                    }
//
//                    override fun loading() {
//                        super.loading()
//                    }
//                }
//            )
//        }


}
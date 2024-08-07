package com.shimmer.store.ui.main.cart

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.shimmer.store.R
import com.shimmer.store.databinding.ItemCartBinding
import com.shimmer.store.datastore.DataStoreKeys.CUSTOMER_TOKEN
import com.shimmer.store.datastore.DataStoreUtil.readData
import com.shimmer.store.datastore.db.CartModel
import com.shimmer.store.genericAdapter.GenericAdapter
import com.shimmer.store.models.cart.ItemCart
import com.shimmer.store.models.cart.ItemCartModel
import com.shimmer.store.models.products.ItemProductRoot
import com.shimmer.store.networking.ApiInterface
import com.shimmer.store.networking.CallHandler
import com.shimmer.store.networking.Repository
import com.shimmer.store.networking.getJsonRequestBody
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.db
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.storeWebUrl
import com.shimmer.store.utils.getPatternFormat
import com.shimmer.store.utils.mainThread
import com.shimmer.store.utils.sessionExpired
import com.shimmer.store.utils.showSnackBar
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CartVM @Inject constructor(private val repository: Repository) : ViewModel() {

    var quoteId : String = ""

//    var refreshPrice = MutableLiveData<Boolean>(false)
    var subTotalPrice : Double = 0.0
    var discountPrice : Double = 10.0
    var shippingPrice : Double = 100.0
    var cgstPrice : Double = 9.0
    var sgstPrice : Double = 9.0
    var totalPrice : Double = 0.0

    var cartMutableList = MutableLiveData<Boolean>(false)
//    val cartAdapter = object : GenericAdapter<ItemCartBinding, CartModel>() {
//        override fun onCreateView(
//            inflater: LayoutInflater,
//            parent: ViewGroup,
//            viewType: Int
//        ) = ItemCartBinding.inflate(inflater, parent, false)
//
//        @SuppressLint("NotifyDataSetChanged")
//        override fun onBindHolder(
//            binding: ItemCartBinding,
//            dataClass: CartModel,
//            position: Int
//        ) {
//            binding.apply {
//
//                ivIcon.singleClick {
//                    binding.root.findNavController().navigate(R.id.action_cart_to_productDetail, Bundle().apply {
//                        putString("sku", dataClass.sku)
//                    })
//                }
//
//                textTitle.text = dataClass.name
//                textDesc.text =  "SKU: "+dataClass.sku
//
//                ivCount.text = dataClass.quantity.toString()
//                textPrice.text = "Price: ₹"+getPatternFormat("1", dataClass.price!!)
//
//                ivMinus.singleClick {
//                    if (dataClass.quantity > 1) {
//                        dataClass.quantity--
//                        mainThread {
//                            db?.cartDao()?.updateById(dataClass.product_id!!, dataClass.quantity)
//                            val userList: List<CartModel> ?= db?.cartDao()?.getAll()
//                            userList?.forEach {
//                                Log.e("TAG", "onViewCreated: "+it.name + " it.currentTime "+it.quantity)
//                            }
//                        }
//                        notifyItemChanged(position)
//                    }
//                    cartMutableList.value = true
//                }
//
//                ivPlus.singleClick {
//                    dataClass.quantity++
//                    mainThread {
//                        db?.cartDao()?.updateById(dataClass.product_id!!, dataClass.quantity)
//                        val userList: List<CartModel> ?= db?.cartDao()?.getAll()
//                        userList?.forEach {
//                            Log.e("TAG", "onViewCreated: "+it.name + " it.currentTime "+it.quantity)
//                        }
//                    }
//                    notifyItemChanged(position)
//                    cartMutableList.value = true
//                }
//
//                btDelete.singleClick {
//                    mainThread {
//                        db?.cartDao()?.deleteById(dataClass.product_id!!)
////                        val userList: List<CartModel> ?= db?.cartDao()?.getAll()
////                        userList?.forEach {
////                            Log.e("TAG", "onViewCreated: "+it.name + " it.currentTime "+it.quantity)
////                        }
//                    }
////                    Log.e("TAG", "onViewCreated: "+position)
////
////                    item1.removeAt(position)
////                    notifyDataSetChanged()
//
//                    cartMutableList.value = true
//
//                }
//            }
//        }
//    }


    val cartAdapter = object : GenericAdapter<ItemCartBinding, ItemCartModel>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemCartBinding.inflate(inflater, parent, false)

        @SuppressLint("NotifyDataSetChanged")
        override fun onBindHolder(
            binding: ItemCartBinding,
            dataClass: ItemCartModel,
            position: Int
        ) {
            binding.apply {
                ivIcon.singleClick {
                    binding.root.findNavController().navigate(R.id.action_cart_to_productDetail, Bundle().apply {
                        putString("sku", dataClass.sku)
                    })
                }

                textTitle.text = dataClass.name
                textDesc.text =  "SKU: "+dataClass.sku

                ivCount.text = dataClass.qty.toString()
                textPrice.text = "Price: ₹"+getPatternFormat("1", dataClass.price!!)

                ivMinus.singleClick {
                    if (dataClass.qty > 1) {
                        dataClass.qty--
                        mainThread {
                            val json: JSONObject = JSONObject().apply {
                                put("sku", dataClass.sku)
                                put("qty", 1)
                                put("quote_id", quoteId)
                            }
                            val jsonCartItem: JSONObject = JSONObject().apply {
                                put("cartItem", json)
                            }
                            readData(CUSTOMER_TOKEN) { token ->
                                addCart(token!!, jsonCartItem) {
                                    notifyItemChanged(position)
                                    cartMutableList.value = true
                                }
                            }
                        }
//                        mainThread {
//                            db?.cartDao()?.updateById(dataClass.product_id!!, dataClass.quantity)
//                            val userList: List<CartModel> ?= db?.cartDao()?.getAll()
//                            userList?.forEach {
//                                Log.e("TAG", "onViewCreated: "+it.name + " it.currentTime "+it.quantity)
//                            }
//                        }
//                        notifyItemChanged(position)
                    }
//                    cartMutableList.value = true
                }

                ivPlus.singleClick {
                    dataClass.qty++
                    mainThread {
                        val json : JSONObject = JSONObject().apply {
                            put("sku", dataClass.sku)
                            put("qty", 1)
                            put("quote_id", quoteId)
                        }
                        val jsonCartItem : JSONObject = JSONObject().apply {
                            put("cartItem", json)
                        }
                        readData(CUSTOMER_TOKEN) { token ->
                            addCart(token!!, jsonCartItem){
                                notifyItemChanged(position)
                                cartMutableList.value = true
                            }
                        }


//                        db?.cartDao()?.updateById(dataClass.product_id!!, dataClass.qty)
//                        val userList: List<CartModel> ?= db?.cartDao()?.getAll()
//                        userList?.forEach {
//                            Log.e("TAG", "onViewCreated: "+it.name + " it.currentTime "+it.quantity)
//                        }
                    }

                }

                btDelete.singleClick {
//                    mainThread {
//                        db?.cartDao()?.deleteById(dataClass.product_id!!)
////                        val userList: List<CartModel> ?= db?.cartDao()?.getAll()
////                        userList?.forEach {
////                            Log.e("TAG", "onViewCreated: "+it.name + " it.currentTime "+it.quantity)
////                        }
//                    }
//                    Log.e("TAG", "onViewCreated: "+position)
//
//                    item1.removeAt(position)
//                    notifyDataSetChanged()

//                    cartMutableList.value = true


                    mainThread {
                        val json: JSONObject = JSONObject().apply {
                            put("sku", dataClass.sku)
                            put("qty", 1)
                            put("quote_id", quoteId)
                        }
                        val jsonCartItem: JSONObject = JSONObject().apply {
                            put("cartItem", json)
                        }
                        readData(CUSTOMER_TOKEN) { token ->
                            addCart(token!!, jsonCartItem) {
                                notifyItemChanged(position)
                                cartMutableList.value = true
                            }
                        }
                    }
                }
            }
        }
    }



    fun getQuoteId(adminToken: String, jsonObject: JSONObject, callBack: String.() -> Unit) =
        viewModelScope.launch {
            repository.callApi(
                callHandler = object : CallHandler<Response<JsonElement>> {
                    override suspend fun sendRequest(apiInterface: ApiInterface) =
//                        if (loginType == "vendor") {
                            apiInterface.getQuoteId("Bearer " +adminToken, storeWebUrl, requestBody = jsonObject.getJsonRequestBody())
//                        } else if (loginType == "guest") {
//                        apiInterface.getQuoteId("Bearer " +adminToken, emptyMap)
                    //                        } else {
//                            apiInterface.products("Bearer " +adminToken, storeWebUrl, emptyMap)
//                        }
                    @SuppressLint("SuspiciousIndentation")
                    override fun success(response: Response<JsonElement>) {
                        if (response.isSuccessful) {
                            try {
                                Log.e("TAG", "successAAXX: ${response.body().toString()}")
                                callBack(response.body().toString())
                            } catch (_: Exception) {
                            }
                        }
                    }

                    override fun error(message: String) {
                        if(message.contains("fieldName")){
                            showSnackBar("Something went wrong!")
                        } else {
                            sessionExpired()
                        }
                    }

                    override fun loading() {
                        super.loading()
                    }
                }
            )
        }


    fun getCart(customerToken: String, callBack: ItemCart.() -> Unit) =
        viewModelScope.launch {
            repository.callApi(
                callHandler = object : CallHandler<Response<ItemCart>> {
                    override suspend fun sendRequest(apiInterface: ApiInterface) =
//                        if (loginType == "vendor") {
                        apiInterface.getCart("Bearer " +customerToken, storeWebUrl)
                    //                        } else if (loginType == "guest") {
//                        apiInterface.getQuoteId("Bearer " +adminToken, emptyMap)
                    //                        } else {
//                            apiInterface.products("Bearer " +adminToken, storeWebUrl, emptyMap)
//                        }
                    @SuppressLint("SuspiciousIndentation")
                    override fun success(response: Response<ItemCart>) {
                        if (response.isSuccessful) {
                            try {
                                Log.e("TAG", "successAAXX: ${response.body().toString()}")
                                callBack(response.body()!!)
                            } catch (_: Exception) {
                            }
                        }
                    }

                    override fun error(message: String) {
                        if(message.contains("fieldName")){
                            showSnackBar("Something went wrong!")
                        } else {
                            sessionExpired()
                        }
                    }

                    override fun loading() {
                        super.loading()
                    }
                }
            )
        }


    fun addCart(adminToken: String, jsonObject: JSONObject, callBack: ItemCartModel.() -> Unit) =
        viewModelScope.launch {
            repository.callApi(
                callHandler = object : CallHandler<Response<ItemCartModel>> {
                    override suspend fun sendRequest(apiInterface: ApiInterface) =
//                        if (loginType == "vendor") {
                        apiInterface.addCart("Bearer " +adminToken, storeWebUrl, requestBody = jsonObject.getJsonRequestBody())
                    //                        } else if (loginType == "guest") {
//                        apiInterface.getQuoteId("Bearer " +adminToken, emptyMap)
                    //                        } else {
//                            apiInterface.products("Bearer " +adminToken, storeWebUrl, emptyMap)
//                        }
                    @SuppressLint("SuspiciousIndentation")
                    override fun success(response: Response<ItemCartModel>) {
                        if (response.isSuccessful) {
                            try {
                                Log.e("TAG", "successAAXX: ${response.body().toString()}")
                                callBack(response.body()!!)
                            } catch (_: Exception) {
                            }
                        }
                    }

                    override fun error(message: String) {
                        if(message.contains("fieldName")){
                            showSnackBar("Something went wrong!")
                        } else {
                            sessionExpired()
                        }
                    }

                    override fun loading() {
                        super.loading()
                    }
                }
            )
        }

}
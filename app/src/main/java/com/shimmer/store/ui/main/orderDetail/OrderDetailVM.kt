package com.shimmer.store.ui.main.orderDetail

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.shimmer.store.R
import com.shimmer.store.databinding.ItemOrderHistoryBinding
import com.shimmer.store.databinding.ItemSkusBinding
import com.shimmer.store.datastore.DataStoreKeys.ADMIN_TOKEN
import com.shimmer.store.datastore.DataStoreUtil.readData
import com.shimmer.store.datastore.db.CartModel
import com.shimmer.store.genericAdapter.GenericAdapter
import com.shimmer.store.models.Items
import com.shimmer.store.models.myOrdersDetail.ItemOrderDetail
import com.shimmer.store.models.myOrdersDetail.ItemX
import com.shimmer.store.models.orderHistory.ItemXX
import com.shimmer.store.models.products.ItemProduct
import com.shimmer.store.networking.ApiInterface
import com.shimmer.store.networking.CallHandler
import com.shimmer.store.networking.IMAGE_URL
import com.shimmer.store.networking.Repository
import com.shimmer.store.networking.getJsonRequestBody
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.db
import com.shimmer.store.utils.glideImageChache
import com.shimmer.store.utils.mainThread
import com.shimmer.store.utils.showSnackBar
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class OrderDetailVM @Inject constructor(private val repository: Repository) : ViewModel() {

    var selectedPositionCustomerOrders = -1
    val orderSKUCustomerOrders = object : GenericAdapter<ItemSkusBinding, CartItem>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemSkusBinding.inflate(inflater, parent, false)

        @SuppressLint("NotifyDataSetChanged")
        override fun onBindHolder(
            binding: ItemSkusBinding,
            dataClass: CartItem,
            position: Int
        ) {
            binding.apply {

                textTitle.text = "SKU : ${dataClass.sku}"

                mainThread {
                    readData(ADMIN_TOKEN) { token ->
                        Log.e("TAG", "tokenOO: "+token)
                        getProductDetail(token.toString(), dataClass.sku) {
                            Log.e("TAG", "getProductDetailOO: "+this.name)
                            if (this.media_gallery_entries.size > 0){
                                (IMAGE_URL +this.media_gallery_entries[0].file).glideImageChache(binding.ivIcon.context, binding.ivIcon)
//                                Glide.with(binding.ivIcon.context)
//                                    .load(IMAGE_URL +this.media_gallery_entries[0].file)
//                                    .apply(myOptionsGlide)
//                                    .into(binding.ivIcon)
                                (IMAGE_URL +this.media_gallery_entries[0].file).glideImageChache(binding.ivIcon.context, binding.ivIconChild)
                            }
                        }
                    }
                }


                skuChild.text = "${dataClass.sku}"
                textQtyChild.text = "${dataClass.qty}"
                textPriceChild.text = "₹${dataClass.price}"
                textTotalPriceChild.text = "₹${dataClass.price * dataClass.qty}"

                layoutChild.visibility =
                    if (selectedPositionCustomerOrders == position) View.VISIBLE else View.GONE
                imageCross.setImageDrawable(
                    ContextCompat.getDrawable(
                        root.context,
                        if (selectedPositionCustomerOrders == position) R.drawable.arrow_down else R.drawable.arrow_right
                    )
                )

                imageCross.singleClick {

                    if (selectedPositionCustomerOrders == position){
                        if (layoutChild.isVisible == true){
                            selectedPositionCustomerOrders = -1
                        }
                        if (layoutChild.isVisible == false){
                            selectedPositionCustomerOrders = position
                        }
                    } else{
                        selectedPositionCustomerOrders = position
                    }
                    notifyDataSetChanged()
                }
            }
        }
    }


    var selectedPositionOrderHistory = -1
    val orderSKUOrderHistory = object : GenericAdapter<ItemSkusBinding, ItemX>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemSkusBinding.inflate(inflater, parent, false)

        @SuppressLint("NotifyDataSetChanged")
        override fun onBindHolder(
            binding: ItemSkusBinding,
            dataClass: ItemX,
            position: Int
        ) {
            binding.apply {

                textTitle.text = "SKU : ${dataClass.sku}"

                mainThread {
                    readData(ADMIN_TOKEN) { token ->
                        Log.e("TAG", "tokenOO: "+token)
                        getProductDetail(token.toString(), dataClass.sku) {
                            Log.e("TAG", "getProductDetailOO: "+this.name)
                            if (this.media_gallery_entries.size > 0){
                                (IMAGE_URL +this.media_gallery_entries[0].file).glideImageChache(binding.ivIcon.context, binding.ivIcon)
                                (IMAGE_URL +this.media_gallery_entries[0].file).glideImageChache(binding.ivIcon.context, binding.ivIconChild)
                            }
                        }
                    }
                }

                skuChild.text = "${dataClass.sku}"
                textQtyChild.text = "${dataClass.qty_ordered}"
                textPriceChild.text = "₹${dataClass.price}"
                textTotalPriceChild.text = "₹${dataClass.price * dataClass.qty_ordered!!}"

                layoutChild.visibility =
                    if (selectedPositionOrderHistory == position) View.VISIBLE else View.GONE
                imageCross.setImageDrawable(
                    ContextCompat.getDrawable(
                        root.context,
                        if (selectedPositionOrderHistory == position) R.drawable.arrow_down else R.drawable.arrow_right
                    )
                )

                imageCross.singleClick {
                    if (selectedPositionOrderHistory == position) {
                        if (layoutChild.isVisible == true) {
                            selectedPositionOrderHistory = -1
                        }
                        if (layoutChild.isVisible == false) {
                            selectedPositionOrderHistory = position
                        }
                    } else {
                        selectedPositionOrderHistory = position
                    }
                    notifyDataSetChanged()
                }
            }
        }
    }





    fun getProductDetail(adminToken: String, skuId: String, callBack: ItemProduct.() -> Unit) =
        viewModelScope.launch {
            repository.callApiWithoutLoader(
                callHandler = object : CallHandler<Response<JsonElement>> {
                    override suspend fun sendRequest(apiInterface: ApiInterface) =
//                    if (loginType == "vendor") {
//                        apiInterface.productsDetail("Bearer " +adminToken, storeWebUrl, skuId)
//                    } else if (loginType == "guest") {
                        apiInterface.productsDetailID("Bearer " +adminToken, skuId)
                    //                    } else {
//                        apiInterface.productsDetail("Bearer " +adminToken, storeWebUrl, skuId)
//                    }
                    @SuppressLint("SuspiciousIndentation")
                    override fun success(response: Response<JsonElement>) {
                        if (response.isSuccessful) {
                            try {
                                Log.e("TAG", "successAA: ${response.body().toString()}")
                                val mMineUserEntity = Gson().fromJson(response.body(), ItemProduct::class.java)

                                viewModelScope.launch {
//                                    mMineUserEntity.forEach {items ->
                                    val userList: List<CartModel>? = db?.cartDao()?.getAll()
                                    userList?.forEach { user ->
                                        if (mMineUserEntity.id == user.product_id) {
                                            mMineUserEntity.apply {
                                                isSelected = true
                                            }
//                                                Log.e( "TAG", "YYYYYYYYY: " )
                                        } else {
                                            mMineUserEntity.apply {
                                                isSelected = false
                                            }
//                                                Log.e( "TAG", "NNNNNNNNNN: " )
                                        }
                                    }
//                                    }


                                    callBack(mMineUserEntity)
                                }


                            } catch (e: Exception) {
                            }
                        }
                    }

                    override fun error(message: String) {
                        Log.e("TAG", "successEE: ${message}")
//                        super.error(message)
//                        showSnackBar(message)
//                        callBack(message.toString())

//                        if(message.contains("fieldName")){
//                            showSnackBar("Something went wrong!")
//                        } else if(message.contains("The product that was requested doesn't exist")){
//                            showSnackBar(message)
//                        } else {
//                            sessionExpired()
//                        }

                    }

                    override fun loading() {
                        super.loading()
                    }
                }
            )
        }




    fun updateStatus(jsonObject: JSONObject, callBack: JsonElement.() -> Unit) =
        viewModelScope.launch {
            repository.callApi(
                callHandler = object : CallHandler<Response<JsonElement>> {
                    override suspend fun sendRequest(apiInterface: ApiInterface) =
                        apiInterface.updateStatus(requestBody = jsonObject.getJsonRequestBody())
                    @SuppressLint("SuspiciousIndentation")
                    override fun success(response: Response<JsonElement>) {
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
                            //sessionExpired()
                        }
                    }

                    override fun loading() {
                        super.loading()
                    }
                }
            )
        }




    fun orderHistoryListDetail(adminToken: String, id: String, callBack: ItemOrderDetail.() -> Unit) =
        viewModelScope.launch {
            repository.callApi(
                callHandler = object : CallHandler<Response<ItemOrderDetail>> {
                    override suspend fun sendRequest(apiInterface: ApiInterface) =
                        apiInterface.orderHistoryListDetail("Bearer " +adminToken, id)
                    @SuppressLint("SuspiciousIndentation")
                    override fun success(response: Response<ItemOrderDetail>) {
                        if (response.isSuccessful) {
                            try {
                                Log.e("TAG", "successAA: ${response.body().toString()}")
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

}
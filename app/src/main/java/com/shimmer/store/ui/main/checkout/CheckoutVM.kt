package com.shimmer.store.ui.main.checkout

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
import com.shimmer.store.databinding.ItemCheckoutBinding
import com.shimmer.store.datastore.DataStoreKeys.ADMIN_TOKEN
import com.shimmer.store.datastore.DataStoreUtil.readData
import com.shimmer.store.datastore.db.CartModel
import com.shimmer.store.genericAdapter.GenericAdapter
import com.shimmer.store.models.products.ItemProduct
import com.shimmer.store.networking.ApiInterface
import com.shimmer.store.networking.CallHandler
import com.shimmer.store.networking.IMAGE_URL
import com.shimmer.store.networking.Repository
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.db
import com.shimmer.store.utils.getPatternFormat
import com.shimmer.store.utils.getSize
import com.shimmer.store.utils.glideImageChache
import com.shimmer.store.utils.mainThread
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CheckoutVM @Inject constructor(private val repository: Repository) : ViewModel() {

    var subTotalPrice : Double = 0.0
    var discountPrice : Double = 10.0
    var shippingPrice : Double = 100.0
    var cgstPrice : Double = 0.0
    var sgstPrice : Double = 0.0
    var totalPrice : Double = 0.0

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


    val ordersAdapter = object : GenericAdapter<ItemCheckoutBinding, CartModel>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemCheckoutBinding.inflate(inflater, parent, false)

        @SuppressLint("NotifyDataSetChanged")
        override fun onBindHolder(
            binding: ItemCheckoutBinding,
            dataClass: CartModel,
            position: Int
        ) {
            binding.apply {
                textTitle.text = dataClass.name
                textColor.text = "Color: "+dataClass.color
//                if (dataClass.material_type == "12"){
                    textPurity.text = "Purity: "+dataClass.purity
//                } else if (dataClass.material_type == ""){
//                    textPurity.text = "Purity: "+dataClass.purity
//                }
                textRingSize.text = "Ring size: "+dataClass.size
                textPrice.text = "Price: ₹"+getPatternFormat("1", dataClass.price!!) + " x "+dataClass.quantity + " = ₹"+getPatternFormat("1", (dataClass.price?.times(dataClass.quantity.toDouble())))


                mainThread {
                    readData(ADMIN_TOKEN) { token ->
                        Log.e("TAG", "tokenOO: "+token)
                        getProductDetail(token.toString(), dataClass.sku) {
                            Log.e("TAG", "getProductDetailOO: "+this.name)
                            if (this.media_gallery_entries.size > 0){
                                (IMAGE_URL +this.media_gallery_entries[0].file).glideImageChache(binding.ivIcon.context, binding.ivIcon)
                            }
                        }
                    }
                }

                var colorW = when(dataClass.color){
                    "19" -> {"Yellow Gold"}
                    "20" -> {"Gold White"}
                    "25" -> {"Rose Gold"}
                    else -> {"No color"}
                }
                textColor.text = "Color: "+colorW

                val purityW = when(dataClass.purity){
                     "26" -> {"9Kt"}
                     "14" -> {"14Kt"}
                     "15" -> {"18Kt"}
                     "16" -> {"22Kt"}
                     "17" -> {"24Kt"}
                     "18" -> {"95(Platinum)"}
                    else -> {"No purity"}
                }
                textPurity.text = "Purity: "+purityW

                textRingSize.text = "Size: "+ getSize(dataClass.size.toInt())

                ivIcon.singleClick {
                    binding.root.findNavController().navigate(R.id.action_checkout_to_productDetail, Bundle().apply {
                        putString("baseSku", dataClass.sku.split("-")[0])
                        putString("sku", dataClass.sku)
                    })
                }

//                ivCount.text = dataClass.quantity.toString()
//
//                ivMinus.singleClick {
//                    if (dataClass.quantity > 0) {
//                        dataClass.quantity--
//                        notifyItemChanged(position)
//                    }
//                }
//
//                ivPlus.singleClick {
//                    dataClass.quantity++
//                    notifyItemChanged(position)
//                }
//
//                btDelete.singleClick {
//                    item1.removeAt(position)
//                    notifyDataSetChanged()
//                }
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
}
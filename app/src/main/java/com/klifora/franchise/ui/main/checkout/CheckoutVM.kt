package com.klifora.franchise.ui.main.checkout

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
import com.klifora.franchise.R
import com.klifora.franchise.databinding.ItemCheckoutBinding
import com.klifora.franchise.datastore.DataStoreKeys.ADMIN_TOKEN
import com.klifora.franchise.datastore.DataStoreUtil.readData
import com.klifora.franchise.datastore.db.CartModel
import com.klifora.franchise.genericAdapter.GenericAdapter
import com.klifora.franchise.models.cart.ItemCart
import com.klifora.franchise.models.cart.ItemCartModel
import com.klifora.franchise.models.products.ItemProduct
import com.klifora.franchise.networking.ApiInterface
import com.klifora.franchise.networking.CallHandler
import com.klifora.franchise.networking.IMAGE_URL
import com.klifora.franchise.networking.Repository
import com.klifora.franchise.networking.getJsonRequestBody
import com.klifora.franchise.ui.mainActivity.MainActivity.Companion.db
import com.klifora.franchise.ui.mainActivity.MainActivityVM.Companion.storeWebUrl
import com.klifora.franchise.utils.getPatternFormat
import com.klifora.franchise.utils.getSize
import com.klifora.franchise.utils.glideImage
import com.klifora.franchise.utils.glideImageChache
import com.klifora.franchise.utils.mainThread
import com.klifora.franchise.utils.sessionExpired
import com.klifora.franchise.utils.showSnackBar
import com.klifora.franchise.utils.singleClick
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
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
//                textColor.text = "Color: "+dataClass.color
//                if (dataClass.material_type == "12"){
//                    textPurity.text = "Purity: "+dataClass.purity
//                } else if (dataClass.material_type == ""){
//                    textPurity.text = "Purity: "+dataClass.purity
//                }
//                textRingSize.text = "Ring size: "+dataClass.size
                textQty.text = "Quantity: "+dataClass.quantity
                textPrice.text = "Price: ₹ "+getPatternFormat("1", dataClass.price!!)
                textTotalPrice?.text = "Total Price: ₹ "+ getPatternFormat("1",dataClass.price * dataClass.quantity)

                mainThread {
//                    readData(ADMIN_TOKEN) { token ->
//                        Log.e("TAG", "tokenOO: "+token)
//                        getProductDetail(token.toString(), dataClass.sku) {
//                            Log.e("TAG", "getProductDetailOO: "+this.name)
//                            if (this.media_gallery_entries.size > 0){
//                                (IMAGE_URL +this.media_gallery_entries[0].file).glideImage(binding.ivIcon.context, binding.ivIcon)
//                            }
//                        }
//                    }

                    getImages(dataClass.sku) {
                        Log.e("TAG", "getProductDetailOO: "+this.name)
                        if (this.media_gallery_entries.size > 0){
                            (IMAGE_URL + this.media_gallery_entries[0].file).glideImage(binding.ivIcon.context, binding.ivIcon)
                        }
                    }
                }

                val colorW = when(dataClass.color){
                    "19" -> {"Yellow Gold"}
                    "50" -> {"Gold White"}
                    "25" -> {"Rose Gold"}
                    else -> {""}
                }
                textColor.text = "Color: "+colorW
                Log.e("TAG", "onBindHolderColor: "+dataClass.color)

                val purityW = when(dataClass.purity){
                     "26" -> {"9Kt"}
                     "14" -> {"14Kt"}
                     "15" -> {"18Kt"}
                     "16" -> {"22Kt"}
                     "17" -> {"24Kt"}
                     "18" -> {"95(Platinum)"}
                    else -> {""}
                }
                textPurity.text = "Purity: "+purityW

                Log.e("TAG", "onBindHolderSize: "+dataClass.size)

                if (dataClass.size.isEmpty()){
                    textRingSize.visibility = ViewGroup.GONE
                } else {
                    textRingSize.visibility = ViewGroup.VISIBLE
                }

                textRingSize.text = "Size: "+ getSize(dataClass.size.toInt())

                ivIcon.singleClick {
                    binding.root.findNavController().navigate(R.id.action_checkout_to_productDetail, Bundle().apply {
                        putString("baseSku", dataClass.sku.split("-")[0])
                        putString("sku", dataClass.sku)
                    })
                }

            }
        }
    }


    val cartAdapter = object : GenericAdapter<ItemCheckoutBinding, ItemCartModel>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemCheckoutBinding.inflate(inflater, parent, false)

        @SuppressLint("NotifyDataSetChanged")
        override fun onBindHolder(
            binding: ItemCheckoutBinding,
            dataClass: ItemCartModel,
            position: Int
        ) {
            binding.apply {
                ivIcon.singleClick {
                    binding.root.findNavController().navigate(R.id.action_checkout_to_productDetail, Bundle().apply {
                        putString("baseSku", dataClass.sku.split("-")[0])
                        putString("sku", dataClass.sku)
                    })
                }

                textTitle.text = dataClass.name

                textQty.text = "Quantity: "+dataClass.qty
                textPrice.text = "Price: ₹ "+getPatternFormat("1", dataClass.price!!)
//                textPrice.text = "Price: ₹"+getPatternFormat("1", dataClass.price!!) + " x "+dataClass.qty + " = ₹"+getPatternFormat("1", (dataClass.price?.times(dataClass.qty.toDouble())))
                textTotalPrice?.text = "Total Price: ₹ "+ getPatternFormat("1",dataClass.price * dataClass.qty)

                mainThread {
                    getImages(dataClass.sku) {
                        Log.e("TAG", "getProductDetailOO: "+this.name)
                        if (this.media_gallery_entries.size > 0){
                            (IMAGE_URL + this.media_gallery_entries[0].file).glideImage(binding.ivIcon.context, binding.ivIcon)
                        }
                    }

                    readData(ADMIN_TOKEN) { token ->
                        Log.e("TAG", "tokenOO: "+token)

                        getProductDetail(token.toString(), dataClass.sku) {
                            Log.e("TAG", "getProductDetailOO: "+this.name)
//                            if (this.media_gallery_entries.size > 0){
//                                (IMAGE_URL +this.media_gallery_entries[0].file).glideImageChache(binding.ivIcon.context, binding.ivIcon)
//                            }


                            this.custom_attributes.forEach { itemProductAttr ->
                                if (itemProductAttr.attribute_code == "metal_color") {
                                    if (itemProductAttr.value == "19") {
                                        textColor.text = "Color: Yellow Gold"
                                    } else if (itemProductAttr.value == "50") {
                                        textColor.text = "Color: White Gold"
                                    } else if (itemProductAttr.value == "25") {
                                        textColor.text = "Color: Rose Gold"
                                    }
                                }


                                if (itemProductAttr.attribute_code == "metal_purity") {
                                    if (itemProductAttr.value == "26") {
                                        textPurity.text = "Purity: 9 kt"
                                    } else if (itemProductAttr.value == "14") {
                                        textPurity.text = "Purity: 14 kt"
                                    } else if (itemProductAttr.value == "15") {
                                        textPurity.text = "Purity: 18 kt"
                                    } else if (itemProductAttr.value == "16") {
                                        textPurity.text = "Purity: 22 kt"
                                    } else if (itemProductAttr.value == "17") {
                                        textPurity.text = "Purity: 24 kt"
                                    } else if (itemProductAttr.value == "18") {
                                        textPurity.text = "Purity: Platinum 95"
                                    }
                                }

                                if (itemProductAttr.attribute_code == "size") {
                                    textRingSize.text = "Size: "+ getSize(itemProductAttr.value.toString().toInt())
                                    textRingSize.visibility = ViewGroup.VISIBLE
                                }

                                if (itemProductAttr.attribute_code != "size") {
                                    textRingSize.visibility = ViewGroup.GONE
                                }
                            }
                        }
                    }
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



    fun getImages(skuId: String, callBack: ItemProduct.() -> Unit) =
        viewModelScope.launch {
            repository.callApiWithoutLoader(
                callHandler = object : CallHandler<Response<JsonElement>> {
                    override suspend fun sendRequest(apiInterface: ApiInterface) =
                        apiInterface.getImages(skuId)
                    @SuppressLint("SuspiciousIndentation")
                    override fun success(response: Response<JsonElement>) {
                        if (response.isSuccessful) {
                            try {
                                Log.e("TAG", "getImages: ${response.body().toString()}")
                                val mMineUserEntity = Gson().fromJson(response.body(), ItemProduct::class.java)

//                                viewModelScope.launch {
//                                    val userList: List<CartModel>? = db?.cartDao()?.getAll()
//                                    userList?.forEach { user ->
//                                        if (mMineUserEntity.id == user.product_id) {
//                                            mMineUserEntity.apply {
//                                                isSelected = true
//                                            }
//                                        } else {
//                                            mMineUserEntity.apply {
//                                                isSelected = false
//                                            }
//                                        }
//                                    }
//                                    callBack(mMineUserEntity)
//                                }


                            } catch (e: Exception) {
                            }
                        }
                    }

                    override fun error(message: String) {
                        Log.e("TAG", "successEE: ${message}")
                        if(message.contains("customerId")){
                            sessionExpired()
                        } else {
                            showSnackBar("Something went wrong!")
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
//                            sessionExpired()
                        }
                    }

                    override fun loading() {
                        super.loading()
                    }
                }
            )
        }




    fun updateShipping(adminToken: String, jsonObject: JSONObject, callBack: JsonElement.() -> Unit) =
        viewModelScope.launch {
            repository.callApi(
                callHandler = object : CallHandler<Response<JsonElement>> {
                    override suspend fun sendRequest(apiInterface: ApiInterface) =
                        apiInterface.updateShipping("Bearer " +adminToken, storeWebUrl, requestBody = jsonObject.getJsonRequestBody())
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
                        showSnackBar(message)
//                        if(message.contains("fieldName")){
//                            showSnackBar("Something went wrong!")
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




    fun postCustomDetails(adminToken: String, jsonObject: JSONObject, callBack: String.() -> Unit) =
        viewModelScope.launch {
            repository.callApi(
                callHandler = object : CallHandler<Response<String>> {
                    override suspend fun sendRequest(apiInterface: ApiInterface) =
//                        apiInterface.postCustomDetails( requestBody = jsonObject.getJsonRequestBody())
                        apiInterface.postCustomDetails(jsonObject.getString("cartId"), jsonObject.getString("checkout_buyer_name"), jsonObject.getString("checkout_buyer_email"), jsonObject.getString("checkout_purchase_order_no"))
                    @SuppressLint("SuspiciousIndentation")
                    override fun success(response: Response<String>) {
                        Log.e("TAG", "successAAXXZZXX: ${response.toString()}")
                        if (response.isSuccessful) {
                            try {
                                Log.e("TAG", "successAAXXZZ: ${response.body().toString()}")
                                callBack(response.body()!!)
                            } catch (_: Exception) {
                                showSnackBar("Something went wrong!")
                            }
                        }
                    }

                    override fun error(message: String) {
                        showSnackBar(message)
//                        if(message.contains("fieldName")){
//                            showSnackBar("Something went wrong!")
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




    fun createOrder(adminToken: String, jsonObject: JSONObject, callBack: JsonElement.() -> Unit) =
        viewModelScope.launch {
            repository.callApi(
                callHandler = object : CallHandler<Response<JsonElement>> {
                    override suspend fun sendRequest(apiInterface: ApiInterface) =
                        apiInterface.createOrder("Bearer " +adminToken, storeWebUrl, requestBody = jsonObject.getJsonRequestBody())
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
                        showSnackBar(message)
//                        if(message.contains("fieldName")){
//                            showSnackBar("Something went wrong!")
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
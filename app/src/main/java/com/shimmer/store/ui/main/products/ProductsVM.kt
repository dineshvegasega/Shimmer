package com.shimmer.store.ui.main.products

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.shimmer.store.datastore.db.CartModel
import com.shimmer.store.models.Items
import com.shimmer.store.models.products.ItemProduct
import com.shimmer.store.models.products.ItemProductRoot
import com.shimmer.store.models.products.Value
import com.shimmer.store.networking.ApiInterface
import com.shimmer.store.networking.CallHandler
import com.shimmer.store.networking.Repository
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.db
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.loginType
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.storeWebUrl
import com.shimmer.store.utils.mainThread
import com.shimmer.store.utils.sessionExpired
import com.shimmer.store.utils.showSnackBar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class ProductsVM @Inject constructor(private val repository: Repository) : ViewModel() {

    var sortFilter : Int = 0

//    var arrayItemMutableList: MutableList<ItemProduct> = ArrayList()

//    var arrayItemMutableList : MutableLiveData<ItemProductRoot> ?= null


//    var itemModels: MutableList<Items> = ArrayList()

    fun getCartCount(callBack: Int.() -> Unit){
        mainThread {
            val userList: List<CartModel> ?= db?.cartDao()?.getAll()
            var countBadge = 0
            userList?.forEach {
                countBadge += it.quantity
                Log.e("TAG", "getCartCount: "+it.quantity)
            }
            callBack(countBadge)
        }
//        viewModelScope.launch {
//            val userList: List<CartModel> ?= db?.cartDao()?.getAll()
//            var countBadge = 0
//            userList?.forEach {
//                countBadge += it.quantity
//            }
//            callBack(countBadge)
//        }
    }





    private var itemLiveNoticeResult = MutableLiveData<ItemProductRoot>()
    val itemLiveNotice : LiveData<ItemProductRoot> get() = itemLiveNoticeResult
    fun getProducts(adminToken: String, view: View, emptyMap: MutableMap<String, String>) =
        viewModelScope.launch {
            repository.callApi(
                callHandler = object : CallHandler<Response<JsonElement>> {
                    override suspend fun sendRequest(apiInterface: ApiInterface) =
//                        if (loginType == "vendor") {
//                            apiInterface.products("Bearer " +adminToken, storeWebUrl, emptyMap)
//                        } else if (loginType == "guest") {
                            apiInterface.productsID("Bearer " +adminToken, emptyMap)
//                        } else {
//                            apiInterface.products("Bearer " +adminToken, storeWebUrl, emptyMap)
//                        }
                    @SuppressLint("SuspiciousIndentation")
                    override fun success(response: Response<JsonElement>) {
                        if (response.isSuccessful) {
                            try {
                                  Log.e("TAG", "successAA: ${response.body().toString()}")
                                val mMineUserEntity = Gson().fromJson(response.body(), ItemProductRoot::class.java)


                                mainThread {
                                    val userList: List<CartModel>? = db?.cartDao()?.getAll()
//                                    userList?.forEach { itemsDB ->
////                                        Log.e( "TAG", "VVVVVVVVV: " +itemsDB.product_id+"  "+itemsDB.isSelected+"  "+itemsDB.price)
//                                    }

                                    mMineUserEntity.items.forEach {items ->
                                        userList?.forEach { user ->
                                            if (items.id == user.product_id) {
                                                items.apply {
                                                    isSelected = true
                                                }
//                                                Log.e( "TAG", "YYYYYYYYY: " +items.price)
                                            }
//                                            else {
//                                                items.apply {
//                                                    isSelected = false
//                                                }
//                                                Log.e( "TAG", "NNNNNNNNNN: " )
//                                            }
                                        }
                                    }


//                                    mMineUserEntity.items.forEach { items ->
//                                        Log.e( "TAG", "YYYYYYYYY: " +items.isSelected+"  "+items.price)
//                                    }

//                                    if(mMineUserEntity.items.isNotEmpty()){
//                                        callBack(mMineUserEntity)
//                                    } else {
                                        //showSnackBar("No Products Available!")
//                                    }
                                    itemLiveNoticeResult.value = mMineUserEntity
//                                    arrayItemMutableList?.value = mMineUserEntity

//                                    callBack(mMineUserEntity)
                                }

//                                viewModelScope.launch {
//
//                                    val userList: List<CartModel>? = db?.cartDao()?.getAll()
//                                    userList?.forEach { itemsDB ->
//                                        Log.e( "TAG", "VVVVVVVVV: " +itemsDB.product_id+"  "+itemsDB.isSelected+"  "+itemsDB.price)
//                                    }
//
//                                    mMineUserEntity.items.forEach {items ->
//                                        userList?.forEach { user ->
//                                            if (items.id == user.product_id) {
//                                                items.apply {
//                                                    isSelected = true
//                                                }
////                                                Log.e( "TAG", "YYYYYYYYY: " +items.price)
//                                            } else {
//                                                items.apply {
//                                                    isSelected = false
//                                                }
////                                                Log.e( "TAG", "NNNNNNNNNN: " )
//                                            }
//                                        }
//                                    }
//
//
//                                    mMineUserEntity.items.forEach { items ->
//                                        Log.e( "TAG", "YYYYYYYYY: " +items.isSelected+"  "+items.price)
//                                    }
//
//                                    if(mMineUserEntity.items.isNotEmpty()){
//                                        callBack(mMineUserEntity)
//                                    } else {
//                                        showSnackBar("No Products Available!")
//                                    }
//                                }


                            } catch (e: Exception) {
                            }
                        }
                    }

                    override fun error(message: String) {
//                        Log.e("TAG", "successAA: ${message}")
//                        super.error(message)
//                        showSnackBar(message)
//                        callBack(message.toString())

                        if(message.contains("authorized")){
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


}
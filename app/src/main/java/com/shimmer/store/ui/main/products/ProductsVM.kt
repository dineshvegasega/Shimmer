package com.shimmer.store.ui.main.products

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.shimmer.store.R
import com.shimmer.store.databinding.ItemProductBinding
import com.shimmer.store.databinding.ItemSearchBinding
import com.shimmer.store.datastore.db.CartModel
import com.shimmer.store.genericAdapter.GenericAdapter
import com.shimmer.store.models.Items
import com.shimmer.store.models.products.ItemProduct
import com.shimmer.store.models.products.ItemProductRoot
import com.shimmer.store.models.products.Value
import com.shimmer.store.networking.ApiInterface
import com.shimmer.store.networking.CallHandler
import com.shimmer.store.networking.IMAGE_URL
import com.shimmer.store.networking.Repository
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.db
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.cartItemLiveData
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.loginType
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.storeWebUrl
import com.shimmer.store.utils.getPatternFormat
import com.shimmer.store.utils.getSize
import com.shimmer.store.utils.glideImage
import com.shimmer.store.utils.mainThread
import com.shimmer.store.utils.sessionExpired
import com.shimmer.store.utils.showSnackBar
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class ProductsVM @Inject constructor(private val repository: Repository) : ViewModel() {

    var sortFilter : Int = 0

    val itemsProduct : ArrayList<ItemProduct> = ArrayList()

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



    val productAdapter = object : GenericAdapter<ItemProductBinding, ItemProduct>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemProductBinding.inflate(inflater, parent, false)

        override fun onBindHolder(
            binding: ItemProductBinding,
            dataClass: ItemProduct,
            position: Int
        ) {
            binding.apply {

                textTitle.text = dataClass.name
                textPrice.text = "₹ "+ getPatternFormat("1", dataClass.price)

                (IMAGE_URL + if(dataClass.media_gallery_entries.size > 0) dataClass.media_gallery_entries[0].file else "").glideImage(ivIcon.context, ivIcon)

                ivIcon.setOnClickListener {
//                it.findNavController().navigate(R.id.action_products_to_productsDetail, Bundle().apply {
//                    putParcelable("model", model)
//                })
                    it.findNavController().navigate(R.id.action_products_to_productsDetail, Bundle().apply {
                        putString("baseSku", dataClass.sku)
                        putString("sku", "")
                    })
                }

                ivAddCart.imageTintList = if(dataClass.isSelected == true) ContextCompat.getColorStateList(root.context,R.color.app_color) else ContextCompat.getColorStateList(root.context,R.color._9A9A9A)


                ivAddCart.setOnClickListener {
                    dataClass.isSelected = !dataClass.isSelected
                    mainThread {
                        val newUser = CartModel(product_id = dataClass.id, name = dataClass.name, price = dataClass.price, quantity = 1, sku = dataClass.sku, currentTime = System.currentTimeMillis())
                        dataClass.custom_attributes.forEach {
                            if (it.attribute_code == "size"){
                                newUser.apply {
                                    this.size = ""+it.value
                                }
                            }


                            if (it.attribute_code == "metal_color"){
                                newUser.apply {
                                    this.color = ""+it.value
                                }
                            }

                            if (it.attribute_code == "metal_type"){
                                Log.e("TAG", "metal_typeAAA " + it.value)
                                if (it.value == "12"){
                                    newUser.apply {
                                        this.material_type = ""+it.value
                                    }
                                    dataClass.custom_attributes.forEach { againAttributes ->
                                        if (againAttributes.attribute_code == "metal_purity"){
                                            Log.e("TAG", "metal_typeBBB " + againAttributes.value)
                                            newUser.apply {
                                                this.purity = ""+againAttributes.value
                                            }
                                        }
//                                    Log.e("TAG", "metal_typeBBB " + againAttributes.toString())
                                    }
                                }
                            } else {
//                            Log.e("TAG", "metal_typeBBB " + it.value)
//                            model.custom_attributes.forEach { againAttributes ->
//                                if (againAttributes.attribute_code == "metal_purity"){
//                                    newUser.apply {
//                                        this.purity = ""+it.value
//                                    }
//                                }
//                            }
                            }
                        }
                        if(dataClass.isSelected == true){
                            db?.cartDao()?.insertAll(newUser)
                        } else {
                            db?.cartDao()?.deleteById(newUser.product_id!!)
                        }
                        cartItemLiveData.value = true
                        notifyItemChanged(position)
                    }
                }


                btAddCart.setOnClickListener {
                    mainThread {
                        val newUser = CartModel(product_id = dataClass.id, name = dataClass.name, price = dataClass.price, quantity = 1, currentTime = System.currentTimeMillis())
                        db?.cartDao()?.insertAll(newUser)
                        cartItemLiveData.value = true
                        it.findNavController().navigate(R.id.action_products_to_cart)
                    }
                }
            }
        }
    }





    private var itemProducstResult = MutableLiveData<ItemProductRoot>()
    val itemProducts : LiveData<ItemProductRoot> get() = itemProducstResult
    fun getProducts(adminToken: String, view: View, emptyMap: MutableMap<String, String>) =
        viewModelScope.launch {
            repository.callApiWithoutLoader(
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
                                    itemProducstResult.value = mMineUserEntity
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
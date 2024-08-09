package com.shimmer.store.ui.main.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.shimmer.store.R
import com.shimmer.store.databinding.ItemFaqBinding
import com.shimmer.store.databinding.ItemSearchBinding
import com.shimmer.store.databinding.ItemSearchHistoryBinding
import com.shimmer.store.datastore.db.CartModel
import com.shimmer.store.datastore.db.SearchModel
import com.shimmer.store.genericAdapter.GenericAdapter
import com.shimmer.store.models.Items
import com.shimmer.store.models.products.ItemProduct
import com.shimmer.store.models.products.ItemProductRoot
import com.shimmer.store.networking.ApiInterface
import com.shimmer.store.networking.CallHandler
import com.shimmer.store.networking.Repository
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.db
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.loginType
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.storeWebUrl
import com.shimmer.store.utils.getPatternFormat
import com.shimmer.store.utils.ioThread
import com.shimmer.store.utils.mainThread
import com.shimmer.store.utils.sessionExpired
import com.shimmer.store.utils.showSnackBar
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SearchVM @Inject constructor(private val repository: Repository) : ViewModel() {

   var isListVisible : Boolean = false



    val searchAdapter = object : GenericAdapter<ItemSearchBinding, ItemProduct>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemSearchBinding.inflate(inflater, parent, false)

        override fun onBindHolder(
            binding: ItemSearchBinding,
            dataClass: ItemProduct,
            position: Int
        ) {
            binding.apply {
//                textDesc.visibility =
//                    if (dataClass.isCollapse == true) View.VISIBLE else View.GONE
//                ivHideShow.setImageDrawable(
//                    ContextCompat.getDrawable(
//                        root.context,
//                        if (dataClass.isCollapse == true) R.drawable.baseline_remove_24 else R.drawable.baseline_add_24
//                    )
//                )

                textTitle.text = dataClass.name
                textPrice.text = "Price: ₹"+ getPatternFormat("1", dataClass.price!!)
                dataClass.custom_attributes.forEach {
                    if (it.attribute_code == "size"){
                        textRingsize.text = "Ring size: "+it.value
                    }

                    if (it.attribute_code == "metal_color"){
                        textColor.text = "Color: "+it.value
                    }

                    if (it.attribute_code == "metal_type"){
                        Log.e("TAG", "metal_typeAAA " + it.value)
                        if (it.value == "12"){
                            dataClass.custom_attributes.forEach { againAttributes ->
                                if (againAttributes.attribute_code == "metal_purity"){
                                    Log.e("TAG", "metal_typeBBB " + againAttributes.value)
                                    textPurity.text = "Purity: "+againAttributes.value
                                }
                            }
                        }
                    } else {
                        dataClass.custom_attributes.forEach { againAttributes ->
                            if (againAttributes.attribute_code == "metal_purity"){
                                Log.e("TAG", "metal_typeBBB " + againAttributes.value)
                                textPurity.text = "Purity: "+againAttributes.value
                            }
                        }
                    }
                }


                root.singleClick {
                    root.findNavController()
                        .navigate(R.id.action_search_to_productDetail, Bundle().apply {
                            putString("model", dataClass.sku)
                        })
                }
            }
        }
    }



    var searchDelete = MutableLiveData<Boolean>(false)
    var searchValue = MutableLiveData<String?>("")

    val searchHistoryAdapter = object : GenericAdapter<ItemSearchHistoryBinding, SearchModel>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemSearchHistoryBinding.inflate(inflater, parent, false)

        override fun onBindHolder(
            binding: ItemSearchHistoryBinding,
            dataClass: SearchModel,
            position: Int
        ) {
            binding.apply {
                textItem.text = dataClass.search_name
//                textDesc.visibility =
//                    if (dataClass.isCollapse == true) View.VISIBLE else View.GONE
//                ivHideShow.setImageDrawable(
//                    ContextCompat.getDrawable(
//                        root.context,
//                        if (dataClass.isCollapse == true) R.drawable.baseline_remove_24 else R.drawable.baseline_add_24
//                    )
//                )

                textItem.singleClick {
                    searchValue.value = dataClass.search_name
                }



                ivCross.singleClick {
                    mainThread {
                        db?.searchDao()?.delete(dataClass)
                        searchDelete.value = true
                    }
                }
            }
        }
    }






    fun getProducts(adminToken: String, view: View, emptyMap: MutableMap<String, String>, callBack: ItemProductRoot.() -> Unit) =
        viewModelScope.launch {
            repository.callApi(
                callHandler = object : CallHandler<Response<JsonElement>> {
                    override suspend fun sendRequest(apiInterface: ApiInterface) =
//                    if (loginType == "vendor") {
//                        apiInterface.products("Bearer " +adminToken, storeWebUrl, emptyMap)
//                    } else if (loginType == "guest") {
                        apiInterface.productsID("Bearer " +adminToken, emptyMap)
//                    } else {
//                        apiInterface.products("Bearer " +adminToken, storeWebUrl, emptyMap)
//                    }
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

                                    if(mMineUserEntity.items.isNotEmpty()){
                                        callBack(mMineUserEntity)
                                    } else {
                                        callBack(mMineUserEntity)
                                        showSnackBar("No Products Available!")
                                    }
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
package com.shimmer.store.ui.main.search

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
import com.shimmer.store.networking.IMAGE_URL
import com.shimmer.store.networking.Repository
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.db
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.loginType
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.storeWebUrl
import com.shimmer.store.utils.getPatternFormat
import com.shimmer.store.utils.getSize
import com.shimmer.store.utils.glideImage
import com.shimmer.store.utils.glideImageChache
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

    var searchType = 1


    val itemsSearch: ArrayList<ItemProduct> = ArrayList()

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
                textTitle.text = dataClass.name
                textPrice.text = "Price: ₹ "+ getPatternFormat("1", dataClass.price!!)
                dataClass.custom_attributes.forEach {
                    if (it.attribute_code == "size"){
//                        textRingsize.text = "Ring size: "+it.value
                        textRingsize.text = "Size: "+ getSize(it.value.toString().toInt())
                        textRingsize.visibility=View.VISIBLE
                    }

                    if (it.attribute_code == "metal_color"){
                        Log.e("TAG", "ColorAAAA: " + it.value)
                        val colorW = when(it.value.toString()){
                            "19" -> {"Yellow Gold"}
                            "20" -> {"Gold White"}
                            "25" -> {"Rose Gold"}
                            else -> {"No color"}
                        }
                        textColor.text = "Color: "+colorW
//                        textColor.text = "Color: "+it.value
                        textColor.visibility=View.VISIBLE
                    }

                    if (it.attribute_code == "metal_purity"){
                        val purityW = when(it.value.toString()){
                            "26" -> {"9Kt"}
                            "14" -> {"14Kt"}
                            "15" -> {"18Kt"}
                            "16" -> {"22Kt"}
                            "17" -> {"24Kt"}
                            "18" -> {"95(Platinum)"}
                            else -> {"No purity"}
                        }
                        textPurity.text = "Purity: "+purityW
                        textPurity.visibility=View.VISIBLE
                    }


//                    if (it.attribute_code == "metal_type"){
//                        Log.e("TAG", "metal_typeAAA " + it.value)
//                        if (it.value == "12"){
//                            dataClass.custom_attributes.forEach { againAttributes ->
//                                if (againAttributes.attribute_code == "metal_purity"){
//                                    Log.e("TAG", "metal_typeBBB " + againAttributes.value)
//                                    textPurity.text = "Purity: "+againAttributes.value
//                                    textRingsize.visibility=View.VISIBLE
//                                }
//                            }
//                        }
//                    } else {
//                        dataClass.custom_attributes.forEach { againAttributes ->
//                            if (againAttributes.attribute_code == "metal_purity"){
//                                Log.e("TAG", "metal_typeBBB " + againAttributes.value)
//                                textPurity.text = "Purity: "+againAttributes.value
//                                textRingsize.visibility=View.VISIBLE
//                            }
//                        }
//                    }
                }


                if (dataClass.media_gallery_entries.size > 0){
                    (IMAGE_URL +dataClass.media_gallery_entries[0].file).glideImage(binding.ivIcon.context, binding.ivIcon)
                }

                root.singleClick {
                    root.findNavController()
                        .navigate(R.id.action_search_to_productDetail, Bundle().apply {
                            if (searchType == 1){
                                if (dataClass.sku.contains("-")){
                                    putString("baseSku", dataClass.sku.split("-")[0])
                                } else if (dataClass.sku.contains(" ")){
                                    putString("baseSku", dataClass.sku.split(" ")[0])
                                } else {
                                    putString("baseSku", dataClass.sku)
                                }
                            } else if (searchType == 2) {
                                if (dataClass.sku.contains("-")){
                                    putString("baseSku", dataClass.sku.split("-")[0])
                                    putString("sku", dataClass.sku)

//                                    Log.e("TAG", "baseSkuAA " + dataClass.sku.split("-")[0])
//                                    Log.e("TAG", "skuAA " + dataClass.sku)

                                } else if (dataClass.sku.contains(" ")) {
                                    putString("baseSku", dataClass.sku.split(" ")[0])
                                    putString("sku", dataClass.sku)
                                } else {
                                    putString("baseSku", dataClass.sku)
//                                    Log.e("TAG", "baseSkuBB " + dataClass.sku)
//                                    Log.e("TAG", "skuBB " + dataClass.sku)
                                }
                            }
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





    private var itemProductsResult = MutableLiveData<ItemProductRoot>()
    val itemProducts : LiveData<ItemProductRoot> get() = itemProductsResult
    fun getProducts(adminToken: String, view: View, emptyMap: MutableMap<String, String>) =
        viewModelScope.launch {
            repository.callApiWithoutLoader(
                callHandler = object : CallHandler<Response<JsonElement>> {
                    override suspend fun sendRequest(apiInterface: ApiInterface) =
                        apiInterface.productsID("Bearer " +adminToken, emptyMap)
                    @SuppressLint("SuspiciousIndentation")
                    override fun success(response: Response<JsonElement>) {
                        if (response.isSuccessful) {
                            try {
                                Log.e("TAG", "successAA: ${response.body().toString()}")
                                val mMineUserEntity = Gson().fromJson(response.body(), ItemProductRoot::class.java)
                                itemProductsResult.value = mMineUserEntity
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
package com.klifora.franchise.ui.main.home

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.klifora.franchise.R
import com.klifora.franchise.databinding.ItemHome3Binding
import com.klifora.franchise.databinding.ItemHomeCategoryBinding
import com.klifora.franchise.datastore.db.CartModel
import com.klifora.franchise.genericAdapter.GenericAdapter
import com.klifora.franchise.models.ItemBanner
import com.klifora.franchise.models.ItemBannerItem
import com.klifora.franchise.models.Items
import com.klifora.franchise.networking.ApiInterface
import com.klifora.franchise.networking.BANNER_IMAGE_URL
import com.klifora.franchise.networking.CallHandler
import com.klifora.franchise.networking.Repository
import com.klifora.franchise.ui.mainActivity.MainActivity.Companion.db
import com.klifora.franchise.ui.mainActivity.MainActivityVM.Companion.mainMaterial
import com.klifora.franchise.ui.mainActivity.MainActivityVM.Companion.mainPrice
import com.klifora.franchise.ui.mainActivity.MainActivityVM.Companion.mainShopFor
import com.klifora.franchise.utils.glideImageBanner
import com.klifora.franchise.utils.glideImageChache
//import com.klifora.franchise.utils.myOptionsGlideLand
import com.klifora.franchise.utils.showSnackBar
import com.klifora.franchise.utils.singleClick
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HomeVM @Inject constructor(private val repository: Repository) : ViewModel() {

    fun getCartCount(callBack: Int.() -> Unit) {
        viewModelScope.launch {
            val userList: List<CartModel>? = db?.cartDao()?.getAll()
            var countBadge = 0
            userList?.forEach {
                countBadge += it.quantity
            }
            callBack(countBadge)
        }
    }


    val categoryAdapter = object : GenericAdapter<ItemHomeCategoryBinding, Items>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemHomeCategoryBinding.inflate(inflater, parent, false)

        override fun onBindHolder(
            binding: ItemHomeCategoryBinding,
            dataClass: Items,
            position: Int
        ) {
            binding.apply {
                dataClass.image.glideImageChache(binding.ivIcon.context, binding.ivIcon)

                textTitle.text = dataClass.name
                ivIcon.setOnClickListener {
                    currentList.forEach {
                        it.isSelected = false
                        it.isCollapse = false
                        it.subCategory.forEach {
                            it.isSelected = false
//                            it.isChildSelect = false
                        }
                    }
                    dataClass.apply {
                        isSelected = true
                        subCategory.forEach {
                            it.isSelected = true
//                            it.isChildSelect = true
                        }
                    }
                    mainPrice.forEach {
                        it.isSelected = false
//                        it.isChildSelect = false
                    }
                    mainMaterial.forEach {
                        it.isSelected = false
//                        it.isChildSelect = false
                    }
                    mainShopFor.forEach {
                        it.isSelected = false
//                        it.isChildSelect = false
                    }


                    it.findNavController().navigate(R.id.action_home_to_products)
                }
            }
        }
    }


    val bannerAdapter = object : GenericAdapter<ItemHome3Binding, ItemBannerItem>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemHome3Binding.inflate(inflater, parent, false)

        override fun onBindHolder(
            binding: ItemHome3Binding,
            dataClass: ItemBannerItem,
            position: Int
        ) {
            binding.apply {
                glideImageBanner(
                    binding.ivIcon.context,
                    binding.ivIcon,
                    BANNER_IMAGE_URL + dataClass.image
                )


//                Glide.with(binding.ivIcon.context)
//                    .load(BANNER_IMAGE_URL + dataClass.image)
//                    .apply(myOptionsGlideLand)
//                    .into(binding.ivIcon)

                ivIcon.singleClick {
                    if (dataClass.newtab == "0") {
                        root.findNavController().navigate(R.id.action_home_to_webPage, Bundle().apply {
                            putString("url", dataClass.url_banner)
                        })
                    } else {
                        dataClass.url_banner?.let {
                            val webIntent = Intent(
                                Intent.ACTION_VIEW, Uri.parse(dataClass.url_banner)
                            )
                            try {
                                root.context.startActivity(webIntent)
                            } catch (ex: ActivityNotFoundException) {
                            }
                        }
                    }
                }
            }
        }
    }


    fun banner(callBack: ItemBanner.() -> Unit) =
        viewModelScope.launch {
            repository.callApi(
                callHandler = object : CallHandler<Response<ItemBanner>> {
                    override suspend fun sendRequest(apiInterface: ApiInterface) =
//                        if (loginType == "vendor") {
//                            apiInterface.products("Bearer " +adminToken, storeWebUrl, emptyMap)
//                        } else if (loginType == "guest") {
                        apiInterface.banner()

                    //                        } else {
//                            apiInterface.products("Bearer " +adminToken, storeWebUrl, emptyMap)
//                        }
                    @SuppressLint("SuspiciousIndentation")
                    override fun success(response: Response<ItemBanner>) {
                        if (response.isSuccessful) {
                            try {
                                Log.e("TAG", "successAA: ${response.body().toString()}")
                                callBack(response.body()!!)
                            } catch (e: Exception) {
                            }
                        }
                    }

                    override fun error(message: String) {
                        showSnackBar(message.toString())
                    }

                    override fun loading() {
                        super.loading()
                    }
                }
            )
        }

//    fun getCart(customerToken: String, callBack: ItemCart.() -> Unit) =
//        viewModelScope.launch {
//            repository.callApi(
//                callHandler = object : CallHandler<Response<ItemCart>> {
//                    override suspend fun sendRequest(apiInterface: ApiInterface) =
////                        if (loginType == "vendor") {
//                        apiInterface.getCart("Bearer " +customerToken, storeWebUrl)
//                    //                        } else if (loginType == "guest") {
////                        apiInterface.getQuoteId("Bearer " +adminToken, emptyMap)
//                    //                        } else {
////                            apiInterface.products("Bearer " +adminToken, storeWebUrl, emptyMap)
////                        }
//                    @SuppressLint("SuspiciousIndentation")
//                    override fun success(response: Response<ItemCart>) {
//                        if (response.isSuccessful) {
//                            try {
//                                Log.e("TAG", "successAAXX: ${response.body().toString()}")
//                                callBack(response.body()!!)
//                            } catch (_: Exception) {
//                            }
//                        }
//                    }
//
//                    override fun error(message: String) {
////                        if(message.contains("fieldName")){
////                            showSnackBar("Something went wrong!")
////                        } else {
////                            sessionExpired()
////                        }
//                    }
//
//                    override fun loading() {
//                        super.loading()
//                    }
//                }
//            )
//        }

//
//    @SuppressLint("UseCompatLoadingForDrawables")
//    public fun indicator(binding: HomeBinding, arrayList: ArrayList<String>, current: Int) {
//        val views : ArrayList<View> = ArrayList()
//        views.add(binding.indicatorLayout.view1)
//        views.add(binding.indicatorLayout.view2)
//        views.add(binding.indicatorLayout.view3)
//        views.add(binding.indicatorLayout.view4)
//        views.add(binding.indicatorLayout.view5)
//        views.add(binding.indicatorLayout.view6)
//        views.add(binding.indicatorLayout.view7)
//        views.add(binding.indicatorLayout.view8)
//        views.add(binding.indicatorLayout.view9)
//        views.add(binding.indicatorLayout.view10)
//        views.add(binding.indicatorLayout.view11)
//        views.add(binding.indicatorLayout.view12)
//        views.add(binding.indicatorLayout.view13)
//        views.add(binding.indicatorLayout.view14)
//        views.add(binding.indicatorLayout.view15)
//        views.add(binding.indicatorLayout.view16)
//        views.add(binding.indicatorLayout.view17)
//        views.add(binding.indicatorLayout.view18)
//        views.add(binding.indicatorLayout.view19)
//        views.add(binding.indicatorLayout.view20)
//
//        var index = 0
//        views.forEach {
//            Log.e("TAG", " index "+index+" size "+arrayList.size+" current "+current)
//            if (index <= (arrayList.size -1)){
//                it.visibility = VISIBLE
//                if (arrayList[index].endsWith(".mp3")){
//                    it.setBackgroundResource(R.drawable.ic_triangle_right)
//                } else {
//                    it.setBackgroundResource(R.drawable.bg_all_round_black)
//                }
//                if (index == current){
//                    it.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(MainActivity.context.get()!!, R.color.app_color))
//                } else {
//                    it.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(MainActivity.context.get()!!, R.color._9A9A9A))
//                }
//            } else {
//                it.visibility = GONE
//            }
//            index++
//        }
//    }

}
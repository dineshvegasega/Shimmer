package com.shimmer.store.ui.main.home

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.shimmer.store.R
import com.shimmer.store.databinding.AbcBinding
import com.shimmer.store.databinding.HomeBinding
import com.shimmer.store.databinding.ItemHomeCategoryBinding
import com.shimmer.store.databinding.ItemProductDiamondsBinding
import com.shimmer.store.datastore.db.CartModel
import com.shimmer.store.genericAdapter.GenericAdapter
import com.shimmer.store.models.Items
import com.shimmer.store.ui.mainActivity.MainActivity
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.db
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.mainMaterial
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.mainPrice
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.mainShopFor
//import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.isFilterFrom
//import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.arrayCategory
//import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.arrayMaterial
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeVM @Inject constructor() : ViewModel() {


    var item1 : ArrayList<String> = ArrayList()
    var item2 : ArrayList<String> = ArrayList()
    var item3 : ArrayList<String> = ArrayList()


    init {
        item1.add("1")
        item1.add("2")
        item1.add("3")

        item2.add("1")
        item2.add("2")
        item2.add("1")
        item2.add("2")
        item2.add("1")
        item2.add("2")

        item3.add("1")
        item3.add("2")
        item3.add("3")
        item3.add("4")
    }


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
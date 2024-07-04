package com.shimmer.store.ui.main.filters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shimmer.store.R
import com.shimmer.store.databinding.ItemFilterBinding
import com.shimmer.store.databinding.ItemProductDiamondsBinding
import com.shimmer.store.genericAdapter.GenericAdapter
import com.shimmer.store.models.Items
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.typefaceroboto_light
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.typefaceroboto_medium
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.arrayCategory
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.arrayMaterial
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.arrayPrice
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.arrayShopFor
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FiltersVM @Inject constructor() : ViewModel() {
    var itemPriceCount = MutableLiveData<Int>(0)
    var itemCategoryCount = MutableLiveData<Int>(0)
    var itemMaterialCount = MutableLiveData<Int>(0)
    var itemShopForCount = MutableLiveData<Int>(0)

    var itemPrice: ArrayList<Items> = ArrayList()
    var itemCategory: ArrayList<Items> = ArrayList()
    var itemMaterial: ArrayList<Items> = ArrayList()
    var itemShopFor: ArrayList<Items> = ArrayList()

    init {
        itemPrice.add(Items("₹1000 - ₹10000"))
        itemPrice.add(Items("₹10000 - ₹15000"))
        itemPrice.add(Items("₹15000 - ₹20000"))
        itemPrice.add(Items("₹20000 - ₹30000"))
        itemPrice.add(Items("₹30000 - ₹50000"))
        itemPrice.add(Items("₹50000 - ₹100000"))
        itemPrice.add(Items("₹100000 - ₹1000000"))
        itemPrice.add(Items("₹1000000 - Above"))

        itemCategory.add(Items("ring"))
        itemCategory.add(Items("necklace"))
        itemCategory.add(Items("earring"))
        itemCategory.add(Items("bangle"))

        itemMaterial.add(Items("Gold 14 K"))
        itemMaterial.add(Items("Gold 18 K"))
        itemMaterial.add(Items("Platinum"))

        itemShopFor.add(Items("Men"))
        itemShopFor.add(Items("Women"))
        itemShopFor.add(Items("Kids"))
    }


    var selectedPosition = -1

    val priceAdapter = object : GenericAdapter<ItemFilterBinding, Items>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemFilterBinding.inflate(inflater, parent, false)

        @SuppressLint("NotifyDataSetChanged")
        override fun onBindHolder(
            binding: ItemFilterBinding,
            dataClass: Items,
            position: Int
        ) {
            binding.apply {
                textItem.text = dataClass.name
                textItem.setTypeface(if (dataClass.isSelected == true) typefaceroboto_medium else typefaceroboto_light)
                ivIconCheck.imageTintList =
                    if (dataClass.isSelected == true) ContextCompat.getColorStateList(
                        binding.root.context,
                        R.color.app_color
                    )
                    else ContextCompat.getColorStateList(
                        binding.root.context,
                        R.color._D9D9D9
                    )

                root.singleClick {
                    selectedPosition = position
                    dataClass.isSelected = !dataClass.isSelected

                    if (dataClass.isSelected == true) {
                        arrayPrice.add(dataClass.name)
                    } else {
                        arrayPrice.remove(dataClass.name)
                    }
                    arrayPrice.distinct()
                    notifyItemChanged(position)
                }

                val filteredNot = currentList.filter {
                    it.isSelected == true
                }
                itemPriceCount.value = filteredNot.size
            }
        }
    }


    val categoryAdapter = object : GenericAdapter<ItemFilterBinding, Items>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemFilterBinding.inflate(inflater, parent, false)

        @SuppressLint("NotifyDataSetChanged")
        override fun onBindHolder(
            binding: ItemFilterBinding,
            dataClass: Items,
            position: Int
        ) {
            binding.apply {
                textItem.text = dataClass.name

                textItem.setTypeface(if (dataClass.isSelected == true) typefaceroboto_medium else typefaceroboto_light)
                ivIconCheck.imageTintList =
                    if (dataClass.isSelected == true) ContextCompat.getColorStateList(
                        binding.root.context,
                        R.color.app_color
                    )
                    else ContextCompat.getColorStateList(
                        binding.root.context,
                        R.color._D9D9D9
                    )

                root.singleClick {
                    selectedPosition = position
                    dataClass.isSelected = !dataClass.isSelected
                    if (dataClass.isSelected == true) {
                        arrayCategory.add(dataClass.name)
                    } else {
                        arrayCategory.remove(dataClass.name)
                    }
                    arrayCategory.distinct()
                    notifyItemChanged(position)
                }


                val filteredNot = currentList.filter { it.isSelected == true }
                itemCategoryCount.value = filteredNot.size

                Log.e("TAG", "dataClass.isSelected " + dataClass.isSelected)

            }
        }
    }


    val materialAdapter = object : GenericAdapter<ItemFilterBinding, Items>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemFilterBinding.inflate(inflater, parent, false)

        @SuppressLint("NotifyDataSetChanged")
        override fun onBindHolder(
            binding: ItemFilterBinding,
            dataClass: Items,
            position: Int
        ) {
            binding.apply {
                textItem.text = dataClass.name

                textItem.setTypeface(if (dataClass.isSelected == true) typefaceroboto_medium else typefaceroboto_light)
                ivIconCheck.imageTintList =
                    if (dataClass.isSelected == true) ContextCompat.getColorStateList(
                        binding.root.context,
                        R.color.app_color
                    )
                    else ContextCompat.getColorStateList(
                        binding.root.context,
                        R.color._D9D9D9
                    )

                root.singleClick {
                    selectedPosition = position
                    dataClass.isSelected = !dataClass.isSelected
                    if (dataClass.isSelected == true) {
                        arrayMaterial.add(dataClass.name)
                    } else {
                        arrayMaterial.remove(dataClass.name)
                    }
                    arrayMaterial.distinct()
                    notifyItemChanged(position)
                }


                val filteredNot = currentList.filter { it.isSelected == true }
                itemMaterialCount.value = filteredNot.size

            }
        }
    }



    val shopForAdapter = object : GenericAdapter<ItemFilterBinding, Items>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemFilterBinding.inflate(inflater, parent, false)

        @SuppressLint("NotifyDataSetChanged")
        override fun onBindHolder(
            binding: ItemFilterBinding,
            dataClass: Items,
            position: Int
        ) {
            binding.apply {
                textItem.text = dataClass.name

                textItem.setTypeface(if (dataClass.isSelected == true) typefaceroboto_medium else typefaceroboto_light)
                ivIconCheck.imageTintList =
                    if (dataClass.isSelected == true) ContextCompat.getColorStateList(
                        binding.root.context,
                        R.color.app_color
                    )
                    else ContextCompat.getColorStateList(
                        binding.root.context,
                        R.color._D9D9D9
                    )

                root.singleClick {
                    selectedPosition = position
                    dataClass.isSelected = !dataClass.isSelected
                    if (dataClass.isSelected == true) {
                        arrayShopFor.add(dataClass.name)
                    } else {
                        arrayShopFor.remove(dataClass.name)
                    }
                    arrayShopFor.distinct()
                    notifyItemChanged(position)
                }


                val filteredNot = currentList.filter { it.isSelected == true }
                itemShopForCount.value = filteredNot.size

            }
        }
    }

}
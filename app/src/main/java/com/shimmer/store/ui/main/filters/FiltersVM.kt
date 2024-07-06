package com.shimmer.store.ui.main.filters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shimmer.store.R
import com.shimmer.store.databinding.ItemFilterBinding
import com.shimmer.store.databinding.ItemFilterCategoryBinding
import com.shimmer.store.databinding.ItemFilterChildBinding
import com.shimmer.store.databinding.ItemProductDiamondsBinding
import com.shimmer.store.genericAdapter.GenericAdapter
import com.shimmer.store.models.Items
//import com.shimmer.store.ui.main.filters.Filters.Companion.headerAdapter
import com.shimmer.store.ui.mainActivity.MainActivity
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.typefaceroboto_light
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.typefaceroboto_medium
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.isFilterFromFrom
//import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.arrayCategory
//import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.arrayMaterial
//import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.arrayPrice
//import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.arrayShopFor
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.mainCategory
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FiltersVM @Inject constructor() : ViewModel() {
    var itemPriceCount = MutableLiveData<Int>(0)
    var itemCategoryCount = MutableLiveData<Int>(0)
    var itemMaterialCount = MutableLiveData<Int>(0)
    var itemShopForCount = MutableLiveData<Int>(0)

//    var itemPrice: ArrayList<Items> = ArrayList()
//    var itemCategory: ArrayList<Items> = ArrayList()
//    var itemMaterial: ArrayList<Items> = ArrayList()
//    var itemShopFor: ArrayList<Items> = ArrayList()

    init {
//        itemPrice.add(Items("₹1000 - ₹10000"))
//        itemPrice.add(Items("₹10000 - ₹15000"))
//        itemPrice.add(Items("₹15000 - ₹20000"))
//        itemPrice.add(Items("₹20000 - ₹30000"))
//        itemPrice.add(Items("₹30000 - ₹50000"))
//        itemPrice.add(Items("₹50000 - ₹100000"))
//        itemPrice.add(Items("₹100000 - ₹1000000"))
//        itemPrice.add(Items("₹1000000 - Above"))

//        itemCategory.add(Items("ring"))
//        itemCategory.add(Items("necklace"))
//        itemCategory.add(Items("earring"))
//        itemCategory.add(Items("bangle"))


//        mainCategory.add(Items("RINGS",
//            subCategory = arrayListOf(Items("All Rings"),
//                Items("Men's"),
//                Items("Band"),
//                Items("Casual"),
//                Items("Cocktail"),
//                Items("Engagement"),
//                Items("Solitaire"))
//        ))
//        mainCategory.add(Items("NECKLACE",
//            subCategory = arrayListOf(Items("All Necklace"),
//                Items("Men's"),
//                Items("Long Necklace"),
//                Items("Short Necklace"),
//                Items("Short Necklace"))
//        ))
//        mainCategory.add(Items("PENDANTS",
//            subCategory = arrayListOf(Items("All Pendants"),
//                Items("Alphabates"),
//                Items("Zodiac"),
//                Items("Casual"),
//                Items("Everyday"))
//        ))
//        mainCategory.add(Items("BRACELETS",
//            subCategory = arrayListOf(Items("All Bracelets"),
//                Items("Casual"),
//                Items("Bangles"),
//                Items("Occasion"),
//                Items("Everyday"))
//        ))
//        mainCategory.add(Items("MANGALSUTRA",
//            subCategory = arrayListOf(Items("All Mangalsutra"),
//                Items("Casual"),
//                Items("Bangles"),
//                Items("Everyday"))
//        ))
//        mainCategory.add(Items("EARRINGS",
//            subCategory = arrayListOf(Items("All Earrings"),
//                Items("Balis"),
//                Items("Studs"),
//                Items("Drops"),
//                Items("Hoops"))
//        ))

//        itemMaterial.add(Items("Gold 14 K"))
//        itemMaterial.add(Items("Gold 18 K"))
//        itemMaterial.add(Items("Platinum"))
//
//        itemShopFor.add(Items("Men"))
//        itemShopFor.add(Items("Women"))
//        itemShopFor.add(Items("Kids"))
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

//                    if (dataClass.isSelected == true) {
//                        arrayPrice.add(dataClass.name)
//                    } else {
//                        arrayPrice.remove(dataClass.name)
//                    }
//                    arrayPrice.distinct()
                    notifyItemChanged(position)
                }

                val filteredNot = currentList.filter {
                    it.isSelected == true
                }
                itemPriceCount.value = filteredNot.size
            }
        }
    }


    val categoryAdapter = object : GenericAdapter<ItemFilterCategoryBinding, Items>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemFilterCategoryBinding.inflate(inflater, parent, false)

        @SuppressLint("NotifyDataSetChanged")
        override fun onBindHolder(
            binding: ItemFilterCategoryBinding,
            dataClass: Items,
            position: Int
        ) {
            binding.apply {
                if (dataClass.isSelected == true) {
                    dataClass.subCategory.forEach {
                        it.isSelected = true
                    }
                } else {
                    dataClass.subCategory.forEach {
//                       it.isSelected = false
//                        if (it.isSelected == true){
//                            it.isSelected = true
//                        } else {
                            it.isSelected = false
//                        }
                    }
                }

                dataClass.subCategory.forEach {
                    Log.e("TAG", "dataClass "+dataClass.isSelected+" it "+it.isSelected)
                }



//                if (isFilterFromFrom){
//
//                } else {
//                    if (dataClass.isSelected == false) {
//                        dataClass.subCategory.forEach {
//                            it.isSelected = false
//                        }
//                    }
//                }


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

                textItem.singleClick {
//                    isFilterFromFrom = false
                    selectedPosition = position
                    dataClass.isSelected = !dataClass.isSelected

//                    currentList.forEach {
//                        if (dataClass.isSelected == false){
//                            it.isSelected = false
//                            it.isChildSelect = false
//                        } else {
//                            it.isSelected = true
//                            it.isChildSelect = true
//                        }
//                    }

                    notifyItemChanged(position)
                }

                val filteredNot = currentList
                itemCategoryCount.value = currentList.filter { it.isSelected == true }.size
//                itemCategoryCount.value = filteredNot.size

                layoutChild.visibility = if(dataClass.isCollapse == true) View.VISIBLE else View.GONE
                ivHideShow.setImageDrawable(
                    ContextCompat.getDrawable(
                        root.context,
                        if (dataClass.isCollapse == true) R.drawable.baseline_add_24 else R.drawable.baseline_remove_24
                    )
                )
                ivHideShow.singleClick {
                    dataClass.isCollapse = !dataClass.isCollapse
                    notifyItemChanged(position)
                }

                val categoryChildAdapter = object : GenericAdapter<ItemFilterChildBinding, Items>() {
                    override fun onCreateView(
                        inflater: LayoutInflater,
                        parent: ViewGroup,
                        viewType: Int
                    ) = ItemFilterChildBinding.inflate(inflater, parent, false)

                    @SuppressLint("NotifyDataSetChanged")
                    override fun onBindHolder(
                        bindingChild: ItemFilterChildBinding,
                        dataClassChild: Items,
                        positionChild: Int
                    ) {
                        bindingChild.apply {
                            textItemChild.text = dataClassChild.name

                            root.singleClick {
                                isFilterFromFrom = true
                                selectedPosition = positionChild
                                dataClassChild.isSelected = !dataClassChild.isSelected
                                dataClassChild.isChildSelect = !dataClassChild.isSelected



                                notifyItemChanged(positionChild)
                            }

                            val filteredNotChild = currentList.filter { it.isSelected == true }
                            //itemCategoryCount.value = filteredNot.size

                            Log.e("TAG" , "filteredNotChild ${filteredNotChild.size}   currentList${currentList.size}")

                            if(filteredNotChild.size == (currentList.size)){
                                dataClass.isSelected = true
                            } else {
                                dataClass.isSelected = false
                            }

                            itemCategoryCount.value = filteredNot.filter { it.isSelected == true }.size
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

                            if (dataClassChild.isSelected == true){
//                                if (dataClassChild.isChildSelect == true){
//                                    textItemChild.setTypeface(typefaceroboto_light)
//                                } else {
                                    textItemChild.setTypeface(typefaceroboto_medium)
//                                }
                            } else {
//                                if (dataClassChild.isChildSelect == true){
                                    textItemChild.setTypeface(typefaceroboto_light)
//                                } else {
//                                    textItemChild.setTypeface(typefaceroboto_medium)
//                                }
                            }


//                            textItemChild.setTypeface(if (dataClassChild.isSelected == true) typefaceroboto_medium else typefaceroboto_light)



                            ivIconCheckChild.imageTintList =
                                if (dataClassChild.isSelected == true) ContextCompat.getColorStateList(
                                    bindingChild.root.context,
                                    R.color.app_color
                                )
                                else ContextCompat.getColorStateList(
                                    bindingChild.root.context,
                                    R.color._D9D9D9
                                )
                        }
                    }
                }

                rvListChild.setHasFixedSize(true)
                rvListChild.adapter = categoryChildAdapter
                categoryChildAdapter.notifyDataSetChanged()
                categoryChildAdapter.submitList(dataClass.subCategory)


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
//                    if (dataClass.isSelected == true) {
//                        arrayMaterial.add(dataClass.name)
//                    } else {
//                        arrayMaterial.remove(dataClass.name)
//                    }
//                    arrayMaterial.distinct()
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
//                    if (dataClass.isSelected == true) {
//                        arrayShopFor.add(dataClass.name)
//                    } else {
//                        arrayShopFor.remove(dataClass.name)
//                    }
//                    arrayShopFor.distinct()
                    notifyItemChanged(position)
                }


                val filteredNot = currentList.filter { it.isSelected == true }
                itemShopForCount.value = filteredNot.size

            }
        }
    }

}
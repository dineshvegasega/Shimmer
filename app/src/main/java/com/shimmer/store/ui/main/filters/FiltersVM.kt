package com.shimmer.store.ui.main.filters

import android.annotation.SuppressLint
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
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FiltersVM @Inject constructor() : ViewModel() {
    var itemPriceCount = MutableLiveData<Int>(0)
    var item1 : ArrayList<Items> = ArrayList()
    var item2 : ArrayList<String> = ArrayList()
    var item3 : ArrayList<String> = ArrayList()


    init {
        item1.add(Items())
        item1.add(Items())
        item1.add(Items())
        item1.add(Items())
        item1.add(Items())
        item1.add(Items())
        item1.add(Items())
        item1.add(Items())

        item2.add("1")
        item2.add("2")

        item3.add("1")
        item3.add("2")
        item3.add("3")
        item3.add("4")
    }


    var selectedPosition = -1

    val recentAdapter = object : GenericAdapter<ItemFilterBinding, Items>() {
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

                textItem.setTypeface(if(dataClass.isSelected == true) typefaceroboto_medium else typefaceroboto_light)
                ivIconCheck.imageTintList = if(dataClass.isSelected == true) ContextCompat.getColorStateList(binding.root.context,
                    R.color.app_color)
                else ContextCompat.getColorStateList(binding.root.context,
                    R.color._D9D9D9)

                root.singleClick {
                    selectedPosition = position
                    dataClass.isSelected = !dataClass.isSelected
                    notifyItemChanged(position)
                }


                val filteredNot = currentList.filter { it.isSelected == true }
                itemPriceCount.value = filteredNot.size

//                list.forEach {
//                    if(it.isSelected){
//
//                    }
//                }

            }
        }
    }




}
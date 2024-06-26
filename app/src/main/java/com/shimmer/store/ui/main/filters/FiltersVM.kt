package com.shimmer.store.ui.main.filters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import com.shimmer.store.databinding.ItemFilterBinding
import com.shimmer.store.databinding.ItemProductDiamondsBinding
import com.shimmer.store.genericAdapter.GenericAdapter
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.typefaceroboto_light
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.typefaceroboto_medium
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FiltersVM @Inject constructor() : ViewModel() {


    var item1 : ArrayList<String> = ArrayList()
    var item2 : ArrayList<String> = ArrayList()
    var item3 : ArrayList<String> = ArrayList()


    init {
        item1.add("1")
        item1.add("2")
        item1.add("3")
        item1.add("1")
        item1.add("2")
        item1.add("3")
        item1.add("1")
        item1.add("2")
        item1.add("3")

        item2.add("1")
        item2.add("2")

        item3.add("1")
        item3.add("2")
        item3.add("3")
        item3.add("4")
    }


    var selectedPosition = -1

    val recentAdapter = object : GenericAdapter<ItemFilterBinding, String>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemFilterBinding.inflate(inflater, parent, false)

        @SuppressLint("NotifyDataSetChanged")
        override fun onBindHolder(
            binding: ItemFilterBinding,
            dataClass: String,
            position: Int
        ) {
            binding.apply {

                textItem.setTypeface(if(selectedPosition == position) typefaceroboto_medium else typefaceroboto_light)

                root.singleClick {
                    selectedPosition = position
                    notifyDataSetChanged()
                }

            }
        }
    }




}
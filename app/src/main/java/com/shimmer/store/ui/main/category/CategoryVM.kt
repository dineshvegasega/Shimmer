package com.shimmer.store.ui.main.category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.shimmer.store.R
import com.shimmer.store.databinding.ItemCategoryRoundBinding
import com.shimmer.store.databinding.ItemFaqBinding
import com.shimmer.store.databinding.ItemHomeCategoryBinding
import com.shimmer.store.genericAdapter.GenericAdapter
import com.shimmer.store.models.Items
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class CategoryVM @Inject constructor() : ViewModel() {
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





    val subCategoryAdapter1 = object : GenericAdapter<ItemCategoryRoundBinding, Items>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemCategoryRoundBinding.inflate(inflater, parent, false)

        override fun onBindHolder(
            binding: ItemCategoryRoundBinding,
            dataClass: Items,
            position: Int
        ) {
            binding.apply {
                textName.text = dataClass.name


            }
        }
    }



    val subCategoryAdapter2 = object : GenericAdapter<ItemCategoryRoundBinding, Items>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemCategoryRoundBinding.inflate(inflater, parent, false)

        override fun onBindHolder(
            binding: ItemCategoryRoundBinding,
            dataClass: Items,
            position: Int
        ) {
            binding.apply {
                textName.text = dataClass.name
            }
        }
    }

}
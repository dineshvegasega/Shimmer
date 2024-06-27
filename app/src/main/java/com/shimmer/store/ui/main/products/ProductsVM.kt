package com.shimmer.store.ui.main.products

import androidx.lifecycle.ViewModel
import com.shimmer.store.models.Items
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductsVM @Inject constructor() : ViewModel() {

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

}
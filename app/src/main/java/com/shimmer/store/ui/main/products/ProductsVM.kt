package com.shimmer.store.ui.main.products

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductsVM @Inject constructor() : ViewModel() {

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

}
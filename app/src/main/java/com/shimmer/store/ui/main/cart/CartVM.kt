package com.shimmer.store.ui.main.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import com.shimmer.store.databinding.ItemCartBinding
import com.shimmer.store.databinding.ItemProductDiamondsBinding
import com.shimmer.store.genericAdapter.GenericAdapter
import com.shimmer.store.models.Items
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CartVM @Inject constructor() : ViewModel() {

    var item1 : ArrayList<Items> = ArrayList()
    var item2 : ArrayList<String> = ArrayList()
    var item3 : ArrayList<String> = ArrayList()


    init {
        item1.add(Items("https://v2.streetsaarthi.in//uploads//1704703414Vishwakarma%20Scheme.jpeg"))
        item1.add(Items("https://v2.streetsaarthi.in//uploads//1704703414Vishwakarma%20Scheme.jpeg"))
        item1.add(Items("https://v2.streetsaarthi.in//uploads//1704703414Vishwakarma%20Scheme.jpeg"))
        item1.add(Items("https://v2.streetsaarthi.in//uploads//1704703414Vishwakarma%20Scheme.jpeg"))
        item1.add(Items("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"))
        item1.add(Items("https://v2.streetsaarthi.in//uploads//1704703414Vishwakarma%20Scheme.jpeg"))
        item1.add(Items("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4"))
        item1.add(Items("https://v2.streetsaarthi.in//uploads//1704703414Vishwakarma%20Scheme.jpeg"))



        item2.add("1")
        item2.add("2")

        item3.add("1")
        item3.add("2")
        item3.add("3")
        item3.add("4")
    }




    val recentAdapter = object : GenericAdapter<ItemCartBinding, Items>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemCartBinding.inflate(inflater, parent, false)

        override fun onBindHolder(
            binding: ItemCartBinding,
            dataClass: Items,
            position: Int
        ) {
            binding.apply {
//                recyclerViewRecentItems.setHasFixedSize(true)
//                val headlineAdapter = RecentChildAdapter(
//                    binding.root.context,
//                    dataClass.items,
//                    position
//                )
//                recyclerViewRecentItems.adapter = headlineAdapter
//                recyclerViewRecentItems.layoutManager = LinearLayoutManager(binding.root.context)
            }
        }
    }




}
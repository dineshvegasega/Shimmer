package com.shimmer.store.ui.main.search

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.shimmer.store.R
import com.shimmer.store.databinding.ItemFaqBinding
import com.shimmer.store.databinding.ItemSearchBinding
import com.shimmer.store.databinding.ItemSearchHistoryBinding
import com.shimmer.store.datastore.db.SearchModel
import com.shimmer.store.genericAdapter.GenericAdapter
import com.shimmer.store.models.Items
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.db
import com.shimmer.store.utils.ioThread
import com.shimmer.store.utils.mainThread
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchVM @Inject constructor() : ViewModel() {

    var item1 : ArrayList<Items> = ArrayList()


    init {
        item1.add(Items(name = "https://v2.streetsaarthi.in//uploads//1704703414Vishwakarma%20Scheme.jpeg"))
        item1.add(Items(name = "https://v2.streetsaarthi.in//uploads//1704703414Vishwakarma%20Scheme.jpeg"))
        item1.add(Items(name = "https://v2.streetsaarthi.in//uploads//1704703414Vishwakarma%20Scheme.jpeg"))
        item1.add(Items(name = "https://v2.streetsaarthi.in//uploads//1704703414Vishwakarma%20Scheme.jpeg"))
        item1.add(Items(name = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"))
        item1.add(Items(name = "https://v2.streetsaarthi.in//uploads//1704703414Vishwakarma%20Scheme.jpeg"))
        item1.add(Items(name = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4"))
        item1.add(Items(name = "https://v2.streetsaarthi.in//uploads//1704703414Vishwakarma%20Scheme.jpeg"))
        item1.add(Items(name = "https://v2.streetsaarthi.in//uploads//1704703414Vishwakarma%20Scheme.jpeg"))
        item1.add(Items(name = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4"))
        item1.add(Items(name = "https://v2.streetsaarthi.in//uploads//1704703414Vishwakarma%20Scheme.jpeg"))

    }





    val searchAdapter = object : GenericAdapter<ItemSearchBinding, Items>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemSearchBinding.inflate(inflater, parent, false)

        override fun onBindHolder(
            binding: ItemSearchBinding,
            dataClass: Items,
            position: Int
        ) {
            binding.apply {
//                textDesc.visibility =
//                    if (dataClass.isCollapse == true) View.VISIBLE else View.GONE
//                ivHideShow.setImageDrawable(
//                    ContextCompat.getDrawable(
//                        root.context,
//                        if (dataClass.isCollapse == true) R.drawable.baseline_remove_24 else R.drawable.baseline_add_24
//                    )
//                )
                root.singleClick {
                    root.findNavController().navigate(R.id.action_search_to_productDetail)
                }
            }
        }
    }



    var searchDelete = MutableLiveData<Boolean>(false)
    var searchValue = MutableLiveData<String?>("")

    val searchHistoryAdapter = object : GenericAdapter<ItemSearchHistoryBinding, SearchModel>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemSearchHistoryBinding.inflate(inflater, parent, false)

        override fun onBindHolder(
            binding: ItemSearchHistoryBinding,
            dataClass: SearchModel,
            position: Int
        ) {
            binding.apply {
                textItem.text = dataClass.search_name
//                textDesc.visibility =
//                    if (dataClass.isCollapse == true) View.VISIBLE else View.GONE
//                ivHideShow.setImageDrawable(
//                    ContextCompat.getDrawable(
//                        root.context,
//                        if (dataClass.isCollapse == true) R.drawable.baseline_remove_24 else R.drawable.baseline_add_24
//                    )
//                )

                textItem.singleClick {
                    searchValue.value = dataClass.search_name
                }



                ivCross.singleClick {
                    mainThread {
                        db?.searchDao()?.delete(dataClass)
                        searchDelete.value = true
                    }
                }



            }
        }
    }

}
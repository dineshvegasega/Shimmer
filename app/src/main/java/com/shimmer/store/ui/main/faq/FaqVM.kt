package com.shimmer.store.ui.main.faq

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shimmer.store.R
import com.shimmer.store.databinding.ItemFaqBinding
import com.shimmer.store.databinding.ItemProductDiamondsBinding
import com.shimmer.store.datastore.db.CartModel
import com.shimmer.store.genericAdapter.GenericAdapter
import com.shimmer.store.models.ItemSizes
import com.shimmer.store.models.Items
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.db
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class FaqVM @Inject constructor() : ViewModel() {

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



    val recentAdapter = object : GenericAdapter<ItemFaqBinding, Items>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemFaqBinding.inflate(inflater, parent, false)

        override fun onBindHolder(
            binding: ItemFaqBinding,
            dataClass: Items,
            position: Int
        ) {
            binding.apply {

                textDesc.visibility =
                    if (dataClass.isCollapse == true) View.VISIBLE else View.GONE
                ivHideShow.setImageDrawable(
                    ContextCompat.getDrawable(
                        root.context,
                        if (dataClass.isCollapse == true) R.drawable.baseline_remove_24 else R.drawable.baseline_add_24
                    )
                )
                ivHideShow.singleClick {
                    dataClass.isCollapse = !dataClass.isCollapse
                    notifyItemChanged(position)
                }

            }
        }
    }


}
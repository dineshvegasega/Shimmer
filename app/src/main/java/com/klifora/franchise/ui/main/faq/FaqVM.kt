package com.klifora.franchise.ui.main.faq

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klifora.franchise.R
import com.klifora.franchise.databinding.ItemFaqBinding
import com.klifora.franchise.datastore.db.CartModel
import com.klifora.franchise.genericAdapter.GenericAdapter
import com.klifora.franchise.models.Items
import com.klifora.franchise.networking.Repository
import com.klifora.franchise.ui.mainActivity.MainActivity.Companion.db
import com.klifora.franchise.utils.singleClick
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class FaqVM @Inject constructor(private val repository: Repository) : ViewModel() {

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


    var selectedPosition = -1
    val faqAdapter = object : GenericAdapter<ItemFaqBinding, Items>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemFaqBinding.inflate(inflater, parent, false)

        @SuppressLint("NotifyDataSetChanged")
        override fun onBindHolder(
            binding: ItemFaqBinding,
            dataClass: Items,
            position: Int
        ) {
            binding.apply {
//                textDesc.visibility =
//                    if (dataClass.isCollapse == true) View.VISIBLE else View.GONE
//                ivHideShow.setImageDrawable(
//                    ContextCompat.getDrawable(
//                        root.context,
//                        if (dataClass.isCollapse == true) R.drawable.arrow_down else R.drawable.arrow_right
//                    )
//                )

                textDesc.visibility =
                    if (selectedPosition == position) View.VISIBLE else View.GONE
                ivHideShow.setImageDrawable(
                    ContextCompat.getDrawable(
                        root.context,
                        if (selectedPosition == position) R.drawable.arrow_down else R.drawable.arrow_right
                    )
                )


                root.singleClick {
                    if (selectedPosition == position){
                        if (textDesc.isVisible == true){
                            selectedPosition = -1
                        }
                        if (textDesc.isVisible == false){
                            selectedPosition = position
                        }
                    } else{
                        selectedPosition = position
                    }
                    notifyDataSetChanged()
                }
            }
        }
    }


}
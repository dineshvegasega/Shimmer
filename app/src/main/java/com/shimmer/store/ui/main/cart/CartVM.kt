package com.shimmer.store.ui.main.cart

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.shimmer.store.R
import com.shimmer.store.databinding.ItemCartBinding
import com.shimmer.store.databinding.ItemProductDiamondsBinding
import com.shimmer.store.datastore.db.CartModel
import com.shimmer.store.genericAdapter.GenericAdapter
import com.shimmer.store.models.Items
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.db
import com.shimmer.store.utils.mainThread
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CartVM @Inject constructor() : ViewModel() {

//    var item1 : ArrayList<Items> = ArrayList()
//    var item2 : ArrayList<String> = ArrayList()
//    var item3 : ArrayList<String> = ArrayList()


    init {
//        item1.add(Items("https://v2.streetsaarthi.in//uploads//1704703414Vishwakarma%20Scheme.jpeg"))
//        item1.add(Items("https://v2.streetsaarthi.in//uploads//1704703414Vishwakarma%20Scheme.jpeg"))
//        item1.add(Items("https://v2.streetsaarthi.in//uploads//1704703414Vishwakarma%20Scheme.jpeg"))
//        item1.add(Items("https://v2.streetsaarthi.in//uploads//1704703414Vishwakarma%20Scheme.jpeg"))
//        item1.add(Items("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"))
//        item1.add(Items("https://v2.streetsaarthi.in//uploads//1704703414Vishwakarma%20Scheme.jpeg"))
//        item1.add(Items("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4"))
//        item1.add(Items("https://v2.streetsaarthi.in//uploads//1704703414Vishwakarma%20Scheme.jpeg"))
//
//
//
//        item2.add("1")
//        item2.add("2")
//
//        item3.add("1")
//        item3.add("2")
//        item3.add("3")
//        item3.add("4")
    }



    var cartMutableList = MutableLiveData<Boolean>(false)
    val cartAdapter = object : GenericAdapter<ItemCartBinding, CartModel>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemCartBinding.inflate(inflater, parent, false)

        @SuppressLint("NotifyDataSetChanged")
        override fun onBindHolder(
            binding: ItemCartBinding,
            dataClass: CartModel,
            position: Int
        ) {
            binding.apply {

                ivIcon.singleClick {
                    binding.root.findNavController().navigate(R.id.action_cart_to_productDetail)
                }

                textTitle.text = dataClass.name
                ivCount.text = dataClass.quantity.toString()
                textPrice.text = "Price: ₹"+dataClass.price.toString()

                ivMinus.singleClick {
                    if (dataClass.quantity > 1) {
                        dataClass.quantity--
                        mainThread {
                            db?.cartDao()?.updateById(dataClass.product_id!!, dataClass.quantity)
                            val userList: List<CartModel> ?= db?.cartDao()?.getAll()
                            userList?.forEach {
                                Log.e("TAG", "onViewCreated: "+it.name + " it.currentTime "+it.quantity)
                            }
                        }
                        notifyItemChanged(position)
                    }
                }

                ivPlus.singleClick {
                    dataClass.quantity++
                    mainThread {
                        db?.cartDao()?.updateById(dataClass.product_id!!, dataClass.quantity)
                        val userList: List<CartModel> ?= db?.cartDao()?.getAll()
                        userList?.forEach {
                            Log.e("TAG", "onViewCreated: "+it.name + " it.currentTime "+it.quantity)
                        }
                    }
                    notifyItemChanged(position)
                }

                btDelete.singleClick {
                    mainThread {
                        db?.cartDao()?.deleteById(dataClass.product_id!!)
//                        val userList: List<CartModel> ?= db?.cartDao()?.getAll()
//                        userList?.forEach {
//                            Log.e("TAG", "onViewCreated: "+it.name + " it.currentTime "+it.quantity)
//                        }
                    }
//                    Log.e("TAG", "onViewCreated: "+position)
//
//                    item1.removeAt(position)
//                    notifyDataSetChanged()

                    cartMutableList.value = true

                }
            }
        }
    }




}
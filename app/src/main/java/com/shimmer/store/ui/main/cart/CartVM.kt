package com.shimmer.store.ui.main.cart

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.shimmer.store.R
import com.shimmer.store.databinding.ItemCartBinding
import com.shimmer.store.datastore.db.CartModel
import com.shimmer.store.genericAdapter.GenericAdapter
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.db
import com.shimmer.store.utils.getPatternFormat
import com.shimmer.store.utils.mainThread
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CartVM @Inject constructor() : ViewModel() {

//    var refreshPrice = MutableLiveData<Boolean>(false)
    var subTotalPrice : Double = 0.0
    var discountPrice : Double = 10.0
    var shippingPrice : Double = 100.0
    var cgstPrice : Double = 9.0
    var sgstPrice : Double = 9.0
    var totalPrice : Double = 0.0

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
                    binding.root.findNavController().navigate(R.id.action_cart_to_productDetail, Bundle().apply {
                        putString("sku", dataClass.sku)
                    })
                }

                textTitle.text = dataClass.name
                textDesc.text =  "SKU: "+dataClass.sku

                ivCount.text = dataClass.quantity.toString()
                textPrice.text = "Price: ₹"+getPatternFormat("1", dataClass.price!!)

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
                    cartMutableList.value = true
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
                    cartMutableList.value = true
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
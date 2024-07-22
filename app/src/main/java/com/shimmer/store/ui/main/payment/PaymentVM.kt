package com.shimmer.store.ui.main.payment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.shimmer.store.R
import com.shimmer.store.databinding.ItemPaymentProductsBinding
import com.shimmer.store.datastore.db.CartModel
import com.shimmer.store.genericAdapter.GenericAdapter
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.db
import com.shimmer.store.utils.getPatternFormat
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentVM @Inject constructor() : ViewModel() {

    var subTotalPrice : Double = 0.0
    var discountPrice : Double = 10.0
    var shippingPrice : Double = 100.0
    var cgstPrice : Double = 9.0
    var sgstPrice : Double = 9.0
    var totalPrice : Double = 0.0

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


    val ordersAdapter = object : GenericAdapter<ItemPaymentProductsBinding, CartModel>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemPaymentProductsBinding.inflate(inflater, parent, false)

        @SuppressLint("NotifyDataSetChanged")
        override fun onBindHolder(
            binding: ItemPaymentProductsBinding,
            dataClass: CartModel,
            position: Int
        ) {
            binding.apply {

                textTitle.text = dataClass.name
                textPrice.text = "Price: ₹"+getPatternFormat("1", dataClass.price!!) + " x "+dataClass.quantity + " = ₹"+getPatternFormat("1", (dataClass.price?.times(dataClass.quantity.toDouble())))

                ivIcon.singleClick {
                    binding.root.findNavController().navigate(R.id.action_payment_to_productDetail)
                }

//                ivCount.text = dataClass.quantity.toString()
//
//                ivMinus.singleClick {
//                    if (dataClass.quantity > 0) {
//                        dataClass.quantity--
//                        notifyItemChanged(position)
//                    }
//                }
//
//                ivPlus.singleClick {
//                    dataClass.quantity++
//                    notifyItemChanged(position)
//                }
//
//                btDelete.singleClick {
//                    item1.removeAt(position)
//                    notifyDataSetChanged()
//                }
            }
        }
    }


}
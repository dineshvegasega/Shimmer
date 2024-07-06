package com.shimmer.store.ui.main.payment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.shimmer.store.R
import com.shimmer.store.databinding.ItemCartBinding
import com.shimmer.store.databinding.ItemPaymentProductsBinding
import com.shimmer.store.genericAdapter.GenericAdapter
import com.shimmer.store.models.Items
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PaymentVM @Inject constructor() : ViewModel() {


    var item1 : ArrayList<Items> = ArrayList()


    init {
        item1.add(Items("https://v2.streetsaarthi.in//uploads//1704703414Vishwakarma%20Scheme.jpeg"))
        item1.add(Items("https://v2.streetsaarthi.in//uploads//1704703414Vishwakarma%20Scheme.jpeg"))
    }





    val ordersAdapter = object : GenericAdapter<ItemPaymentProductsBinding, Items>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemPaymentProductsBinding.inflate(inflater, parent, false)

        @SuppressLint("NotifyDataSetChanged")
        override fun onBindHolder(
            binding: ItemPaymentProductsBinding,
            dataClass: Items,
            position: Int
        ) {
            binding.apply {

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
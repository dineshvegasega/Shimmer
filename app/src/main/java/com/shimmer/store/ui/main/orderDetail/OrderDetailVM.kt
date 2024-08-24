package com.shimmer.store.ui.main.orderDetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.shimmer.store.R
import com.shimmer.store.databinding.ItemOrderHistoryBinding
import com.shimmer.store.databinding.ItemSkusBinding
import com.shimmer.store.genericAdapter.GenericAdapter
import com.shimmer.store.models.Items
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderDetailVM @Inject constructor() : ViewModel() {

    val orderSKU = object : GenericAdapter<ItemSkusBinding, String>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemSkusBinding.inflate(inflater, parent, false)

        override fun onBindHolder(
            binding: ItemSkusBinding,
            dataClass: String,
            position: Int
        ) {
            binding.apply {
                root.singleClick {
                   // root.findNavController().navigate(R.id.action_orders_to_orderDetail)
                }
            }
        }
    }



}
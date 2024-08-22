package com.shimmer.store.ui.main.orders

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.shimmer.store.R
import com.shimmer.store.databinding.ItemCategoryBinding
import com.shimmer.store.databinding.ItemCustomerOrderBinding
import com.shimmer.store.databinding.ItemOrderHistoryBinding
import com.shimmer.store.databinding.LoaderBinding
import com.shimmer.store.datastore.db.CartModel
import com.shimmer.store.genericAdapter.GenericAdapter
import com.shimmer.store.models.Items
import com.shimmer.store.ui.main.category.CategoryChildTab.Companion.mainSelect
import com.shimmer.store.ui.mainActivity.MainActivity
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.db
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.mainCategory
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.mainMaterial
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.mainPrice
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.mainShopFor
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersVM @Inject constructor() : ViewModel() {

    var ordersTypesArray: ArrayList<Items> = ArrayList()

    var alertDialog: AlertDialog? = null
    var positionSelect = 0


    fun show() {
        viewModelScope.launch {
            if (alertDialog != null) {
                alertDialog?.dismiss()
                alertDialog?.show()
            }
        }
    }

    fun hide() {
        viewModelScope.launch {
            if (alertDialog != null) {
                alertDialog?.dismiss()
            }
        }
    }


    init {
        val alert = AlertDialog.Builder(MainActivity.activity.get())
        val binding =
            LoaderBinding.inflate(LayoutInflater.from(MainActivity.activity.get()), null, false)
        alert.setView(binding.root)
        alert.setCancelable(false)
        alertDialog = alert.create()
        alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        ordersTypesArray.add(Items(id = 10, name = "Order History"))
        ordersTypesArray.add(Items(id = 11, name = "Customer Orders"))
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





    val orderHistory = object : GenericAdapter<ItemOrderHistoryBinding, Items>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemOrderHistoryBinding.inflate(inflater, parent, false)

        override fun onBindHolder(
            binding: ItemOrderHistoryBinding,
            dataClass: Items,
            position: Int
        ) {
            binding.apply {
                root.singleClick {
                    root.findNavController().navigate(R.id.action_orders_to_orderDetail)
                }
            }
        }
    }


    val customerOrders = object : GenericAdapter<ItemCustomerOrderBinding, Items>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemCustomerOrderBinding.inflate(inflater, parent, false)

        override fun onBindHolder(
            binding: ItemCustomerOrderBinding,
            dataClass: Items,
            position: Int
        ) {
            binding.apply {
                root.singleClick {
                    root.findNavController().navigate(R.id.action_orders_to_orderDetail)
                }
            }
        }
    }



}
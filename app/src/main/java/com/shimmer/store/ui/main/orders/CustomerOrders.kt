package com.shimmer.store.ui.main.orders

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import com.shimmer.store.databinding.CategoryChildTabBinding
import com.shimmer.store.databinding.CustomerOrdersBinding
import com.shimmer.store.models.Items
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CustomerOrders (
    private val activity: FragmentActivity,
    private val videoPath: Items,
    position: Int
) : Fragment() {
    private var _binding: CustomerOrdersBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OrdersVM by viewModels()

    companion object {
        @JvmStatic
        var mainSelect = 0
//        lateinit var adapter2: CategoryChildTabAdapter
    }


//    var adapter2: CategoryChildTabAdapter ?= null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CustomerOrdersBinding.inflate(inflater)
        return binding.root
    }
    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        adapter2 = CategoryChildTabAdapter()

        binding.apply {
//            adapter2.submitData(viewModel.item1)
//            adapter2.notifyDataSetChanged()
//            binding.rvList2.adapter = adapter2

            rvListCategory1.setHasFixedSize(true)
            viewModel.customerOrders.notifyDataSetChanged()
            viewModel.customerOrders.submitList(viewModel.ordersTypesArray)
            rvListCategory1.adapter = viewModel.customerOrders

        }

    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
//        Log.e("TAG", "onViewCreated: Fragment Position : ${videoPath.name}")
//        mainShopFor.forEach {
//            if (videoPath.name == it.name){
//                it.isSelected = true
//                Log.e("TAG", "onViewCreated: Fragment PositionIF : ${it.name}")
//            } else {
//                it.isSelected = false
//                Log.e("TAG", "onViewCreated: Fragment PositionELSE : ${it.name}")
//            }
//        }

        viewModel.customerOrders.notifyDataSetChanged()
    }
}
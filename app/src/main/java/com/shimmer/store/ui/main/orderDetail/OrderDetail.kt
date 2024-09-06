package com.shimmer.store.ui.main.orderDetail

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.shimmer.store.databinding.OrderDetailBinding
import com.shimmer.store.models.guestOrderList.ItemGuestOrderListItem
import com.shimmer.store.ui.mainActivity.MainActivity
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OrderDetail : Fragment() {
    private var _binding: OrderDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OrderDetailVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = OrderDetailBinding.inflate(inflater)
        return binding.root
    }
    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MainActivity.mainActivity.get()!!.callBack(2)
        binding.apply {
//            rvListCategory1.setHasFixedSize(true)
//            viewModel.customerOrders.notifyDataSetChanged()
//            viewModel.customerOrders.submitList(viewModel.ordersTypesArray)
//            rvListCategory1.adapter = viewModel.customerOrders


            topBarBack.includeBackButton.apply {
                layoutBack.singleClick {
                    findNavController().navigateUp()
                }
            }
            topBarBack.ivCartLayout.visibility = View.GONE

//            topBar.apply {
//                textViewTitle.visibility = View.VISIBLE
////                cardSearch.visibility = View.GONE
//                ivSearch.visibility = View.GONE
//                ivCartLayout.visibility = View.GONE
//                textViewTitle.text = "Order Detail"
//
//                appicon.setImageDrawable(
//                    ContextCompat.getDrawable(
//                        MainActivity.context.get()!!,
//                        R.drawable.baseline_west_24
//                    )
//                )
//
//                appicon.singleClick {
//                    findNavController().navigateUp()
//                }
//            }



            val data = arguments?.getString("key")
            Log.e("TAG", "onViewCreated: $data")
            if(data?.length!! > 2){
                val token = data?.substring(1, data.toString().length - 1)
                val element: ItemGuestOrderListItem = Gson().fromJson(token, ItemGuestOrderListItem::class.java)
                Log.e("TAG", "elementelement: $element")
            }





            rvListCategory1.setHasFixedSize(true)
            viewModel.orderSKU.notifyDataSetChanged()
            viewModel.orderSKU.submitList(arrayListOf("","",""))
            rvListCategory1.adapter = viewModel.orderSKU




        }

    }


}
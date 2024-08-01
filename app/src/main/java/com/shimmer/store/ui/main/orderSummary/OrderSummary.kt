package com.shimmer.store.ui.main.orderSummary

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.shimmer.store.R
import com.shimmer.store.databinding.OrderSummaryBinding
import com.shimmer.store.databinding.PaymentBinding
import com.shimmer.store.datastore.db.CartModel
import com.shimmer.store.ui.mainActivity.MainActivity
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.db
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.isBackStack
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.loginType
import com.shimmer.store.utils.getPatternFormat
import com.shimmer.store.utils.mainThread
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderSummary : Fragment() {
    private val viewModel: OrderSummaryVM by viewModels()
    private var _binding: OrderSummaryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = OrderSummaryBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged", "ClickableViewAccessibility", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isBackStack = true


        binding.apply {
            topBar.apply {
                textViewTitle.visibility = View.VISIBLE
//                cardSearch.visibility = View.GONE
                ivSearch.visibility = View.GONE
                ivCartLayout.visibility = View.GONE
                textViewTitle.text = "YOUR ORDER"

                appicon.setImageDrawable(
                    ContextCompat.getDrawable(
                        MainActivity.context.get()!!,
                        R.drawable.baseline_west_24
                    )
                )

                appicon.singleClick {
                    findNavController().navigateUp()
                }
            }


            when(loginType){
                "vendor" ->  {
                    groupVendor.visibility = View.VISIBLE
                    groupGuest.visibility = View.VISIBLE
                    textPayment.text = resources.getString(R.string.proceed_to_payment)
                }
                "guest" ->  {
                    groupVendor.visibility = View.GONE
                    groupGuest.visibility = View.VISIBLE
                    textPayment.text = resources.getString(R.string.select_franchise)
                }
            }


            mainThread {
                val userList: List<CartModel>? = db?.cartDao()?.getAll()
                binding.apply {
                    rvList.setHasFixedSize(true)
                    rvList.adapter = viewModel.ordersAdapter
                    viewModel.ordersAdapter.notifyDataSetChanged()
                    viewModel.ordersAdapter.submitList(userList)
                }

                var price : Double = 0.0
                userList?.forEach {
                    price += (it.price!! * it.quantity)
                    Log.e(
                        "TAG",
                        "onViewCreated: " + it.name + " it.currentTime " + it.currentTime
                    )
                }

                viewModel.subTotalPrice = price
                textSubtotalPrice.text = "₹${getPatternFormat("1", price)}"

                val discountPriceAfter = (viewModel.subTotalPrice * viewModel.discountPrice) / 100
                textDiscountPrice.text = "₹${getPatternFormat("1", discountPriceAfter)}"

                val priceANDdiscountPrice = price + discountPriceAfter

                val cstPriceAfter = (priceANDdiscountPrice * viewModel.cgstPrice ) / 100
                textCGSTPrice.text = "₹${getPatternFormat("1", cstPriceAfter)}"

                val sgstPriceAfter = (priceANDdiscountPrice * viewModel.sgstPrice) / 100
                textSGSTPrice.text = "₹${getPatternFormat("1", sgstPriceAfter)}"

                val priceANDGSTPrice = priceANDdiscountPrice + (cstPriceAfter + sgstPriceAfter) + viewModel.shippingPrice
                textTotalPrice.text = "₹${getPatternFormat("1", priceANDGSTPrice)}"

                textShippingPrice.text = "₹${getPatternFormat("1", viewModel.shippingPrice)}"


                textDiscount.text = "Discount (${viewModel.discountPrice}%)"
                textCGST.text = "CGST (${viewModel.cgstPrice}%)"
                textSGST.text = "SGST (${viewModel.sgstPrice}%)"
            }

//            rvList.setHasFixedSize(true)
//            rvList.adapter = viewModel.ordersAdapter
//            viewModel.ordersAdapter.notifyDataSetChanged()
//            viewModel.ordersAdapter.submitList(viewModel.item1)

            layoutSort.singleClick {
                when(loginType){
                    "vendor" ->  {
                        findNavController().navigate(R.id.action_orderSummary_to_home)
                    }
                    "guest" ->  {
                        findNavController().navigate(R.id.action_orderSummary_to_franchiseList)
                    }
                }
            }
        }
    }
}
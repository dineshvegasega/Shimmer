package com.shimmer.store.ui.main.cart

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
import com.shimmer.store.databinding.CartBinding
import com.shimmer.store.datastore.DataStoreKeys.ADMIN_TOKEN
import com.shimmer.store.datastore.DataStoreKeys.CUSTOMER_TOKEN
import com.shimmer.store.datastore.DataStoreKeys.STORE_DETAIL
import com.shimmer.store.datastore.DataStoreUtil.readData
import com.shimmer.store.datastore.db.CartModel
import com.shimmer.store.networking.login
import com.shimmer.store.ui.enums.LoginType
import com.shimmer.store.ui.mainActivity.MainActivity
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.db
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.isBackStack
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.loginType
import com.shimmer.store.utils.getPatternFormat
import com.shimmer.store.utils.mainThread
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class Cart : Fragment() {
    private val viewModel: CartVM by viewModels()
    private var _binding: CartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CartBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged", "ClickableViewAccessibility", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isBackStack = true
        MainActivity.mainActivity.get()!!.callBack(2)
//        Log.e("TAG", "onViewCreated: ${isHide.value}")
        binding.apply {

            topBarBack.includeBackButton.apply {
                layoutBack.singleClick {
                    findNavController().navigateUp()
                }
            }

            topBarBack.ivCartLayout.visibility = View.GONE

//            topBar.apply {
//                textViewTitle.visibility = View.VISIBLE
//                ivSearch.visibility = View.GONE
//                ivCartLayout.visibility = View.GONE
//                textViewTitle.text = "Cart"
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


            when (loginType) {
                LoginType.VENDOR -> {
                    textCartOrder.text = resources.getString(R.string.place_order)
                }

                LoginType.CUSTOMER -> {
                    textCartOrder.text = resources.getString(R.string.proceed)
                }
            }
//            {
//                "cartItem": {
//                "sku": "SAS0005-G-18-YG-11",
//                "qty": 1,
//                "quote_id": "7"
//            }
//            }


            upperLayout.visibility = View.GONE
            filterLayout.visibility = View.GONE
//            viewModel.cartMutableList.value = false
            viewModel.cartMutableList.observe(viewLifecycleOwner) {
                Log.e("TAG", "loginType " + loginType)
                if (LoginType.CUSTOMER == loginType) {
                    rvList.adapter = viewModel.cartAdapterCustomer
                    mainThread {
                        val userList: List<CartModel>? = db?.cartDao()?.getAll()
                        rvList.setHasFixedSize(true)
                        viewModel.cartAdapterCustomer.notifyDataSetChanged()
                        viewModel.cartAdapterCustomer.submitList(userList)

                        var qunty = 0
                        var totalPrice: Double = 0.0
                        userList?.forEach {
                            totalPrice += (it.price!! * it.quantity)
                            qunty += it.quantity
                        }
                        textSubtotalPrice.text = "₹ " + getPatternFormat("1", totalPrice)
                        textTotalPrice.text = "₹ " + getPatternFormat("1", totalPrice)
                        textItems.text = "${qunty} Item"

                        if (!userList.isNullOrEmpty()) {
                            upperLayout.visibility = View.VISIBLE
                            filterLayout.visibility = View.VISIBLE
                        } else {
                            upperLayout.visibility = View.GONE
                            filterLayout.visibility = View.GONE
                        }

                        if(userList!!.size == 0){
                            binding.idDataNotFound.root.visibility = View.VISIBLE
                        }else{
                            binding.idDataNotFound.root.visibility = View.GONE
                        }
                    }

                } else if (LoginType.VENDOR == loginType) {
                    rvList.adapter = viewModel.cartAdapter
                    readData(CUSTOMER_TOKEN) { token ->
                        viewModel.getCart(token!!) {
                            val itemCart = this
                            Log.e("TAG", "getCart " + this.toString())
                            rvList.setHasFixedSize(true)
                            viewModel.cartAdapter.notifyDataSetChanged()
                            viewModel.cartAdapter.submitList(itemCart.items)

                            var qunty = 0
                            var totalPrice: Double = 0.0
                            itemCart.items.forEach {
                                totalPrice += (it.price * it.qty)
                                qunty += it.qty
                            }
                            textSubtotalPrice.text = "₹ " + getPatternFormat("1", totalPrice)
                            textTotalPrice.text = "₹ " + getPatternFormat("1", totalPrice)
                            textItems.text = "${qunty} Item"

                            if (!itemCart.items.isNullOrEmpty()) {
                                upperLayout.visibility = View.VISIBLE
                                filterLayout.visibility = View.VISIBLE
                            } else {
                                upperLayout.visibility = View.GONE
                                filterLayout.visibility = View.GONE
                            }


                            if(itemCart.items.size == 0){
                                binding.idDataNotFound.root.visibility = View.VISIBLE
                            }else{
                                binding.idDataNotFound.root.visibility = View.GONE
                            }
                        }
                    }
                }

            }

//            viewModel.cartMutableList.observe(viewLifecycleOwner) {
//                mainThread {
//                    val userList: List<CartModel>? = db?.cartDao()?.getAll()
//
//                    rvList.setHasFixedSize(true)
//                    rvList.adapter = viewModel.cartAdapter
//                    viewModel.cartAdapter.notifyDataSetChanged()
//                    viewModel.cartAdapter.submitList(userList)
//
//
//                    if (!userList.isNullOrEmpty()) {
//                        upperLayout.visibility = View.VISIBLE
//                        filterLayout.visibility = View.VISIBLE
//                    } else {
//                        upperLayout.visibility = View.GONE
//                        filterLayout.visibility = View.GONE
//                    }
//
//
//                    var price: Double = 0.0
//                    userList?.forEach {
//                        price += (it.price!! * it.quantity)
//                        Log.e(
//                            "TAG",
//                            "onViewCreated: " + it.name + " it.currentTime " + it.currentTime
//                        )
//                    }
//
//                    viewModel.subTotalPrice = price
//                    textSubtotalPrice.text = "₹${getPatternFormat("1", price)}"
//
//                    val discountPriceAfter =
//                        (viewModel.subTotalPrice * viewModel.discountPrice) / 100
//                    textDiscountPrice.text = "₹${getPatternFormat("1", discountPriceAfter)}"
//
//                    val priceANDdiscountPrice = price + discountPriceAfter
//
//                    val cstPriceAfter = (priceANDdiscountPrice * viewModel.cgstPrice) / 100
//                    textCGSTPrice.text = "₹${getPatternFormat("1", cstPriceAfter)}"
//
//                    val sgstPriceAfter = (priceANDdiscountPrice * viewModel.sgstPrice) / 100
//                    textSGSTPrice.text = "₹${getPatternFormat("1", sgstPriceAfter)}"
//
//                    val priceANDGSTPrice =
//                        priceANDdiscountPrice + (cstPriceAfter + sgstPriceAfter) + viewModel.shippingPrice
//                    textTotalPrice.text = "₹${getPatternFormat("1", priceANDGSTPrice)}"
//
//                    textShippingPrice.text = "₹${getPatternFormat("1", viewModel.shippingPrice)}"
//
//
//                    textDiscount.text = "Discount (${viewModel.discountPrice}%)"
//                    textCGST.text = "CGST (${viewModel.cgstPrice}%)"
//                    textSGST.text = "SGST (${viewModel.sgstPrice}%)"
//                }
//            }


            layoutProceedToPayment.singleClick {
                findNavController().navigate(R.id.action_cart_to_checkout)
            }


//            readData(CUSTOMER_TOKEN) { token ->
//                viewModel.getCart(token!!) {
//                    val itemCart = this
//                    Log.e("TAG", "getCart " + this.toString())
//                    rvList.setHasFixedSize(true)
//                    rvList.adapter = viewModel.cartAdapter
//                    viewModel.cartAdapter.notifyDataSetChanged()
//                    viewModel.cartAdapter.submitList(itemCart.items)
//
//
//                    if (!itemCart.items.isNullOrEmpty()) {
//                        upperLayout.visibility = View.VISIBLE
//                        filterLayout.visibility = View.VISIBLE
//                    } else {
//                        upperLayout.visibility = View.GONE
//                        filterLayout.visibility = View.GONE
//                    }
//
//
//                }
//            }

        }
    }


}
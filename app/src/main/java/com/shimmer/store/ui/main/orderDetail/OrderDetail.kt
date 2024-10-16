package com.shimmer.store.ui.main.orderDetail

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.shimmer.store.R
import com.shimmer.store.databinding.OrderDetailBinding
import com.shimmer.store.datastore.DataStoreKeys.ADMIN_TOKEN
import com.shimmer.store.datastore.DataStoreUtil.readData
import com.shimmer.store.di.AppModule_GsonFactory.gson
import com.shimmer.store.models.guestOrderList.ItemGuestOrderListItem
import com.shimmer.store.models.orderHistory.Item
import com.shimmer.store.ui.enums.LoginType
import com.shimmer.store.ui.mainActivity.MainActivity
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.loginType
import com.shimmer.store.utils.changeDateFormat
import com.shimmer.store.utils.getPatternFormat
import com.shimmer.store.utils.parcelable
import com.shimmer.store.utils.showSnackBar
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.internal.StringUtil.isNumeric


@AndroidEntryPoint
class OrderDetail : Fragment() {
    private var _binding: OrderDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OrderDetailVM by viewModels()

    companion object {
        var orderDetailLiveA = MutableLiveData<Boolean>(false)
        var orderDetailLiveB = MutableLiveData<Boolean>(false)
    }


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

            if (arguments?.getString("from") == "customerOrders") {
                val consentIntent = arguments?.parcelable<ItemGuestOrderListItem>("key")

                Log.e("TAG", "onViewCreated: ${consentIntent.toString()}")

                textName.text = consentIntent?.CustomerName
                textMobile.text = consentIntent?.customerMobile
                textEmail.text = consentIntent?.customerEmail

                textDate.text = consentIntent?.updatedtime?.changeDateFormat(
                    "yyyy-MM-dd HH:mm:ss",
                    "dd-MMM-yyyy"
                )
                textTime.text =
                    consentIntent?.updatedtime?.changeDateFormat("yyyy-MM-dd HH:mm:ss", "HH:mm")

                val typeToken = object : TypeToken<List<CartItem>>() {}.type
                val changeValue =
                    Gson().fromJson<List<CartItem>>(
                        Gson().fromJson(
                            consentIntent?.cartItem,
                            JsonElement::class.java
                        ), typeToken
                    )

                rvListCategory1.setHasFixedSize(true)
                viewModel.orderSKUCustomerOrders.notifyDataSetChanged()
                viewModel.orderSKUCustomerOrders.submitList(changeValue)
                rvListCategory1.adapter = viewModel.orderSKUCustomerOrders

                var price = 0.0
                changeValue.forEach {
                    price += it.price * it.qty
                }

                textSubTotalPrice.text = "₹ " + getPatternFormat("1", price)
                textTotalAmountPrice.text = "₹ " + getPatternFormat("1", price)


                if (consentIntent?.status == "pending") {
                    layoutSort.visibility = View.VISIBLE
                } else {
                    layoutSort.visibility = View.GONE
                }


                layoutSort.singleClick {
                    val jsonObjectStatus = JSONObject().apply {
                        put("id", consentIntent?.guestcustomeroder_id)
                        put("status", "complete")
                    }


                    viewModel.updateStatus(jsonObjectStatus) {
                        Log.e("TAG", "placeOrderGuest " + this)
                        if (this.toString().contains("updated")) {
                            layoutSort.visibility = View.GONE
                        } else {
                            showSnackBar("Something went wrong!")
                        }
                    }
                }
            } else if (arguments?.getString("from") == "orderHistory") {
//                val consentIntent = arguments?.parcelable<Item>("key")
                val _id = arguments?.getString("_id")
                Log.e("TAG", "onViewCreated: ${_id.toString()}")





                _id?.let {
                    readData(ADMIN_TOKEN) { token ->
                        viewModel.orderHistoryListDetail(token.toString(), _id) {
                            val itemOrderDetail = this

                            textDate.text = itemOrderDetail?.updated_at?.changeDateFormat(
                                "yyyy-MM-dd HH:mm:ss",
                                "dd-MMM-yyyy"
                            )
                            textTime.text =
                                itemOrderDetail?.updated_at?.changeDateFormat(
                                    "yyyy-MM-dd HH:mm:ss",
                                    "HH:mm"
                                )




                            rvListCategory1.setHasFixedSize(true)
                            viewModel.orderSKUOrderHistory.notifyDataSetChanged()
                            viewModel.orderSKUOrderHistory.submitList(itemOrderDetail?.items)
                            rvListCategory1.adapter = viewModel.orderSKUOrderHistory

                            layoutSort.visibility = View.GONE


                            textSubTotalPrice.text = "₹ " + getPatternFormat("1", itemOrderDetail?.base_subtotal)
//                            textGSTPrice.text = "₹" +itemOrderDetail?.base_shipping_incl_tax
                            textTotalAmountPrice.text = "₹ " + getPatternFormat("1", itemOrderDetail?.base_subtotal)

                        }
                    }





                    viewModel.orderHistoryDetail(_id!!){
                        val checkout_buyer_name = JSONObject(this).getString("checkout_buyer_name")
                        val checkout_buyer_email = JSONObject(this).getString("checkout_buyer_email")
                        val checkout_purchase_order_no = JSONObject(this).getString("checkout_purchase_order_no")

                        textName.text = checkout_buyer_name
                        textMobile.text = checkout_buyer_email
                        textEmail.text = checkout_purchase_order_no

                    }
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        Log.e("TAG", "onDestroyView:")
//        if (arguments?.getString("from") == "customerOrders") {
            orderDetailLiveA.value = true
//        } else if (arguments?.getString("from") == "orderHistory") {
            orderDetailLiveB.value = true
//        }
    }


}


data class CartItem(
    val name: String,
    val price: Double,
    val sku: String,
    val qty: Int,
    var isSelected: Boolean = false
)
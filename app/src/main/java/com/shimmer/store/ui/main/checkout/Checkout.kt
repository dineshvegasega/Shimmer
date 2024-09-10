package com.shimmer.store.ui.main.checkout

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
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.shimmer.store.R
import com.shimmer.store.databinding.CheckoutBinding
import com.shimmer.store.datastore.DataStoreKeys.CUSTOMER_TOKEN
import com.shimmer.store.datastore.DataStoreKeys.LOGIN_DATA
import com.shimmer.store.datastore.DataStoreKeys.QUOTE_ID
import com.shimmer.store.datastore.DataStoreUtil.readData
import com.shimmer.store.datastore.db.CartModel
import com.shimmer.store.models.user.ItemUserItem
import com.shimmer.store.ui.enums.LoginType
import com.shimmer.store.ui.mainActivity.MainActivity
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.db
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.isBackStack
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.loginType
import com.shimmer.store.utils.getPatternFormat
import com.shimmer.store.utils.mainThread
import com.shimmer.store.utils.showSnackBar
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONArray
import org.json.JSONObject

@AndroidEntryPoint
class Checkout : Fragment() {
    private val viewModel: CheckoutVM by viewModels()
    private var _binding: CheckoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CheckoutBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged", "ClickableViewAccessibility", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isBackStack = true
        MainActivity.mainActivity.get()!!.callBack(0)

        binding.apply {
            topBarBack.includeBackButton.apply {
                layoutBack.singleClick {
                    findNavController().navigateUp()
                }

                topBarBack.ivCartLayout.visibility = View.GONE
            }


            readData(LOGIN_DATA) { loginUser ->
                if (loginUser != null) {
                    val data = Gson().fromJson(
                        loginUser,
                        ItemUserItem::class.java
                    )
                    textFNTxt.text = "Name : " + data.contact_person
                    textCompanyNameTxt.text = "Franchise Name : " + data.name
                    textMobileTxt.text = "Mobile No : " + data.mobile_number
                    textAdrressTxt.text = "Address : " + data.register_address
                    textCityTxt.text = "City : " + data.register_city
                    textStateTxt.text = "State : " + data.register_state
                    textPinCodeTxt.text = "Pincode : " + data.register_pincode
                }
            }



            when (loginType) {
                LoginType.CUSTOMER -> {
                    groupVendor.visibility = View.GONE
                    groupGuest.visibility = View.VISIBLE
                    textPayment.text = resources.getString(R.string.select_franchise)
                }

                LoginType.VENDOR -> {
                    groupVendor.visibility = View.VISIBLE
                    groupGuest.visibility = View.VISIBLE
                    textPayment.text = resources.getString(R.string.proceed_to_payment)
                }
            }



            when (loginType) {
                LoginType.CUSTOMER -> {
                    mainThread {
                        val userList: List<CartModel>? = db?.cartDao()?.getAll()
                        binding.apply {
                            rvList.setHasFixedSize(true)
                            rvList.adapter = viewModel.ordersAdapter
                            viewModel.ordersAdapter.notifyDataSetChanged()
                            viewModel.ordersAdapter.submitList(userList)
                        }

                        var price: Double = 0.0
                        userList?.forEach {
                            price += (it.price!! * it.quantity)
                            Log.e(
                                "TAG",
                                "onViewCreated: " + it.name + " it.currentTime " + it.currentTime
                            )
                        }

                        viewModel.subTotalPrice = price
//                textSubtotalPrice.text = "₹${getPatternFormat("1", price)}"

                        val discountPriceAfter =
                            (viewModel.subTotalPrice * viewModel.discountPrice) / 100
                        textDiscountPrice.text = "₹${getPatternFormat("1", discountPriceAfter)}"

                        val priceANDdiscountPrice = price + discountPriceAfter

                        val cstPriceAfter = (priceANDdiscountPrice * viewModel.cgstPrice) / 100
//                textCGSTPrice.text = "₹${getPatternFormat("1", cstPriceAfter)}"

                        val sgstPriceAfter = (priceANDdiscountPrice * viewModel.sgstPrice) / 100
//                textSGSTPrice.text = "₹${getPatternFormat("1", sgstPriceAfter)}"

                        val priceANDGSTPrice =
                            priceANDdiscountPrice + (cstPriceAfter + sgstPriceAfter) + viewModel.shippingPrice
                        textSubtotalPrice.text = "₹${getPatternFormat("1", priceANDGSTPrice)}"
                        textTotalPrice.text = "₹${getPatternFormat("1", priceANDGSTPrice)}"

//                textShippingPrice.text = "₹${getPatternFormat("1", viewModel.shippingPrice)}"


                        textDiscount.text = "Discount (${viewModel.discountPrice}%)"
//                textCGST.text = "CGST (${viewModel.cgstPrice}%)"
//                textSGST.text = "SGST (${viewModel.sgstPrice}%)"
                    }
                }

                LoginType.VENDOR -> {
                    mainThread {
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
                                textSubtotalPrice.text = "₹" + totalPrice
                                textTotalPrice.text = "₹" + totalPrice
//                                textItems.text = "${qunty} Item"

                                if (!itemCart.items.isNullOrEmpty()) {
//                                    upperLayout.visibility = View.VISIBLE
                                    filterLayout.visibility = View.VISIBLE
                                } else {
//                                    upperLayout.visibility = View.GONE
                                    filterLayout.visibility = View.GONE
                                }


                                if (itemCart.items.size == 0) {
                                    binding.idDataNotFound.root.visibility = View.VISIBLE
                                } else {
                                    binding.idDataNotFound.root.visibility = View.GONE
                                }
                            }
                        }
                    }
                }
            }





            layoutSort.singleClick {
                when (loginType) {
                    LoginType.VENDOR -> {
                        if (editTextN.text.toString().isEmpty()) {
                            showSnackBar("Enter Full Name")
                        } else if (editEmail.text.toString().isEmpty()) {
                            showSnackBar("Enter Email")
                        } else if (editMobileNo.text.toString().isEmpty()) {
                            showSnackBar("Enter Mobile No")
                        } else {
//                            findNavController().navigate(
//                                R.id.action_checkout_to_payment,
//                                Bundle().apply {
//                                    putString("name", editTextN.text.toString())
//                                    putString("email", editEmail.text.toString())
//                                    putString("mobile", editMobileNo.text.toString())
//                                })


                            readData(LOGIN_DATA) { loginUser ->
                                if (loginUser != null) {
                                    val data = Gson().fromJson(
                                        loginUser,
                                        ItemUserItem::class.java
                                    )

                                    val shipping_address = JSONObject().apply {
                                        put("region", data.d_state)
                                        put("region_id", data.d_resignid)
                                        put("region_code", data.d_resigncode)
                                        put("country_id", "IN")
                                        put("street", JSONArray().put(data.d_address))
                                        put("postcode", data.d_pincode)
                                        put("city", data.d_city)
                                        put("firstname", data.contact_person)
                                        put("lastname", data.contact_person)
                                        put("email", "")
                                        put("telephone", data.mobile_number)
                                    }

                                    val billing_address = JSONObject().apply {
                                        put("region", data.register_state)
                                        put("region_id", data.register_resignid)
                                        put("region_code", data.register_resigncode)
                                        put("country_id", "IN")
                                        put("street", JSONArray().put(data.register_address))
                                        put("postcode", data.register_pincode)
                                        put("city", data.register_city)
                                        put("firstname", data.contact_person)
                                        put("lastname", data.contact_person)
                                        put("email", "")
                                        put("telephone", data.mobile_number)
                                    }

                                    val addressInformation = JSONObject().apply {
                                        put("addressInformation", JSONObject().apply {
                                            put("shipping_address", shipping_address)
                                            put("billing_address", billing_address)
                                            put("shipping_carrier_code", "flatrate")
                                            put("shipping_method_code", "flatrate")
                                        })
                                    }

                                    Log.e(
                                        "TAG",
                                        "jsonObjectaddressInformation " + addressInformation
                                    )

                                    readData(CUSTOMER_TOKEN) { token ->
                                        viewModel.updateShipping(token!!, addressInformation) {
                                            Log.e("TAG", "onCallBack22: ${this.toString()}")
//                                            findNavController().navigate(
//                                                R.id.action_checkout_to_payment,
//                                                Bundle().apply {
//                                                    putString("name", editTextN.text.toString())
//                                                    putString("email", editEmail.text.toString())
//                                                    putString(
//                                                        "mobile",
//                                                        editMobileNo.text.toString()
//                                                    )
//                                                })


                                            readData(QUOTE_ID) { quoteId->
                                                val customerData = JSONObject().apply {
                                                    put("cartId", quoteId)
                                                    put("customFields", JSONObject().apply {
                                                        put("checkout_buyer_name", editTextN.text.toString())
                                                        put("checkout_buyer_email", editEmail.text.toString())
                                                        put("checkout_purchase_order_no", editMobileNo.text.toString())
                                                        put("checkout_goods_mark", "")
                                                    })
                                                }

                                                viewModel.postCustomDetails(token!!, customerData) {
                                                    Log.e("TAG", "postCustomDetailsonCallBack22: ${this.toString()}")
                                                    findNavController().navigate(R.id.action_checkout_to_payment)
                                                }

                                            }
                                        }
                                    }
                                }
                            }

                        }

                    }

                    LoginType.CUSTOMER -> {
                        if (editTextN.text.toString().isEmpty()) {
                            showSnackBar("Enter Full Name")
                        } else if (editEmail.text.toString().isEmpty()) {
                            showSnackBar("Enter Email")
                        } else if (editMobileNo.text.toString().isEmpty()) {
                            showSnackBar("Enter Mobile No")
                        } else {

                            mainThread {
                                val userList: List<CartModel>? = db?.cartDao()?.getAll()

                                if (userList?.size!! > 0) {
                                    findNavController().navigate(
                                        R.id.action_checkout_to_selectFranchise,
                                        Bundle().apply {
                                            putString("name", editTextN.text.toString())
                                            putString("email", editEmail.text.toString())
                                            putString("mobile", editMobileNo.text.toString())
                                        })
                                }

//                                val jsonArrayCartItem = JSONArray()
//
//                                userList?.forEach {
//                                    jsonArrayCartItem.apply {
//                                        put(JSONObject().apply {
//                                            put("name", it.name)
//                                            put("price", it.price)
//                                            put("sku", it.sku)
//                                            put("qty", it.quantity)
//                                        })
//                                    }
//                                }
//
//                                val jsonObject = JSONObject().apply {
//                                    put("customerName", editTextN.text.toString())
//                                    put("customerEmail", editEmail.text.toString())
//                                    put("customerMobile", editMobileNo.text.toString())
//                                    put("franchiseCode", "")
//                                    put("status", "pending")
//                                    put("cartItem", jsonArrayCartItem)
//                                }
//
//
//                                val jsonObjectGuestcart = JSONObject().apply {
//                                    put("guestcart", jsonObject)
//                                }
//                                Log.e("TAG", "jsonObjectGuestcart " + jsonObjectGuestcart)
//
//                                val jsonObjectXX = Gson().fromJson(
//                                    jsonObjectGuestcart.toString(),
//                                    JsonElement::class.java
//                                )
//
//                                var dd = jsonObjectXX.getAsJsonObject().get("guestcart")
//
//                                var vv = dd.getAsJsonObject().get("customerMobile")
//                                vv.apply {
//                                    "asfsdfdf"
//                                }
//
//
//                                Log.e("TAG", "jsonObjectXX " + jsonObjectXX)
////                            findNavController().navigate(
////                                R.id.action_checkout_to_selectFranchise,
////                                Bundle().apply {
//////                                    putString("name", editTextN.text.toString())
//////                                    putString("email", editEmail.text.toString())
//////                                    putString("mobile", editMobileNo.text.toString())
////                                })
//                            }
                            }
                        }
//
//                            }
//                        }


                    }
                }
            }
        }
    }
}
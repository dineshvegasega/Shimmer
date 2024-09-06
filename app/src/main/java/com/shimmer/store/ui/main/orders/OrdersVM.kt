package com.shimmer.store.ui.main.orders

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.shimmer.store.R
import com.shimmer.store.databinding.ItemCustomerOrderBinding
import com.shimmer.store.databinding.ItemOrderHistoryBinding
import com.shimmer.store.databinding.LoaderBinding
import com.shimmer.store.datastore.db.CartModel
import com.shimmer.store.genericAdapter.GenericAdapter
import com.shimmer.store.models.Items
import com.shimmer.store.models.guestOrderList.ItemGuestOrderList
import com.shimmer.store.models.guestOrderList.ItemGuestOrderListItem
import com.shimmer.store.models.orderHistory.Item
import com.shimmer.store.models.orderHistory.ItemOrderHistoryModel
import com.shimmer.store.models.products.ItemProductRoot
import com.shimmer.store.networking.ApiInterface
import com.shimmer.store.networking.CallHandler
import com.shimmer.store.networking.Repository
import com.shimmer.store.ui.mainActivity.MainActivity
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.db
import com.shimmer.store.utils.changeDateFormat
import com.shimmer.store.utils.mainThread
import com.shimmer.store.utils.sessionExpired
import com.shimmer.store.utils.showSnackBar
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class OrdersVM @Inject constructor(private val repository: Repository) : ViewModel() {

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

    fun getCartCount(callBack: Int.() -> Unit) {
        viewModelScope.launch {
            val userList: List<CartModel>? = db?.cartDao()?.getAll()
            var countBadge = 0
            userList?.forEach {
                countBadge += it.quantity
            }
            callBack(countBadge)
        }
    }


    val orderHistory = object : GenericAdapter<ItemOrderHistoryBinding, Item>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemOrderHistoryBinding.inflate(inflater, parent, false)

        override fun onBindHolder(
            binding: ItemOrderHistoryBinding,
            dataClass: Item,
            position: Int
        ) {
            binding.apply {
                val date =
                    dataClass.updated_at.changeDateFormat("yyyy-MM-dd HH:mm:ss", "dd-MMM-yyyy")
                btDate.text = date
                textOrderPrice.text = "₹" + dataClass.base_grand_total


                when (dataClass.status) {
                    "pending" -> {
                        btStatus.text = "Pending"
                        btStatus.backgroundTintList =
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    binding.root.context,
                                    R.color._E87103
                                )
                            )
                        btStatus.visibility = View.VISIBLE
                    }
                    "inprogress" -> {
                        btStatus.text = "In Progress"
                        btStatus.backgroundTintList =
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    binding.root.context,
                                    R.color._F7879A
                                )
                            )
                        btStatus.visibility = View.VISIBLE
                    }
                    "complete" -> {
                        btStatus.text = "Complete"
                        btStatus.backgroundTintList =
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    binding.root.context,
                                    R.color._2eb82e
                                )
                            )
                        btStatus.visibility = View.VISIBLE
                    }
                    else -> {
                        btStatus.visibility = View.GONE
                    }
                }

                root.singleClick {
                    root.findNavController().navigate(R.id.action_orders_to_orderDetail, Bundle().apply {
                        putString("key" , dataClass.toString())
                    })
                }
            }
        }
    }


    val customerOrders =
        object : GenericAdapter<ItemCustomerOrderBinding, ItemGuestOrderListItem>() {
            override fun onCreateView(
                inflater: LayoutInflater,
                parent: ViewGroup,
                viewType: Int
            ) = ItemCustomerOrderBinding.inflate(inflater, parent, false)

            override fun onBindHolder(
                binding: ItemCustomerOrderBinding,
                dataClass: ItemGuestOrderListItem,
                position: Int
            ) {
                binding.apply {

                    val date =
                        dataClass.updatedtime.changeDateFormat("yyyy-MM-dd HH:mm:ss", "dd-MMM-yyyy")
                    btDate.text = date

                    textName.text = dataClass.CustomerName
                    textMobile.text = dataClass.customerEmail
                    textMobile.text = dataClass.customerMobile
                    when (dataClass.status) {
                        "pending" -> {
                            btStatus.text = "Pending"
                            btStatus.backgroundTintList =
                                ColorStateList.valueOf(
                                        ContextCompat.getColor(
                                            binding.root.context,
                                            R.color._E87103
                                        )
                                    )
                            btStatus.visibility = View.VISIBLE
                        }
                        "complete" -> {
                            btStatus.text = "Complete"
                            btStatus.backgroundTintList =
                                ColorStateList.valueOf(
                                    ContextCompat.getColor(
                                        binding.root.context,
                                        R.color._2eb82e
                                    )
                                )
                            btStatus.visibility = View.VISIBLE
                        }
                        else -> {
                            btStatus.visibility = View.GONE
                        }
                    }

                    root.singleClick {
                        root.findNavController().navigate(R.id.action_orders_to_orderDetail, Bundle().apply {
                            putString("key" , ""+Gson().toJson(dataClass.toString()))
                        })
                    }
                }
            }
        }


    fun getOrderHistory(
        adminToken: String,
        emptyMap: MutableMap<String, String>,
        callBack: ItemOrderHistoryModel.() -> Unit
    ) =
        viewModelScope.launch {
            repository.callApi(
                callHandler = object : CallHandler<Response<ItemOrderHistoryModel>> {
                    override suspend fun sendRequest(apiInterface: ApiInterface) =
//                        if (loginType == "vendor") {
//                            apiInterface.products("Bearer " +adminToken, storeWebUrl, emptyMap)
//                        } else if (loginType == "guest") {
                        apiInterface.getOrderHistory("Bearer " + adminToken, emptyMap)

                    //                        } else {
//                            apiInterface.products("Bearer " +adminToken, storeWebUrl, emptyMap)
//                        }
                    @SuppressLint("SuspiciousIndentation")
                    override fun success(response: Response<ItemOrderHistoryModel>) {
                        if (response.isSuccessful) {
                            try {
                                Log.e("TAG", "successAA: ${response.body().toString()}")
//                                val mMineUserEntity = Gson().fromJson(response.body(), ItemProductRoot::class.java)
                                callBack(response.body()!!)

                            } catch (e: Exception) {
                            }
                        }
                    }

                    override fun error(message: String) {
                        if (message.contains("authorized")) {
                            sessionExpired()
                        } else {
                            showSnackBar("Something went wrong!")
                        }
                    }

                    override fun loading() {
                        super.loading()
                    }
                }
            )
        }


    fun guestOrderList(franchiseId: String, callBack: ItemGuestOrderList.() -> Unit) =
        viewModelScope.launch {
            repository.callApi(
                callHandler = object : CallHandler<Response<ItemGuestOrderList>> {
                    override suspend fun sendRequest(apiInterface: ApiInterface) =
//                        if (loginType == "vendor") {
//                            apiInterface.products("Bearer " +adminToken, storeWebUrl, emptyMap)
//                        } else if (loginType == "guest") {
                        apiInterface.guestOrderList(franchiseId)

                    //                        } else {
//                            apiInterface.products("Bearer " +adminToken, storeWebUrl, emptyMap)
//                        }
                    @SuppressLint("SuspiciousIndentation")
                    override fun success(response: Response<ItemGuestOrderList>) {
                        if (response.isSuccessful) {
                            try {
                                Log.e("TAG", "successAA: ${response.body().toString()}")
//                                val mMineUserEntity = Gson().fromJson(response.body(), ItemProductRoot::class.java)
                                callBack(response.body()!!)

                            } catch (e: Exception) {
                            }
                        }
                    }

                    override fun error(message: String) {
                        if (message.contains("authorized")) {
                            sessionExpired()
                        } else {
                            showSnackBar("Something went wrong!")
                        }
                    }

                    override fun loading() {
                        super.loading()
                    }
                }
            )
        }


}
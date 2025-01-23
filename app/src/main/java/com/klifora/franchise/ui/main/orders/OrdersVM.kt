package com.klifora.franchise.ui.main.orders

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.klifora.franchise.R
import com.klifora.franchise.databinding.ItemCustomerOrderBinding
import com.klifora.franchise.databinding.ItemOrderHistoryBinding
import com.klifora.franchise.databinding.LoaderBinding
import com.klifora.franchise.datastore.db.CartModel
import com.klifora.franchise.genericAdapter.GenericAdapter
import com.klifora.franchise.models.Items
import com.klifora.franchise.models.guestOrderList.ItemGuestOrderList
import com.klifora.franchise.models.guestOrderList.ItemGuestOrderListItem
import com.klifora.franchise.models.myOrdersList.ItemOrders
import com.klifora.franchise.models.myOrdersList.ItemOrdersItem
import com.klifora.franchise.networking.ApiInterface
import com.klifora.franchise.networking.CallHandler
import com.klifora.franchise.networking.GST_PERCENT
import com.klifora.franchise.networking.Repository
import com.klifora.franchise.ui.mainActivity.MainActivity
import com.klifora.franchise.ui.mainActivity.MainActivity.Companion.db
import com.klifora.franchise.utils.changeDateFormat
import com.klifora.franchise.utils.getPatternFormat
import com.klifora.franchise.utils.sessionExpired
import com.klifora.franchise.utils.showSnackBar
import com.klifora.franchise.utils.singleClick
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class OrdersVM @Inject constructor(private val repository: Repository) : ViewModel() {

    val itemsCustomerOrders: ArrayList<ItemGuestOrderListItem> = ArrayList()

    val itemsOrderHistory: ArrayList<ItemOrdersItem> = ArrayList()



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
        alert.setView(binding.root.rootView)
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


    val orderHistory = object : GenericAdapter<ItemOrderHistoryBinding, ItemOrdersItem>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemOrderHistoryBinding.inflate(inflater, parent, false)

        override fun onBindHolder(
            binding: ItemOrderHistoryBinding,
            dataClass: ItemOrdersItem,
            position: Int
        ) {
            binding.apply {
                val date =
                    dataClass.updated_at.changeDateFormat("yyyy-MM-dd HH:mm:ss", "dd-MMM-yyyy")
                var gstValue: Double = 0.0
                gstValue = (dataClass.base_subtotal.toDouble() * GST_PERCENT) / 100
                var gstTotalPrice: Double = 0.0
                gstTotalPrice = dataClass.base_subtotal.toDouble() + gstValue
                btDate.text = date
                textOrderPrice.text = "₹ " + getPatternFormat("1",gstTotalPrice)

                textOrderNo.text =  dataClass.increment_id

                when (dataClass.state) {
                    "new" -> {
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
                            "Accepted" -> {
                                btStatus.text = "Accepted"
                                btStatus.backgroundTintList =
                                    ColorStateList.valueOf(
                                        ContextCompat.getColor(
                                            binding.root.context,
                                            R.color._138808
                                        )
                                    )
                                btStatus.visibility = View.VISIBLE
                            }
                        }
                    }

                    "processing" -> {
                        when (dataClass.status) {
                            "Accepted" -> {
                                btStatus.text = "Accepted"
                                btStatus.backgroundTintList =
                                    ColorStateList.valueOf(
                                        ContextCompat.getColor(
                                            binding.root.context,
                                            R.color._138808
                                        )
                                    )
                                btStatus.visibility = View.VISIBLE
                            }
                            "processing" -> {
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
                            "approved" -> {
                                btStatus.text = "Approved"
                                btStatus.backgroundTintList =
                                    ColorStateList.valueOf(
                                        ContextCompat.getColor(
                                            binding.root.context,
                                            R.color._0098BD
                                        )
                                    )
                                btStatus.visibility = View.VISIBLE
                            }
                            "Photography" -> {
                                btStatus.text = "Photography"
                                btStatus.backgroundTintList =
                                    ColorStateList.valueOf(
                                        ContextCompat.getColor(
                                            binding.root.context,
                                            R.color._E79D46
                                        )
                                    )
                                btStatus.visibility = View.VISIBLE
                            }
                            "Certification" -> {
                                btStatus.text = "Certification"
                                btStatus.backgroundTintList =
                                    ColorStateList.valueOf(
                                        ContextCompat.getColor(
                                            binding.root.context,
                                            R.color._FF5555
                                        )
                                    )
                                btStatus.visibility = View.VISIBLE
                            }
                            "Packaging" -> {
                                btStatus.text = "Packaging"
                                btStatus.backgroundTintList =
                                    ColorStateList.valueOf(
                                        ContextCompat.getColor(
                                            binding.root.context,
                                            R.color._494949
                                        )
                                    )
                                btStatus.visibility = View.VISIBLE
                            }
                            "Dispatch" -> {
                                btStatus.text = "Dispatch"
                                btStatus.backgroundTintList =
                                    ColorStateList.valueOf(
                                        ContextCompat.getColor(
                                            binding.root.context,
                                            R.color._0098BD
                                        )
                                    )
                                btStatus.visibility = View.VISIBLE
                            }
                            "complete" -> {
                                btStatus.text = "Delivered"
                                btStatus.backgroundTintList =
                                    ColorStateList.valueOf(
                                        ContextCompat.getColor(
                                            binding.root.context,
                                            R.color._138808
                                        )
                                    )
                                btStatus.visibility = View.VISIBLE
                            }
                            "Faild" -> {
                                btStatus.text = "Failed"
                                btStatus.backgroundTintList =
                                    ColorStateList.valueOf(
                                        ContextCompat.getColor(
                                            binding.root.context,
                                            R.color._F11608
                                        )
                                    )
                                btStatus.visibility = View.VISIBLE
                            }
                            "fraud" -> {
                                btStatus.text = "Fraud"
                                btStatus.backgroundTintList =
                                    ColorStateList.valueOf(
                                        ContextCompat.getColor(
                                            binding.root.context,
                                            R.color._9F0625
                                        )
                                    )
                                btStatus.visibility = View.VISIBLE
                            }
                            else -> {
                                btStatus.visibility = View.GONE
                            }
                        }
                    }

                    "complete" -> {
                        when (dataClass.status) {
                            "complete" -> {
                                btStatus.text = "Delivered"
                                btStatus.backgroundTintList =
                                    ColorStateList.valueOf(
                                        ContextCompat.getColor(
                                            binding.root.context,
                                            R.color._138808
                                        )
                                    )
                                btStatus.visibility = View.VISIBLE
                            }
                        }
                    }
                }


                root.singleClick {
                    root.findNavController().navigate(R.id.action_orders_to_orderDetail, Bundle().apply {
                        putString("from" , "orderHistory")
                        putString("_id" , ""+dataClass.entity_id)
//                        putString("name" , ""+dataClass.checkout_buyer_name)
//                        putString("mobile" , ""+dataClass.checkout_purchase_order_no)
//                        putString("email" , ""+dataClass.checkout_buyer_email)
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
                        "dispatched" -> {
                            btStatus.text = "Dispatched"
                            btStatus.backgroundTintList =
                                ColorStateList.valueOf(
                                    ContextCompat.getColor(
                                        binding.root.context,
                                        R.color._2eb82e
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
                                        R.color._2A3740
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
                            putString("from" , "customerOrders")
                            putParcelable("key" , dataClass)
                        })
                    }
                }
            }
        }


//    fun getOrderHistory(
//        adminToken: String,
//        emptyMap: MutableMap<String, String>,
//        callBack: ItemOrderHistoryModel.() -> Unit
//    ) =
//        viewModelScope.launch {
//            repository.callApi(
//                callHandler = object : CallHandler<Response<ItemOrderHistoryModel>> {
//                    override suspend fun sendRequest(apiInterface: ApiInterface) =
////                        if (loginType == "vendor") {
////                            apiInterface.products("Bearer " +adminToken, storeWebUrl, emptyMap)
////                        } else if (loginType == "guest") {
//                        apiInterface.getOrderHistory("Bearer " + adminToken, emptyMap)
//
//                    //                        } else {
////                            apiInterface.products("Bearer " +adminToken, storeWebUrl, emptyMap)
////                        }
//                    @SuppressLint("SuspiciousIndentation")
//                    override fun success(response: Response<ItemOrderHistoryModel>) {
//                        if (response.isSuccessful) {
//                            try {
//                                Log.e("TAG", "successAA: ${response.body().toString()}")
////                                val mMineUserEntity = Gson().fromJson(response.body(), ItemProductRoot::class.java)
//                                callBack(response.body()!!)
//
//                            } catch (e: Exception) {
//                            }
//                        }
//                    }
//
//                    override fun error(message: String) {
//                        if (message.contains("authorized")) {
//                            sessionExpired()
//                        } else {
//                            showSnackBar("Something went wrong!")
//                        }
//                    }
//
//                    override fun loading() {
//                        super.loading()
//                    }
//                }
//            )
//        }



    private var itemCustomerOrdersResult = MutableLiveData<ItemGuestOrderList>()
    val itemLiveCustomerOrders : LiveData<ItemGuestOrderList> get() = itemCustomerOrdersResult
    fun guestOrderList(franchiseId: String, mobile : String, name : String, pageNumber: Int) =
        viewModelScope.launch {
            if(pageNumber == 0 || pageNumber == 1){
                repository.callApi(
                    callHandler = object : CallHandler<Response<ItemGuestOrderList>> {
                        override suspend fun sendRequest(apiInterface: ApiInterface) =
                            apiInterface.guestOrderList(franchiseId, mobile, name, "", pageNumber)
                        @SuppressLint("SuspiciousIndentation")
                        override fun success(response: Response<ItemGuestOrderList>) {
                            if (response.isSuccessful) {
                                itemCustomerOrdersResult.value = response.body()!!
//                            try {
//                                Log.e("TAG", "successAA: ${response.body().toString()}")
////                                val mMineUserEntity = Gson().fromJson(response.body(), ItemProductRoot::class.java)
////                                callBack(response.body()!!.toString().toString().replace("\\", ""))
//                                callBack(response.body()!!)
//
//                            } catch (e: Exception) {
//                            }
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
            } else {
                repository.callApiWithoutLoader(
                    callHandler = object : CallHandler<Response<ItemGuestOrderList>> {
                        override suspend fun sendRequest(apiInterface: ApiInterface) =
                            apiInterface.guestOrderList(franchiseId, mobile, name, "", pageNumber)
                        @SuppressLint("SuspiciousIndentation")
                        override fun success(response: Response<ItemGuestOrderList>) {
                            if (response.isSuccessful) {
                                itemCustomerOrdersResult.value = response.body()!!
//                            try {
//                                Log.e("TAG", "successAA: ${response.body().toString()}")
////                                val mMineUserEntity = Gson().fromJson(response.body(), ItemProductRoot::class.java)
////                                callBack(response.body()!!.toString().toString().replace("\\", ""))
//                                callBack(response.body()!!)
//
//                            } catch (e: Exception) {
//                            }
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



    private var itemOrderHistoryResult = MutableLiveData<ItemOrders>()
    val itemLiveOrderHistory : LiveData<ItemOrders> get() = itemOrderHistoryResult
    fun orderHistoryList(franchiseId: String, mobile : String, name : String, pageNumber : Int) =
        viewModelScope.launch {
            if(pageNumber == 0 || pageNumber == 1){
                repository.callApi(
                    callHandler = object : CallHandler<Response<ItemOrders>> {
                        override suspend fun sendRequest(apiInterface: ApiInterface) =
                            apiInterface.orderHistoryList(franchiseId, mobile, name, pageNumber, 10)
                        @SuppressLint("SuspiciousIndentation")
                        override fun success(response: Response<ItemOrders>) {
                            if (response.isSuccessful) {
                                itemOrderHistoryResult.value = response.body()!!
//                            try {
//                                Log.e("TAG", "successAA: ${response.body().toString()}")
//                                callBack(response.body()!!)
//                            } catch (e: Exception) {
//                            }
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
            } else {
                repository.callApiWithoutLoader(
                    callHandler = object : CallHandler<Response<ItemOrders>> {
                        override suspend fun sendRequest(apiInterface: ApiInterface) =
                            apiInterface.orderHistoryList(franchiseId, mobile, name, pageNumber, 10)
                        @SuppressLint("SuspiciousIndentation")
                        override fun success(response: Response<ItemOrders>) {
                            if (response.isSuccessful) {
                                itemOrderHistoryResult.value = response.body()!!
//                            try {
//                                Log.e("TAG", "successAA: ${response.body().toString()}")
//                                callBack(response.body()!!)
//                            } catch (e: Exception) {
//                            }
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


}
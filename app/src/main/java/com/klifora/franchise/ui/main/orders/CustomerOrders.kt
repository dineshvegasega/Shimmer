package com.klifora.franchise.ui.main.orders

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.klifora.franchise.databinding.CustomerOrdersBinding
import com.klifora.franchise.datastore.DataStoreKeys.LOGIN_DATA
import com.klifora.franchise.datastore.DataStoreUtil.readData
import com.klifora.franchise.models.Items
import com.klifora.franchise.models.user.ItemUserItem
import com.klifora.franchise.utils.isLastItemDisplaying
import dagger.hilt.android.AndroidEntryPoint
import org.jsoup.internal.StringUtil.isNumeric


@AndroidEntryPoint
class CustomerOrders(
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

    var page: Int = 1

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

//            readData(ADMIN_TOKEN) { token ->
//                val emptyMap = mutableMapOf<String, String>()
//                emptyMap["searchCriteria[filter_groups][0][filters][" + 0 + "][field]"] = "store_id"
//                emptyMap["searchCriteria[filter_groups][0][filters][" + 0 + "][value]"] = "4"
//
//            }


//            loadData()

//            orderDetailLiveA.value = true
//            orderDetailLiveA.observe(viewLifecycleOwner) {
//                Log.e("TAG", "orderDetailLiveAA: $it")
                loadData("" , "")
//            }


            editTextSearch.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val isNumeric = isNumeric(editTextSearch.text.toString())
                    viewModel.itemsCustomerOrders.clear()
                    page = 1
                    if(isNumeric == true){
                        loadData(""+editTextSearch.text.toString() , "")
                    } else {
                        loadData("" , ""+editTextSearch.text.toString())
                    }
                }
                true
            }

//
//            editTextSearch.addTextChangedListener(object : TextWatcher {
//                    override fun afterTextChanged(s: Editable?) {
//                    }
//
//                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                    }
//
//                    @SuppressLint("SuspiciousIndentation")
//                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                        val isNumeric = isNumeric(editTextSearch.text.toString())
//                        if(isNumeric == true){
//                            loadData(""+editTextSearch.text.toString() , "")
//                        } else {
//                            loadData("" , ""+editTextSearch.text.toString())
//                        }
//                    }
//                })

            rvListCategory1.setHasFixedSize(true)
            rvListCategory1.adapter = viewModel.customerOrders


            var isLoad = true
            viewModel.itemLiveCustomerOrders.observe(viewLifecycleOwner) {
                if (it.size != 0) {
                    viewModel.itemsCustomerOrders.addAll(it)
                    isLoad = true
                } else {
                    isLoad = false
                }

                idPBLoading.visibility = View.GONE

                Log.e("TAG", "onViewCreatedXXX: "+viewModel.itemsCustomerOrders.size)

                viewModel.customerOrders.submitList(viewModel.itemsCustomerOrders)
                viewModel.customerOrders.notifyDataSetChanged()

//                viewModel.customerOrders.notifyItemRangeChanged(0, viewModel.customerOrders.getItemCount())
                if (viewModel.itemsCustomerOrders.size == 0) {
                    binding.idDataNotFound.root.visibility = View.VISIBLE
                } else {
                    binding.idDataNotFound.root.visibility = View.GONE
                }
            }


            rvListCategory1.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (rvListCategory1.isLastItemDisplaying()) {
                        if (isLoad){
                            Log.e("TAG", "addOnScrollListener "+dx +"   "+dy)
                            page++
                            idPBLoading.visibility = View.VISIBLE
                            val isNumeric = isNumeric(editTextSearch.text.toString())
                            if(isNumeric == true){
                                loadData(""+editTextSearch.text.toString() , "")
                            } else {
                                loadData("" , ""+editTextSearch.text.toString())
                            }
                        }
                    }
                }
            })

        }
    }



    @SuppressLint("NotifyDataSetChanged")
    fun loadData(mobile : String, name : String) {
        binding.apply {
            readData(LOGIN_DATA) { loginUser ->
                if (loginUser != null) {
                    val data = Gson().fromJson(
                        loginUser,
                        ItemUserItem::class.java
                    )

                    viewModel.guestOrderList(data.name, mobile, name, page)
//                    viewModel.guestOrderList(data.name, mobile, name ) {
//                        Log.e("TAG", "this.items " + this.toString())
////                        val element: ItemGuestOrderListItem = Gson().fromJson(this.toString(), ItemGuestOrderListItem::class.java)
////                        val typeToken = object : TypeToken<List<ItemGuestOrderListItem>>() {}.type
////                        val changeValue =
////                            Gson().fromJson<List<ItemGuestOrderListItem>>(Gson().toJson(this.toString()), typeToken)
//
//
//
//                        viewModel.customerOrders.notifyDataSetChanged()
//                        viewModel.customerOrders.submitList(this)
//
//                        viewModel.customerOrders.notifyItemRangeChanged(0, viewModel.customerOrders.getItemCount());
//                    }
                }
            }
        }
    }



}
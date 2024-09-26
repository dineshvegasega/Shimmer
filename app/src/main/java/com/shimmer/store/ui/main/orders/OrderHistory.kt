package com.shimmer.store.ui.main.orders

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.shimmer.store.databinding.CategoryChildTabBinding
import com.shimmer.store.databinding.OrderHistoryBinding
import com.shimmer.store.datastore.DataStoreKeys.ADMIN_TOKEN
import com.shimmer.store.datastore.DataStoreKeys.LOGIN_DATA
import com.shimmer.store.datastore.DataStoreUtil.readData
import com.shimmer.store.models.Items
import com.shimmer.store.models.user.ItemUserItem
import com.shimmer.store.ui.main.orderDetail.OrderDetail.Companion.orderDetailLiveB
import com.shimmer.store.utils.isLastItemDisplaying
import dagger.hilt.android.AndroidEntryPoint
import org.jsoup.internal.StringUtil.isNumeric

@AndroidEntryPoint
class OrderHistory (
    private val activity: FragmentActivity,
    private val videoPath: Items,
    position: Int
) : Fragment() {
    private var _binding: OrderHistoryBinding? = null
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
        _binding = OrderHistoryBinding.inflate(inflater)
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

//            orderDetailLiveB.value = true
//            orderDetailLiveB.observe(viewLifecycleOwner){
//                Log.e("TAG", "orderDetailLiveBB: $it")
                loadData("" ,"")
//            }



            editTextSearch.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val isNumeric = isNumeric(editTextSearch.text.toString())
                    viewModel.itemsOrderHistory.clear()
                    page = 1
                    if(isNumeric == true){
                        loadData(""+editTextSearch.text.toString() , "")
                    } else {
                        loadData("" , ""+editTextSearch.text.toString())
                    }
                }
                true
            }



            rvListCategory1.setHasFixedSize(true)
            rvListCategory1.adapter = viewModel.orderHistory


            var isLoad = true
            viewModel.itemLiveOrderHistory.observe(viewLifecycleOwner) {
                if (it.size != 0) {
                    viewModel.itemsOrderHistory.addAll(it)
                    isLoad = true
                } else {
                    isLoad = false
                }

                idPBLoading.visibility = View.GONE

                Log.e("TAG", "onViewCreatedXXX: "+viewModel.itemsOrderHistory.size)

                viewModel.orderHistory.submitList(viewModel.itemsOrderHistory)
                viewModel.orderHistory.notifyDataSetChanged()

//                viewModel.customerOrders.notifyItemRangeChanged(0, viewModel.customerOrders.getItemCount())
                if (viewModel.itemsOrderHistory.size == 0) {
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



//        viewModel.itemLiveOrderHistory?.observe(viewLifecycleOwner) {
//            viewModel.orderHistory.notifyDataSetChanged()
//            viewModel.orderHistory.submitList(it)
//            viewModel.orderHistory.notifyItemRangeChanged(0, viewModel.orderHistory.getItemCount());
//            if (it.size == 0) {
//                binding.idDataNotFound.root.visibility = View.VISIBLE
//            } else {
//                binding.idDataNotFound.root.visibility = View.GONE
//            }
//        }

    }



    @SuppressLint("NotifyDataSetChanged")
    fun loadData(mobile : String, name : String) {
        binding.apply {
//            readData(ADMIN_TOKEN) { token ->
//                val emptyMap = mutableMapOf<String, String>()
//                emptyMap["searchCriteria[filter_groups][0][filters][" + 0 + "][field]"] = "store_id"
//                emptyMap["searchCriteria[filter_groups][0][filters][" + 0 + "][value]"] = "4"
//                viewModel.getOrderHistory(token.toString(), emptyMap){
////                    Log.e("TAG", "this.items "+this.items.size)
//                    rvListCategory1.setHasFixedSize(true)
//                    rvListCategory1.adapter = viewModel.orderHistory
//                    viewModel.orderHistory.notifyDataSetChanged()
//                    viewModel.orderHistory.submitList(this.items)
//                }
//            }


            readData(LOGIN_DATA) { loginUser ->
                if (loginUser != null) {
                    val data = Gson().fromJson(
                        loginUser,
                        ItemUserItem::class.java
                    )

                    viewModel.orderHistoryList(data.name, mobile, name, page)

//                    viewModel.orderHistoryList(data.name, mobile, name) {
//                        rvListCategory1.setHasFixedSize(true)
//                        rvListCategory1.adapter = viewModel.orderHistory
//                        viewModel.orderHistory.notifyDataSetChanged()
//                        viewModel.orderHistory.submitList(this)
//                    }
                }
            }



        }
    }
}
package com.shimmer.store.ui.main.searchOrder

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.shimmer.store.R
import com.shimmer.store.databinding.SearchOrderBinding
import com.shimmer.store.datastore.DataStoreKeys.ADMIN_TOKEN
import com.shimmer.store.datastore.DataStoreUtil.readData
import com.shimmer.store.datastore.db.SearchModel
import com.shimmer.store.ui.mainActivity.MainActivity
import com.shimmer.store.utils.changeDateFormat
import com.shimmer.store.utils.getPatternFormat
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint
import org.jsoup.internal.StringUtil.isNumeric

@AndroidEntryPoint
class SearchOrder : Fragment() {
    private val viewModel: SearchOrderVM by viewModels()
    private var _binding: SearchOrderBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SearchOrderBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MainActivity.mainActivity.get()!!.callBack(0)
        binding.apply {
            topBarBack.includeBackButton.apply {
                layoutBack.singleClick {
                    findNavController().navigateUp()
                }
            }

            topBarBack.ivCartLayout.visibility = View.GONE
            ivEditSearch.setHint(resources.getString(R.string.enterOrderNumber))

//            val userList = listOf(
//                SearchModel(_id = 1, search_name = "Search1", currentTime = System.currentTimeMillis()),
//                SearchModel(_id = 1, search_name = "Search1", currentTime = System.currentTimeMillis()),
//                SearchModel(_id = 1, search_name = "Search1", currentTime = System.currentTimeMillis()),
//                SearchModel(_id = 1, search_name = "Search1", currentTime = System.currentTimeMillis()),
//                SearchModel(_id = 1, search_name = "Search1", currentTime = System.currentTimeMillis()),
//                SearchModel(_id = 1, search_name = "Search1", currentTime = System.currentTimeMillis()),
//            )


            val userList = mutableListOf<SearchOrderModel>()

//            rvList.setHasFixedSize(true)
//            rvList.adapter = viewModel.searchOrderAdapter
//            viewModel.searchOrderAdapter.notifyDataSetChanged()
//            viewModel.searchOrderAdapter.submitList(userList)
//            rvList.visibility = View.VISIBLE


            ivEditSearch.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    readData(ADMIN_TOKEN) { token ->
                        viewModel.orderHistoryListDetail(token.toString(), "12") {
                            val itemOrderDetail = this

                            userList.add(SearchOrderModel(
                                orderNo = ""+itemOrderDetail.entity_id,
                                noofItems = itemOrderDetail.items.size,
                                dateTime = itemOrderDetail.created_at,
                                base_subtotal = itemOrderDetail.base_subtotal,
                            ))

                            rvList.setHasFixedSize(true)
                            rvList.adapter = viewModel.searchOrderAdapter
                            viewModel.searchOrderAdapter.notifyDataSetChanged()
                            viewModel.searchOrderAdapter.submitList(userList)
                        }
                    }
                }
                true
            }


        }

    }
}




data class SearchOrderModel (
    val orderNo: String = "",
    val noofItems: Int = 0,
    val dateTime: String = "",
    val base_subtotal: Double = 0.0,

)
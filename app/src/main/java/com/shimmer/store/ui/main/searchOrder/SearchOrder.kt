package com.shimmer.store.ui.main.searchOrder

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.shimmer.store.R
import com.shimmer.store.databinding.SearchOrderBinding
import com.shimmer.store.datastore.db.SearchModel
import com.shimmer.store.ui.mainActivity.MainActivity
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint

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

            val userList = listOf(
                SearchModel(_id = 1, search_name = "Search1", currentTime = System.currentTimeMillis()),
                SearchModel(_id = 1, search_name = "Search1", currentTime = System.currentTimeMillis()),
                SearchModel(_id = 1, search_name = "Search1", currentTime = System.currentTimeMillis()),
                SearchModel(_id = 1, search_name = "Search1", currentTime = System.currentTimeMillis()),
                SearchModel(_id = 1, search_name = "Search1", currentTime = System.currentTimeMillis()),
                SearchModel(_id = 1, search_name = "Search1", currentTime = System.currentTimeMillis()),
            )

            rvList.setHasFixedSize(true)
            rvList.adapter = viewModel.searchOrderAdapter
            viewModel.searchOrderAdapter.notifyDataSetChanged()
            viewModel.searchOrderAdapter.submitList(userList)
            rvList.visibility = View.VISIBLE

        }

    }
}
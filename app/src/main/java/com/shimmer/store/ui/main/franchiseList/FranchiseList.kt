package com.shimmer.store.ui.main.franchiseList

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
import com.shimmer.store.databinding.FranchiseListBinding
import com.shimmer.store.datastore.db.SearchModel
import com.shimmer.store.ui.mainActivity.MainActivity
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.loginType
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FranchiseList : Fragment() {
    private val viewModel: FranchiseListVM by viewModels()
    private var _binding: FranchiseListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FranchiseListBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.apply {
            topBarSearch.apply {
                appicon.setImageDrawable(
                    ContextCompat.getDrawable(
                        MainActivity.context.get()!!,
                        R.drawable.baseline_west_24
                    )
                )

                appicon.singleClick {
                    findNavController().navigateUp()
                }


//                viewModel.searchValue.observe(viewLifecycleOwner) {
//                    binding.topBarSearch.editTextSearch.setText(it)
//                }
//
//                viewModel.searchDelete.observe(viewLifecycleOwner) {
//                    if (it) {
//                        openSearchHistory()
//                    }
//                }
//                editTextSearch.singleClick {
//                    openSearchHistory()
//                }

//                editTextSearch.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
//                    if (hasFocus) {
//                        openSearchHistory()
//                    }
//                })
            }

            val iconTypeSearch = when (resources.getInteger(R.integer.layout_value)){
                1 -> R.drawable.baseline_search_24
                2 -> R.drawable.baseline_search_32
                3 -> R.drawable.baseline_search_50
                else -> R.drawable.baseline_search_24
            }
            Log.e("TAG", "onViewCreated: "+resources.getInteger(R.integer.layout_value))
            topBarSearch.editTextSearch.setCompoundDrawablesWithIntrinsicBounds(0, 0, iconTypeSearch, 0)

val userList = listOf(
    SearchModel(_id = 1, search_name = "Search1", currentTime = System.currentTimeMillis()),
    SearchModel(_id = 1, search_name = "Search1", currentTime = System.currentTimeMillis()),
    SearchModel(_id = 1, search_name = "Search1", currentTime = System.currentTimeMillis()),
    SearchModel(_id = 1, search_name = "Search1", currentTime = System.currentTimeMillis()),
    SearchModel(_id = 1, search_name = "Search1", currentTime = System.currentTimeMillis()),
    SearchModel(_id = 1, search_name = "Search1", currentTime = System.currentTimeMillis()),
    )

            rvListSearchHistory.setHasFixedSize(true)
            rvListSearchHistory.adapter = viewModel.franchiseListAdapter
            viewModel.franchiseListAdapter.notifyDataSetChanged()
            viewModel.franchiseListAdapter.submitList(userList)
            rvListSearchHistory.visibility = View.VISIBLE




            layoutSort.singleClick {
                when(loginType){
                    "vendor" ->  {
//                        findNavController().navigate(R.id.action_orderSummary_to_home)
                    }
                    "guest" ->  {
                        findNavController().navigate(R.id.action_franchiseList_to_home)
                    }
                }
            }

        }



    }
}
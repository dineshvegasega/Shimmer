package com.shimmer.store.ui.main.search

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.shimmer.store.R
import com.shimmer.store.databinding.DialogSearchHistoryBinding
import com.shimmer.store.databinding.SearchBinding
import com.shimmer.store.datastore.db.SearchModel
import com.shimmer.store.ui.mainActivity.MainActivity
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.db
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.isBackStack
import com.shimmer.store.utils.ioThread
import com.shimmer.store.utils.onRightDrawableClicked
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class Search : Fragment() {
    private val viewModel: SearchVM by viewModels()
    private var _binding: SearchBinding? = null
    private val binding get() = _binding!!


    var searchType = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SearchBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isBackStack = true
        MainActivity.mainActivity.get()!!.callBack(0)


        binding.apply {
//            appicon.singleClick {
//                findNavController().navigateUp()
//            }

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


                editTextSearch.singleClick {
                    rvListSearchHistory.setHasFixedSize(true)
                    rvListSearchHistory.adapter = viewModel.searchHistoryAdapter
                    viewModel.searchHistoryAdapter.notifyDataSetChanged()
                    viewModel.searchHistoryAdapter.submitList(viewModel.item1)
                }

                editTextSearch.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
                    if (hasFocus) {
                        Log.e("TAG", "Focused Now!")
                        rvListSearchHistory.setHasFixedSize(true)
                        rvListSearchHistory.adapter = viewModel.searchHistoryAdapter
                        viewModel.searchHistoryAdapter.notifyDataSetChanged()
                        viewModel.searchHistoryAdapter.submitList(viewModel.item1)
                    }
                })
            }

            val iconTypeSearch = when (resources.getInteger(R.integer.layout_value)){
                1 -> R.drawable.baseline_search_24
                2 -> R.drawable.baseline_search_32
                3 -> R.drawable.baseline_search_50
                else -> R.drawable.baseline_search_24
            }
            Log.e("TAG", "onViewCreated: "+resources.getInteger(R.integer.layout_value))
            topBarSearch.editTextSearch.setCompoundDrawablesWithIntrinsicBounds(0, 0, iconTypeSearch, 0)

            setSearchButtons()

            btProduct.singleClick {
                searchType = 1
                setSearchButtons()
            }

            btSKU.singleClick {
                searchType = 2
                setSearchButtons()
            }

            ivSearch.singleClick {
                val newUser = SearchModel(search_name = "abc", currentTime = System.currentTimeMillis())
                ioThread {
                    db?.searchDao()?.insertAll(newUser)
                    val userList: List<SearchModel> ?= db?.searchDao()?.getAll()
                    userList?.forEach {
                        Log.e("TAG", "onViewCreated: "+it.search_name + " it.currentTime "+it.currentTime)
                    }
                }
            }


            rvList2.setHasFixedSize(true)
            rvList2.adapter = viewModel.searchAdapter
            viewModel.searchAdapter.notifyDataSetChanged()
            viewModel.searchAdapter.submitList(viewModel.item1)







        }


        searchHandler()
    }

    private fun setSearchButtons() {
        if (searchType == 1) {
            binding.topBarSearch.editTextSearch.hint = resources.getString(R.string.search_by_product)
            binding.btProduct.setTextColor(ContextCompat.getColor(MainActivity.context.get()!!, R.color.white))
            binding.btSKU.setTextColor(ContextCompat.getColor(MainActivity.context.get()!!, R.color.black))
            binding.btProduct.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color._363438))
            binding.btSKU.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white))
            binding.btProduct.strokeColor = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color._363438))
            binding.btSKU.strokeColor = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color._363438))
        } else {
            binding.topBarSearch.editTextSearch.hint = getString(R.string.search_by_sku)
            binding.btProduct.setTextColor(ContextCompat.getColor(MainActivity.context.get()!!, R.color.black))
            binding.btSKU.setTextColor(ContextCompat.getColor(MainActivity.context.get()!!, R.color.white))
            binding.btProduct.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white))
            binding.btSKU.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color._363438))
            binding.btProduct.strokeColor = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color._363438))
            binding.btSKU.strokeColor = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color._363438))
        }

//        btProduct.setTextColor(ContextCompat.getColor(MainActivity.context.get()!!, R.color.white))
//        btProduct.setBackgroundResource(R.drawable.bg_all_round_black)
//        btSKU.setTextColor(ContextCompat.getColor(MainActivity.context.get()!!, R.color.black))
//        btSKU.setBackgroundResource(R.drawable.bg_top_round_corner)
    }


    private fun searchHandler() {
        binding.apply {
            topBarSearch.editTextSearch.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                   // loadFirstPage()
                }
                true
            }

            topBarSearch.editTextSearch.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val iconTypeSearch = when (resources.getInteger(R.integer.layout_value)){
                        1 -> R.drawable.baseline_search_24
                        2 -> R.drawable.baseline_search_32
                        3 -> R.drawable.baseline_search_50
                        else -> R.drawable.baseline_search_24
                    }
                    topBarSearch.editTextSearch.setCompoundDrawablesWithIntrinsicBounds(0, 0, if(count >= 1) R.drawable.ic_cross_white else iconTypeSearch, 0);
                }
            })

            topBarSearch.editTextSearch.onRightDrawableClicked {
                it.text.clear()
//                loadFirstPage()
            }
        }
    }




    @SuppressLint("NotifyDataSetChanged")
    private fun openDialog() {
        val dialogBinding = DialogSearchHistoryBinding.inflate(requireContext().getSystemService(
            Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        )
        val dialog = Dialog(requireContext())
        dialog.setContentView(dialogBinding.root)
//        dialog.setOnShowListener { dia ->
//            val bottomSheetDialog = dia as BottomSheetDialog
//            val bottomSheetInternal: FrameLayout =
//                bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)!!
//            bottomSheetInternal.setBackgroundResource(R.drawable.bg_top_round_corner)
//        }
        dialog.show()
        val window = dialog.window
        window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )


        dialogBinding.rvListSearchHistory.setHasFixedSize(true)
        dialogBinding.rvListSearchHistory.adapter = viewModel.searchHistoryAdapter
        viewModel.searchHistoryAdapter.notifyDataSetChanged()
        viewModel.searchHistoryAdapter.submitList(viewModel.item1)

    }



}
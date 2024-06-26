package com.shimmer.store.ui.main.category

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.shimmer.store.R
import com.shimmer.store.databinding.CategoryBinding
import com.shimmer.store.ui.mainActivity.MainActivity
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.hideValueOff
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.isBackStack
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Category : Fragment() {
    private val viewModel: CategoryVM by viewModels()
    private var _binding: CategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CategoryBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isBackStack = false
        hideValueOff = 2
        MainActivity.mainActivity.get()!!.callBack(1)
        binding.apply {
            button.setOnClickListener {
                findNavController().navigate(R.id.action_category_to_products)
            }


            topBar.apply {
                textViewTitle.visibility = View.VISIBLE
                ivSearch.visibility = View.VISIBLE
                ivCart.visibility = View.VISIBLE

                ivSearch.singleClick {
                    findNavController().navigate(R.id.action_category_to_search)
                }

                ivCart.singleClick {
                    findNavController().navigate(R.id.action_category_to_cart)
                }
            }
        }

    }
}
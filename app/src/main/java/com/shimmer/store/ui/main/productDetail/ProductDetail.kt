package com.shimmer.store.ui.main.productDetail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.shimmer.store.databinding.ProductDetailBinding
import com.shimmer.store.ui.mainActivity.MainActivity
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.hideValueOff
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.isBackStack
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetail : Fragment() {
    private val viewModel: ProductDetailVM by viewModels()
    private var _binding: ProductDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProductDetailBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isBackStack = true
        hideValueOff = 2
        MainActivity.mainActivity.get()!!.callBack(2)
    }
}
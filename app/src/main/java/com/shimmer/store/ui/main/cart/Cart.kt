package com.shimmer.store.ui.main.cart

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.shimmer.store.databinding.CartBinding
import com.shimmer.store.ui.mainActivity.MainActivity
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.isBackStack
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Cart : Fragment() {
    private val viewModel: CartVM by viewModels()
    private var _binding: CartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CartBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isBackStack = true
        MainActivity.mainActivity.get()!!.callBack(2)
//        Log.e("TAG", "onViewCreated: ${isHide.value}")
        binding.apply {
//            appicon.setOnClickListener {
//                findNavController().navigateUp()
//            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
      //  MainActivity.mainActivity.get()!!.callBack(true)
    }


}
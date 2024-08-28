package com.shimmer.store.ui.main.payment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.shimmer.store.R
import com.shimmer.store.databinding.FaqBinding
import com.shimmer.store.databinding.PaymentBinding
import com.shimmer.store.ui.main.faq.FaqVM
import com.shimmer.store.ui.mainActivity.MainActivity
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.hideValueOff
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.isBackStack
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Payment : Fragment() {
    private val viewModel: PaymentVM by viewModels()

    private var _binding: PaymentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PaymentBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isBackStack = false
        hideValueOff = 2
        MainActivity.mainActivity.get()!!.callBack(1)
        //MainActivity.mainActivity.get()!!.callCartApi()
        binding.apply {
            topBarBack.apply {
                includeBackButton.layoutBack.singleClick {
                    findNavController().navigateUp()
                }

                ivCartLayout.visibility = View.GONE
            }



            layoutSort.singleClick {
                findNavController().navigate(R.id.action_payment_to_thankyou)
            }
        }
    }
}
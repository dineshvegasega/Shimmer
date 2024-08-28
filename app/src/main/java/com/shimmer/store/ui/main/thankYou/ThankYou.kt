package com.shimmer.store.ui.main.thankYou

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.shimmer.store.R
import com.shimmer.store.databinding.ThankyouBinding
import com.shimmer.store.ui.enums.LoginType
import com.shimmer.store.ui.mainActivity.MainActivity
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.isBackStack
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.loginType
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ThankYou : Fragment() {
    private val viewModel: ThankYouVM by viewModels()
    private var _binding: ThankyouBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ThankyouBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged", "ClickableViewAccessibility", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isBackStack = true
        MainActivity.mainActivity.get()!!.callBack(2)


        binding.apply {

            when(loginType){
                LoginType.VENDOR ->  {
                    textTitle2.text = "Your order has been successfully placed."
                }
                LoginType.CUSTOMER ->  {
                    textTitle2.text = "Your order has been successfully placed.\n" +
                            "Franchise will contact you within 24hrs"
                }
            }

            btHome.singleClick {
                findNavController().navigate(R.id.action_thankyou_to_home)
            }
        }
    }
}
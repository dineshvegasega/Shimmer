package com.shimmer.store.ui.main.filters

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.shimmer.store.R
import com.shimmer.store.databinding.FiltersBinding
import com.shimmer.store.ui.mainActivity.MainActivity
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.hideValueOff
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.isBackStack
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.typefaceroboto_light
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.typefaceroboto_medium
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Filters : Fragment() {
    private val viewModel: FiltersVM by viewModels()
    private var _binding: FiltersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FiltersBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isBackStack = true
        hideValueOff = 3
        MainActivity.mainActivity.get()!!.callBack(0)


        binding.apply {

            ivIconCross.singleClick {
                findNavController().navigateUp()
            }



            rvList2.setHasFixedSize(true)
            rvList2.adapter = viewModel.recentAdapter
            viewModel.recentAdapter.notifyDataSetChanged()
            viewModel.recentAdapter.submitList(viewModel.item1)


            viewModel.itemPriceCount.observe(viewLifecycleOwner) {
                if(it != 0){
                    textPrice.text = "Price ($it)"
                }
            }

            textPrice.singleClick {
                textPrice.setTypeface(typefaceroboto_medium)
                textCategories.setTypeface(typefaceroboto_light)
                textMaterial.setTypeface(typefaceroboto_light)

                Handler(Looper.getMainLooper()).postDelayed({
                }, 200)
            }

            textCategories.singleClick {
                textPrice.setTypeface(typefaceroboto_light)
                textCategories.setTypeface(typefaceroboto_medium)
                textMaterial.setTypeface(typefaceroboto_light)

                Handler(Looper.getMainLooper()).postDelayed({
                }, 200)
            }

            textMaterial.singleClick {
                textPrice.setTypeface(typefaceroboto_light)
                textCategories.setTypeface(typefaceroboto_light)
                textMaterial.setTypeface(typefaceroboto_medium)

                Handler(Looper.getMainLooper()).postDelayed({
                }, 200)
            }

        }
    }
}
package com.shimmer.store.ui.main.category

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import com.shimmer.store.databinding.CategoryChildTabBinding
import com.shimmer.store.models.Items
import com.shimmer.store.ui.main.products.Products
import com.shimmer.store.ui.main.products.Products.Companion
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.mainShopFor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryChildTab (
    private val activity: FragmentActivity,
    private val videoPath: Items,
    position: Int
) : Fragment() {
    private var _binding: CategoryChildTabBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CategoryVM by viewModels()

    companion object {
        @JvmStatic
        lateinit var adapter2: CategoryChildTabAdapter
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CategoryChildTabBinding.inflate(inflater)
        return binding.root
    }
    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        _binding = FragmentExoPlayerBinding.bind(view)




        adapter2 = CategoryChildTabAdapter()
        binding.apply {
            adapter2.submitData(viewModel.item1)
            adapter2.notifyDataSetChanged()
            binding.rvList2.adapter = adapter2
        }
    }


    override fun onResume() {
        super.onResume()
//        Log.e("TAG", "onViewCreated: Fragment Position : ${videoPath.name}")
        mainShopFor.forEach {
            if (videoPath.name == it.name){
                it.isSelected = true
                Log.e("TAG", "onViewCreated: Fragment PositionIF : ${it.name}")
            } else {
                it.isSelected = false
                Log.e("TAG", "onViewCreated: Fragment PositionELSE : ${it.name}")
            }
        }
    }
}
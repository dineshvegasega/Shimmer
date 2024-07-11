package com.shimmer.store.ui.main.category

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.shimmer.store.databinding.CategoryChildTabBinding
import com.shimmer.store.models.Items
import com.shimmer.store.ui.main.products.Products
import com.shimmer.store.ui.main.products.Products.Companion
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.mainCategory
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.mainShopFor
import com.shimmer.store.utils.singleClick
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
        var mainSelect = 0
//        lateinit var adapter2: CategoryChildTabAdapter
    }


//    var adapter2: CategoryChildTabAdapter ?= null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CategoryChildTabBinding.inflate(inflater)
        return binding.root
    }
    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        adapter2 = CategoryChildTabAdapter()

        binding.apply {
//            adapter2.submitData(viewModel.item1)
//            adapter2.notifyDataSetChanged()
//            binding.rvList2.adapter = adapter2

            itemCategory1.textName.text = mainCategory[0].name
            itemCategory2.textName.text = mainCategory[1].name
            itemCategory3.textName.text = mainCategory[2].name
            itemCategory4.textName.text = mainCategory[3].name
            itemCategory5.textName.text = mainCategory[4].name
            itemCategory6.textName.text = mainCategory[5].name



            rvListCategory1.adapter = viewModel.subCategoryAdapter1
            itemCategory1.linearLayout.singleClick {
                rvListCategory1.adapter = viewModel.subCategoryAdapter1
                viewModel.subCategoryAdapter1.notifyDataSetChanged()
                viewModel.subCategoryAdapter1.submitList(mainCategory[0].subCategory)
                rvListCategory1.visibility = View.VISIBLE
                rvListCategory2.visibility = View.GONE
                rvListCategory3.visibility = View.GONE
                mainSelect = 1
            }

            itemCategory2.linearLayout.singleClick {
                rvListCategory1.adapter = viewModel.subCategoryAdapter1
                viewModel.subCategoryAdapter1.notifyDataSetChanged()
                viewModel.subCategoryAdapter1.submitList(mainCategory[1].subCategory)
                rvListCategory1.visibility = View.VISIBLE
                rvListCategory2.visibility = View.GONE
                rvListCategory3.visibility = View.GONE
                mainSelect = 2
            }



            rvListCategory2.adapter = viewModel.subCategoryAdapter2
            itemCategory3.linearLayout.singleClick {
                rvListCategory1.adapter = viewModel.subCategoryAdapter1
                viewModel.subCategoryAdapter2.notifyDataSetChanged()
                viewModel.subCategoryAdapter2.submitList(mainCategory[2].subCategory)
                rvListCategory1.visibility = View.GONE
                rvListCategory2.visibility = View.VISIBLE
                rvListCategory3.visibility = View.GONE
                mainSelect = 3
            }

            itemCategory4.linearLayout.singleClick {
                rvListCategory2.adapter = viewModel.subCategoryAdapter2
                viewModel.subCategoryAdapter2.notifyDataSetChanged()
                viewModel.subCategoryAdapter2.submitList(mainCategory[3].subCategory)
                rvListCategory1.visibility = View.GONE
                rvListCategory2.visibility = View.VISIBLE
                rvListCategory3.visibility = View.GONE
                mainSelect = 4
            }



            rvListCategory3.adapter = viewModel.subCategoryAdapter3
            itemCategory5.linearLayout.singleClick {
                rvListCategory3.adapter = viewModel.subCategoryAdapter3
                viewModel.subCategoryAdapter3.notifyDataSetChanged()
                viewModel.subCategoryAdapter3.submitList(mainCategory[4].subCategory)
                rvListCategory1.visibility = View.GONE
                rvListCategory2.visibility = View.GONE
                rvListCategory3.visibility = View.VISIBLE
                mainSelect = 5
            }

            itemCategory6.linearLayout.singleClick {
                rvListCategory3.adapter = viewModel.subCategoryAdapter3
                viewModel.subCategoryAdapter3.notifyDataSetChanged()
                viewModel.subCategoryAdapter3.submitList(mainCategory[5].subCategory)
                rvListCategory1.visibility = View.GONE
                rvListCategory2.visibility = View.GONE
                rvListCategory3.visibility = View.VISIBLE
                mainSelect = 6
            }
        }

    }


    @SuppressLint("NotifyDataSetChanged")
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
//        adapter2 = CategoryChildTabAdapter()

        viewModel.subCategoryAdapter1.notifyDataSetChanged()
        viewModel.subCategoryAdapter2.notifyDataSetChanged()
        viewModel.subCategoryAdapter3.notifyDataSetChanged()
    }
}
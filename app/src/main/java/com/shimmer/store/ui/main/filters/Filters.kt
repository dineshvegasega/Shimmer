package com.shimmer.store.ui.main.filters

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
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
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.arrayCategory
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.arrayMaterial
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.arrayPrice
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Filters : Fragment() {
    private val viewModel: FiltersVM by viewModels()
    private var _binding: FiltersBinding? = null
    private val binding get() = _binding!!

    var defaultRun = 1

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


            btClear.singleClick {
                arrayPrice.clear()
                arrayCategory.clear()
                arrayMaterial.clear()

                viewModel.itemPrice.map { mapPrice ->
                    if (mapPriceMatch(mapPrice.name) == true){
                        mapPrice.isSelected = true
                    } else {
                        mapPrice.isSelected = false
                    }
                }

                viewModel.itemCategory.map { mapCategory ->
                    if (mapCategoryMatch(mapCategory.name) == true){
                        mapCategory.isSelected = true
                    } else {
                        mapCategory.isSelected = false
                    }
                }

                viewModel.itemMaterial.map { mapMaterial ->
                    if (mapMaterialMatch(mapMaterial.name) == true){
                        mapMaterial.isSelected = true
                    } else {
                        mapMaterial.isSelected = false
                    }
                }

                viewModel.itemPriceCount.value = 0
                viewModel.itemCategoryCount.value = 0
                viewModel.itemMaterialCount.value = 0

                when(defaultRun){
                    1 -> {
                        rvList2.adapter = viewModel.priceAdapter
                        viewModel.priceAdapter.notifyDataSetChanged()
                        viewModel.priceAdapter.submitList(viewModel.itemPrice)
                    }
                    2 -> {
                        rvList2.adapter = viewModel.categoryAdapter
                        viewModel.categoryAdapter.notifyDataSetChanged()
                        viewModel.categoryAdapter.submitList(viewModel.itemCategory)
                    }
                    3 -> {
                        rvList2.adapter = viewModel.materialAdapter
                        viewModel.materialAdapter.notifyDataSetChanged()
                        viewModel.materialAdapter.submitList(viewModel.itemMaterial)
                    }
                }
            }

            btApply.singleClick {
                findNavController().navigateUp()
            }


//            arrayPrice.apply {
//                add("₹1000 - ₹10000")
//                add("₹10000 - ₹15000")
//                add("₹15000 - ₹20000")
//            }

            viewModel.itemPrice.map { mapPrice ->
                if (mapPriceMatch(mapPrice.name) == true){
                    mapPrice.isSelected = true
                } else {
                    mapPrice.isSelected = false
                }
            }

            val filteredPrice = viewModel.itemPrice.filter { it.isSelected == true }
            viewModel.itemPriceCount.value = filteredPrice.size

            rvList2.setHasFixedSize(true)
            rvList2.adapter = viewModel.priceAdapter
            viewModel.priceAdapter.notifyDataSetChanged()
            viewModel.priceAdapter.submitList(viewModel.itemPrice)


//            arrayCategory.apply {
//                add("ring")
//                add("necklace")
//                add("earring")
//            }

            viewModel.itemCategory.map { mapCategory ->
                if (mapCategoryMatch(mapCategory.name) == true){
                    mapCategory.isSelected = true
                } else {
                    mapCategory.isSelected = false
                }
            }

            val filteredCategory = viewModel.itemCategory.filter { it.isSelected == true }
            viewModel.itemCategoryCount.value = filteredCategory.size

//            rvList2.setHasFixedSize(true)
//            rvList2.adapter = viewModel.categoryAdapter
//            viewModel.categoryAdapter.notifyDataSetChanged()
//            viewModel.categoryAdapter.submitList(viewModel.itemCategory)



//            arrayMaterial.apply {
//                add("gold")
//                add("silver")
//                add("diamond")
//            }

            viewModel.itemMaterial.map { mapMaterial ->
                if (mapMaterialMatch(mapMaterial.name) == true){
                    mapMaterial.isSelected = true
                } else {
                    mapMaterial.isSelected = false
                }
            }

            val filteredMaterial = viewModel.itemMaterial.filter { it.isSelected == true }
            viewModel.itemMaterialCount.value = filteredMaterial.size

//            rvList2.setHasFixedSize(true)
//            rvList2.adapter = viewModel.materialAdapter
//            viewModel.materialAdapter.notifyDataSetChanged()
//            viewModel.materialAdapter.submitList(viewModel.itemMaterial)



            viewModel.itemPriceCount.observe(viewLifecycleOwner) {
                textPrice.text = if(it == 0) "Price" else "Price ($it)"
            }

            viewModel.itemCategoryCount.observe(viewLifecycleOwner) {
                textCategories.text = if(it == 0) "Categories" else "Categories ($it)"
            }

            viewModel.itemMaterialCount.observe(viewLifecycleOwner) {
                textMaterial.text = if(it == 0) "Material" else "Material ($it)"
            }

            textPrice.singleClick {
                defaultRun = 1
                textPrice.setTypeface(typefaceroboto_medium)
                textCategories.setTypeface(typefaceroboto_light)
                textMaterial.setTypeface(typefaceroboto_light)

                Handler(Looper.getMainLooper()).postDelayed({
//                    arrayPrice.clear()
//                    arrayPrice.apply {
//                        add("₹1000 - ₹10000")
//                        add("₹10000 - ₹15000")
////                        add("₹15000 - ₹20000")
//                    }

                    viewModel.itemPrice.map { mapPrice ->
                        if (mapPriceMatch(mapPrice.name) == true){
                            mapPrice.isSelected = true
                        } else {
                            mapPrice.isSelected = false
                        }
                    }

                    rvList2.setHasFixedSize(true)
                    rvList2.adapter = viewModel.priceAdapter
                    viewModel.priceAdapter.notifyDataSetChanged()
                    viewModel.priceAdapter.submitList(viewModel.itemPrice)
                }, 100)
            }

            textCategories.singleClick {
                defaultRun = 2
                textPrice.setTypeface(typefaceroboto_light)
                textCategories.setTypeface(typefaceroboto_medium)
                textMaterial.setTypeface(typefaceroboto_light)

                Handler(Looper.getMainLooper()).postDelayed({
//                    arrayCategory.clear()
//                    arrayCategory.apply {
//                        add("ring")
//                        add("necklace")
//                    }

                    viewModel.itemCategory.map { mapCategory ->
                        if (mapCategoryMatch(mapCategory.name) == true){
                            mapCategory.isSelected = true
                        } else {
                            mapCategory.isSelected = false
                        }
                    }

                    rvList2.setHasFixedSize(true)
                    rvList2.adapter = viewModel.categoryAdapter
                    viewModel.categoryAdapter.notifyDataSetChanged()
                    viewModel.categoryAdapter.submitList(viewModel.itemCategory)
                }, 100)
            }

            textMaterial.singleClick {
                defaultRun = 3
                textPrice.setTypeface(typefaceroboto_light)
                textCategories.setTypeface(typefaceroboto_light)
                textMaterial.setTypeface(typefaceroboto_medium)

                Handler(Looper.getMainLooper()).postDelayed({
//                    arrayMaterial.clear()
//                    arrayMaterial.apply {
//                        add("gold")
//                        add("silver")
//                    }

                    viewModel.itemMaterial.map { mapMaterial ->
                        if (mapMaterialMatch(mapMaterial.name) == true){
                            mapMaterial.isSelected = true
                        } else {
                            mapMaterial.isSelected = false
                        }
                    }

                    rvList2.setHasFixedSize(true)
                    rvList2.adapter = viewModel.materialAdapter
                    viewModel.materialAdapter.notifyDataSetChanged()
                    viewModel.materialAdapter.submitList(viewModel.itemMaterial)
                }, 100)
            }

        }
    }

    private fun mapPriceMatch(name: String): Boolean {
        arrayPrice.forEach {
            if(it.equals(name)){
                return true
            }
        }
        return false
    }


    private fun mapCategoryMatch(name: String): Boolean {
        arrayCategory.forEach {
            if(it.equals(name)){
                return true
            }
        }
        return false
    }


    private fun mapMaterialMatch(name: String): Boolean {
        arrayMaterial.forEach {
            if(it.equals(name)){
                return true
            }
        }
        return false
    }



}
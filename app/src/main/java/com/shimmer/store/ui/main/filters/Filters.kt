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
import com.shimmer.store.databinding.ItemFilterCategoryBinding
import com.shimmer.store.genericAdapter.GenericAdapter
import com.shimmer.store.models.Items
import com.shimmer.store.ui.mainActivity.MainActivity
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.hideValueOff
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.isBackStack
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.typefaceroboto_light
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.typefaceroboto_medium
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.mainCategory
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.mainMaterial
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.mainPrice
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.mainShopFor
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Filters : Fragment() {
    private val viewModel: FiltersVM by viewModels()
    private var _binding: FiltersBinding? = null
    private val binding get() = _binding!!

    companion object{
//        var headerAdapter : GenericAdapter<ItemFilterCategoryBinding, Items>? = null
    }

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
//                isFilterFromFrom = false
//                arrayPrice.clear()
//                arrayCategory.clear()
//                arrayMaterial.clear()
//                arrayShopFor.clear()

//                mainPrice.forEach {
//                    it.isSelected = false
//                }

//                headerAdapter = viewModel.categoryAdapter

//                viewModel.itemPrice.map { mapPrice ->
//                    if (mapPriceMatch(mapPrice.name) == true){
//                        mapPrice.isSelected = true
//                    } else {
//                        mapPrice.isSelected = false
//                    }
//                }

//                mainCategory.map { mapCategory ->
//                    if (mapCategoryMatch(mapCategory.name) == true){
//                        mapCategory.isSelected = true
//                    } else {
//                        mapCategory.isSelected = false
//                    }
//                }

//                viewModel.itemMaterial.map { mapMaterial ->
//                    if (mapMaterialMatch(mapMaterial.name) == true){
//                        mapMaterial.isSelected = true
//                    } else {
//                        mapMaterial.isSelected = false
//                    }
//                }
//
//                viewModel.itemShopFor.map { mapMaterial ->
//                    if (mapShopForMatch(mapMaterial.name) == true){
//                        mapMaterial.isSelected = true
//                    } else {
//                        mapMaterial.isSelected = false
//                    }
//                }


                mainPrice.forEach {
                    it.isSelected = false
                }

                mainCategory.forEach {
                    it.isSelected = false
                    if(it.isSelected == false){
                        it.subCategory.forEach { sub ->
                            sub.isSelected = false
                        }
                    }
                }

                mainMaterial.forEach {
                    it.isSelected = false
                }

                mainShopFor.forEach {
                    it.isSelected = false
                }

                viewModel.itemPriceCount.value = 0
                viewModel.itemCategoryCount.value = 0
                viewModel.itemMaterialCount.value = 0
                viewModel.itemShopForCount.value = 0

                when(defaultRun){
                    1 -> {
                        rvList2.adapter = viewModel.priceAdapter
                        viewModel.priceAdapter.notifyDataSetChanged()
                        viewModel.priceAdapter.submitList(mainPrice)
                    }
                    2 -> {
                        rvList2.adapter = viewModel.categoryAdapter
                        viewModel.categoryAdapter.notifyDataSetChanged()
                        viewModel.categoryAdapter.submitList(mainCategory)
                    }
                    3 -> {
                        rvList2.adapter = viewModel.materialAdapter
                        viewModel.materialAdapter.notifyDataSetChanged()
                        viewModel.materialAdapter.submitList(mainMaterial)
                    }
                    4 -> {
                        rvList2.adapter = viewModel.shopForAdapter
                        viewModel.shopForAdapter.notifyDataSetChanged()
                        viewModel.shopForAdapter.submitList(mainShopFor)
                    }
                }
            }

            btApply.singleClick {
//                isFilterFromFrom = false
                findNavController().navigateUp()
            }


//            arrayPrice.apply {
//                add("₹1000 - ₹10000")
//                add("₹10000 - ₹15000")
//                add("₹15000 - ₹20000")
//            }

//            viewModel.itemPrice.map { mapPrice ->
//                if (mapPriceMatch(mapPrice.name) == true){
//                    mapPrice.isSelected = true
//                } else {
//                    mapPrice.isSelected = false
//                }
//            }

            val filteredPrice = mainPrice.filter { it.isSelected == true }
            viewModel.itemPriceCount.value = filteredPrice.size

            rvList2.setHasFixedSize(true)
            rvList2.adapter = viewModel.priceAdapter
            viewModel.priceAdapter.notifyDataSetChanged()
            viewModel.priceAdapter.submitList(mainPrice)


//            arrayCategory.apply {
//                add("ring")
//                add("necklace")
//                add("earring")
//            }

//            mainCategory.map { mapCategory ->
//                if (mapCategoryMatch(mapCategory.name) == true){
//                    mapCategory.isSelected = true
//                } else {
//                    mapCategory.isSelected = false
//                }
//            }

//            val filteredCategory = mainCategory.filter { it.isSelected == true }
//            viewModel.itemCategoryCount.value = filteredCategory.size

//            rvList2.setHasFixedSize(true)
//            rvList2.adapter = viewModel.categoryAdapter
//            viewModel.categoryAdapter.notifyDataSetChanged()
//            viewModel.categoryAdapter.submitList(viewModel.itemCategory)



//            arrayMaterial.apply {
//                add("gold")
//                add("silver")
//                add("diamond")
//            }

//            viewModel.itemMaterial.map { mapMaterial ->
//                if (mapMaterialMatch(mapMaterial.name) == true){
//                    mapMaterial.isSelected = true
//                } else {
//                    mapMaterial.isSelected = false
//                }
//            }

            val filteredMaterial = mainMaterial.filter { it.isSelected == true }
            viewModel.itemMaterialCount.value = filteredMaterial.size

//            rvList2.setHasFixedSize(true)
//            rvList2.adapter = viewModel.materialAdapter
//            viewModel.materialAdapter.notifyDataSetChanged()
//            viewModel.materialAdapter.submitList(viewModel.itemMaterial)


//            viewModel.itemShopFor.map { mapShopFor ->
//                if (mapShopForMatch(mapShopFor.name) == true){
//                    mapShopFor.isSelected = true
//                } else {
//                    mapShopFor.isSelected = false
//                }
//            }

            val filteredShopFor = mainShopFor.filter { it.isSelected == true }
            viewModel.itemShopForCount.value = filteredShopFor.size

//            rvList2.setHasFixedSize(true)
//            rvList2.adapter = viewModel.shopForAdapter
//            viewModel.shopForAdapter.notifyDataSetChanged()
//            viewModel.shopForAdapter.submitList(viewModel.itemShopFor)


            viewModel.itemPriceCount.observe(viewLifecycleOwner) {
                textPrice.text = if(it == 0) "Price" else "Price ($it)"
            }

            viewModel.itemCategoryCount.observe(viewLifecycleOwner) {
                var count = 0
                mainCategory.forEach {
                    it.subCategory.forEach { sub ->
                        if(sub.isSelected){
                            Log.e("TAG" , "it.isSelected ${sub.isSelected}")
                            count += 1
                        }
                    }
                }

//                mainPrice.forEach {
//                    if(it.isSelected){
//                        count += 1
//                    }
//                }
//
//                mainMaterial.forEach {
//                    if(it.isSelected){
//                        count += 1
//                    }
//                }
//
//                mainShopFor.forEach {
//                    if(it.isSelected){
//                        count += 1
//                    }
//                }

                Log.e("TAG" , "count ${count}")
                textCategories.text = if(count == 0) "Categories" else "Categories ($count)"
            }

            viewModel.itemMaterialCount.observe(viewLifecycleOwner) {
                textMaterial.text = if(it == 0) "Material" else "Material ($it)"
            }

            viewModel.itemShopForCount.observe(viewLifecycleOwner) {
                textShopFor.text = if(it == 0) "Shop For" else "Shop For ($it)"
            }

            textPrice.singleClick {
                defaultRun = 1
                textPrice.setTypeface(typefaceroboto_medium)
                textCategories.setTypeface(typefaceroboto_light)
                textMaterial.setTypeface(typefaceroboto_light)
                textShopFor.setTypeface(typefaceroboto_light)

                Handler(Looper.getMainLooper()).postDelayed({
//                    arrayPrice.clear()
//                    arrayPrice.apply {
//                        add("₹1000 - ₹10000")
//                        add("₹10000 - ₹15000")
////                        add("₹15000 - ₹20000")
//                    }

//                    viewModel.itemPrice.map { mapPrice ->
//                        if (mapPriceMatch(mapPrice.name) == true){
//                            mapPrice.isSelected = true
//                        } else {
//                            mapPrice.isSelected = false
//                        }
//                    }

                    rvList2.setHasFixedSize(true)
                    rvList2.adapter = viewModel.priceAdapter
                    viewModel.priceAdapter.notifyDataSetChanged()
                    viewModel.priceAdapter.submitList(mainPrice)
                }, 100)
            }

            textCategories.singleClick {
//                isFilterFromFrom = false
                defaultRun = 2
                textPrice.setTypeface(typefaceroboto_light)
                textCategories.setTypeface(typefaceroboto_medium)
                textMaterial.setTypeface(typefaceroboto_light)
                textShopFor.setTypeface(typefaceroboto_light)

                Handler(Looper.getMainLooper()).postDelayed({
//                    arrayCategory.clear()
//                    arrayCategory.apply {
//                        add("ring")
//                        add("necklace")
//                    }

//                    mainCategory.map { mapCategory ->
//                        if (mapCategoryMatch(mapCategory.name) == true){
//                            mapCategory.isSelected = true
//                        } else {
//                            mapCategory.isSelected = false
//                        }
//                    }

                    rvList2.setHasFixedSize(true)
                    rvList2.adapter = viewModel.categoryAdapter
                    viewModel.categoryAdapter.notifyDataSetChanged()
                    viewModel.categoryAdapter.submitList(mainCategory)
                }, 100)
            }

            textMaterial.singleClick {
                defaultRun = 3
                textPrice.setTypeface(typefaceroboto_light)
                textCategories.setTypeface(typefaceroboto_light)
                textMaterial.setTypeface(typefaceroboto_medium)
                textShopFor.setTypeface(typefaceroboto_light)

                Handler(Looper.getMainLooper()).postDelayed({
//                    arrayMaterial.clear()
//                    arrayMaterial.apply {
//                        add("gold")
//                        add("silver")
//                    }

//                    viewModel.itemMaterial.map { mapMaterial ->
//                        if (mapMaterialMatch(mapMaterial.name) == true){
//                            mapMaterial.isSelected = true
//                        } else {
//                            mapMaterial.isSelected = false
//                        }
//                    }

                    rvList2.setHasFixedSize(true)
                    rvList2.adapter = viewModel.materialAdapter
                    viewModel.materialAdapter.notifyDataSetChanged()
                    viewModel.materialAdapter.submitList(mainMaterial)
                }, 100)
            }

            textShopFor.singleClick {
                defaultRun = 3
                textPrice.setTypeface(typefaceroboto_light)
                textCategories.setTypeface(typefaceroboto_light)
                textMaterial.setTypeface(typefaceroboto_light)
                textShopFor.setTypeface(typefaceroboto_medium)

                Handler(Looper.getMainLooper()).postDelayed({
//                    arrayMaterial.clear()
//                    arrayMaterial.apply {
//                        add("gold")
//                        add("silver")
//                    }

//                    viewModel.itemMaterial.map { mapShopFor ->
//                        if (mapShopForMatch(mapShopFor.name) == true){
//                            mapShopFor.isSelected = true
//                        } else {
//                            mapShopFor.isSelected = false
//                        }
//                    }

                    rvList2.setHasFixedSize(true)
                    rvList2.adapter = viewModel.shopForAdapter
                    viewModel.shopForAdapter.notifyDataSetChanged()
                    viewModel.shopForAdapter.submitList(mainShopFor)
                }, 100)
            }
        }
    }

//    private fun mapPriceMatch(name: String): Boolean {
//        arrayPrice.forEach {
//            if(it.equals(name)){
//                return true
//            }
//        }
//        return false
//    }


//    private fun mapCategoryMatch(name: String): Boolean {
//        arrayCategory.forEach {
//            if(it.equals(name)){
//                return true
//            }
//        }
//        return false
//    }


//    private fun mapMaterialMatch(name: String): Boolean {
//        arrayMaterial.forEach {
//            if(it.equals(name)){
//                return true
//            }
//        }
//        return false
//    }
//
//
//    private fun mapShopForMatch(name: String): Boolean {
//        arrayShopFor.forEach {
//            if(it.equals(name)){
//                return true
//            }
//        }
//        return false
//    }

}
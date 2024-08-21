package com.shimmer.store.ui.main.products

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.shimmer.store.R
import com.shimmer.store.databinding.DialogSortBinding
import com.shimmer.store.databinding.ProductsBinding
import com.shimmer.store.datastore.DataStoreKeys.ADMIN_TOKEN
import com.shimmer.store.datastore.DataStoreUtil.readData
import com.shimmer.store.datastore.db.CartModel
import com.shimmer.store.models.products.ItemProduct
import com.shimmer.store.ui.mainActivity.MainActivity
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.db
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.hideValueOff
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.isBackStack
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.typefacenunitosans_light
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.typefacenunitosans_semibold
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.badgeCount
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.mainCategory
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.mainMaterial
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.mainPrice
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.mainShopFor
import com.shimmer.store.utils.mainThread
//import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.mainCategory
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class Products : Fragment() {
    private val viewModel: ProductsVM by viewModels()
    private var _binding: ProductsBinding? = null
    private val binding get() = _binding!!

    var sortAlert: BottomSheetDialog? = null


    companion object {

        @JvmStatic
        lateinit var adapter2: ProductsAdapter

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProductsBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged", "ClickableViewAccessibility", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isBackStack = true
        hideValueOff = 1
        MainActivity.mainActivity.get()!!.callBack(2)

        adapter2 = ProductsAdapter()
        binding.rvList2.adapter = adapter2

        binding.apply {
            topBarProducts.includeBackButton.apply {
                layoutBack.singleClick {
                    findNavController().navigateUp()
                }
            }
//            button.setOnClickListener {
//                findNavController().navigate(R.id.action_products_to_productsDetail)
//            }


//           val leftMaxima = requireArguments().serializable("filters") as HashMap<String, Any?>?
//
//            Log.e("TAG", "leftMaxima "+leftMaxima?.get("category"))


//            mainCategory.forEach {
//                Log.e("TAG", "itmainCategory "+it.isSelected)
//            }


//            topBar.apply {
//                textViewTitle.visibility = View.GONE
////                cardSearch.visibility = View.GONE
//
//                appicon.setImageDrawable(
//                    ContextCompat.getDrawable(
//                        MainActivity.context.get()!!,
//                        R.drawable.baseline_west_24
//                    )
//                )
//
//                appicon.singleClick {
//                    findNavController().navigateUp()
//                }
//
//                ivSearch.singleClick {
//                    findNavController().navigate(R.id.action_products_to_search)
//                }
//
//                ivCart.singleClick {
//                    findNavController().navigate(R.id.action_products_to_cart)
//                }
//
//                badgeCount.observe(viewLifecycleOwner) { badgeCount ->
//                    viewModel.getCartCount() {
//                        Log.e("TAG", "countAA: $this")
//                        menuBadge.text = "${this}"
//                        menuBadge.visibility = if (this != 0) View.VISIBLE else View.GONE
//                    }
//                }
//            }



            filters()
//
//            searchCriteria[filter_groups][0][filters][0][field]
//            searchCriteria[filter_groups][0][filters][1][field]
//            searchCriteria[filter_groups][0][filters][2][field]
//
//
//            searchCriteria[filter_groups][0][filters][0][field]
//            searchCriteria[filter_groups][1][filters][0][field]


            layoutSort.singleClick {
                if (sortAlert?.isShowing == true) {
                    return@singleClick
                }
                val dialogBinding = DialogSortBinding.inflate(
                    root.context.getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE
                    ) as LayoutInflater
                )
                val dialog = BottomSheetDialog(root.context)
                dialog.setContentView(dialogBinding.root)
                dialog.setOnShowListener { dia ->
                    sortAlert = dia as BottomSheetDialog
                    val bottomSheetInternal: FrameLayout =
                        sortAlert?.findViewById(com.google.android.material.R.id.design_bottom_sheet)!!
                    bottomSheetInternal.setBackgroundResource(R.drawable.bg_top_round_corner)
                }
                dialog.show()

                dialogBinding.apply {
                    ivIconCross.singleClick {
                        dialog.dismiss()
                    }

                    when (viewModel.sortFilter) {
                        1 -> {
                            textDefaultSort.setTypeface(typefacenunitosans_semibold)
                            textPriceLowToHighSort.setTypeface(typefacenunitosans_light)
                            textPriceHighToLowSort.setTypeface(typefacenunitosans_light)
                        }

                        2 -> {
                            textDefaultSort.setTypeface(typefacenunitosans_light)
                            textPriceLowToHighSort.setTypeface(typefacenunitosans_semibold)
                            textPriceHighToLowSort.setTypeface(typefacenunitosans_light)
                        }

                        3 -> {
                            textDefaultSort.setTypeface(typefacenunitosans_light)
                            textPriceLowToHighSort.setTypeface(typefacenunitosans_light)
                            textPriceHighToLowSort.setTypeface(typefacenunitosans_semibold)
                        }
                    }

                    textDefaultSort.singleClick {
                        // textDefaultSort.setTextColor(ContextCompat.getColor(binding.root.context, R.color.white))
                        textDefaultSort.setTypeface(typefacenunitosans_semibold)
                        textPriceLowToHighSort.setTypeface(typefacenunitosans_light)
                        textPriceHighToLowSort.setTypeface(typefacenunitosans_light)
                        viewModel.sortFilter = 1
//                        Handler(Looper.getMainLooper()).postDelayed({
//                            dialog.dismiss()
//                        }, 700)
                    }

                    textPriceLowToHighSort.singleClick {
                        textDefaultSort.setTypeface(typefacenunitosans_light)
                        textPriceLowToHighSort.setTypeface(typefacenunitosans_semibold)
                        textPriceHighToLowSort.setTypeface(typefacenunitosans_light)
                        viewModel.sortFilter = 2
//                        Handler(Looper.getMainLooper()).postDelayed({
//                            dialog.dismiss()
//                        }, 700)
                    }

                    textPriceHighToLowSort.singleClick {
                        textDefaultSort.setTypeface(typefacenunitosans_light)
                        textPriceLowToHighSort.setTypeface(typefacenunitosans_light)
                        textPriceHighToLowSort.setTypeface(typefacenunitosans_semibold)
                        viewModel.sortFilter = 3
//                        Handler(Looper.getMainLooper()).postDelayed({
//                            dialog.dismiss()
//                        }, 700)
                    }

                    btClear.singleClick {
                        textDefaultSort.setTypeface(typefacenunitosans_light)
                        textPriceLowToHighSort.setTypeface(typefacenunitosans_light)
                        textPriceHighToLowSort.setTypeface(typefacenunitosans_light)
                        viewModel.sortFilter = 0
                        filters()
                    }

                    btApply.singleClick {
                        filters()
                        dialog.dismiss()
                    }

                }

            }

            layoutFilter.singleClick {
                findNavController().navigate(R.id.action_products_to_filter)
            }


//            val hobbies = mapOf("searchCriteria[filter_groups][0][filters][0][field]" to "category_id",
//                "searchCriteria[filter_groups][0][filters][0][value]" to "13",
//            )

        }
    }




    @SuppressLint("NotifyDataSetChanged")
    fun filters(){
        binding.apply {
            val emptyMap = mutableMapOf<String, String>()
            var count = 0

            var categoryIds : String = ""
            var countFrom1 = 0
            var countFrom2 = 0
            var mainCategoryBoolean = false
            mainCategory.forEach {
                if (it.isSelected) {
                    categoryIds += ""+it.id+","
                    mainCategoryBoolean = true
                }

                it.subCategory.forEach { sub ->
                    if (sub.isSelected && sub.isAll == false) {
                        Log.e("TAG", "it.isSelected ${sub.isSelected} ids:${sub.id}")
                        count += 1
                        categoryIds += ""+sub.id+","
                    }
                }
                categoryIds = if(categoryIds.length > 0) categoryIds.substring(0, categoryIds.length-1) else ""
            }

            if (mainCategoryBoolean){
                emptyMap["searchCriteria[filter_groups][0][filters][" + countFrom1 + "][field]"] = "category_id"
                emptyMap["searchCriteria[filter_groups][0][filters][" + countFrom1 + "][value]"] = categoryIds
                emptyMap["searchCriteria[filter_groups][0][filters][" + countFrom1 + "][condition_type]"] = "in"
            }
            Log.e("TAG", "countFromAAA "+emptyMap.toString())

            var mainPriceBoolean = false
            var priceFrom : Double = 0.0
            var priceTo : Double = 0.0
            mainPrice.forEach {
                if (it.isSelected) {
                    count += 1
                    countFrom1 += 1
                    mainPriceBoolean = true
                    priceFrom = it.name.replace("₹","").split("-")[0].trim().toDouble()
                    priceTo = if(it.name.replace("₹","").split("-")[1].trim() == "Above") 10000000.00 else it.name.replace("₹","").split("-")[1].trim().toDouble()
                }
            }
//            Log.e("TAG", "countFrom2BBB "+countFrom1)
            if (mainPriceBoolean){
                countFrom2 += 1
                emptyMap["searchCriteria[filter_groups][" + countFrom2 + "][filters][0][field]"] = "price"
                emptyMap["searchCriteria[filter_groups][" + countFrom2 + "][filters][0][value]"] =
                    priceFrom.toString()
                emptyMap["searchCriteria[filter_groups][" + countFrom2 + "][filters][0][condition_type]"] = "from"

                countFrom2 += 1
                emptyMap["searchCriteria[filter_groups][" + countFrom2 + "][filters][0][field]"] = "price"
                emptyMap["searchCriteria[filter_groups][" + countFrom2 + "][filters][0][value]"] = priceTo.toString()
                emptyMap["searchCriteria[filter_groups][" + countFrom2 + "][filters][0][condition_type]"] = "to"
            }
            Log.e("TAG", "countFrom2BBB "+emptyMap.toString())

            var materialIds : String = ""
            var mainMaterialBoolean = false
            mainMaterial.forEach {
                if (it.isSelected) {
                    materialIds += ""+it.id+","
                    count += 1
                    countFrom1 += 1
                    mainMaterialBoolean = true
                }
            }
//            Log.e("TAG", "countFromCCC "+countFrom1)
            if (mainMaterialBoolean){
                emptyMap["searchCriteria[filter_groups][0][filters][" + countFrom1 + "][field]"] = "metal_type"
                emptyMap["searchCriteria[filter_groups][0][filters][" + countFrom1 + "][value]"] = materialIds
                emptyMap["searchCriteria[filter_groups][0][filters][" + countFrom1 + "][condition_type]"] = "in"
            }
            Log.e("TAG", "countFromCCC "+emptyMap.toString())

            var genderIds : String = ""
            var mainShopForBoolean = false
            mainShopFor.forEach {
                if (it.isSelected) {
                    genderIds += ""+it.id+","
                    count += 1
                    countFrom1 += 1
                    mainShopForBoolean = true
                }
            }
//            Log.e("TAG", "countFromDDD "+countFrom1)
            if (mainShopForBoolean){
                emptyMap["searchCriteria[filter_groups][0][filters][" + countFrom1 + "][field]"] = "gender"
                emptyMap["searchCriteria[filter_groups][0][filters][" + countFrom1 + "][value]"] = genderIds
                emptyMap["searchCriteria[filter_groups][0][filters][" + countFrom1 + "][condition_type]"] = "in"
            }
            Log.e("TAG", "countFromDDD "+emptyMap.toString())

            countFrom1 += 1
            emptyMap["searchCriteria[filter_groups][0][filters][" + countFrom1 + "][field]"] = "visibility"
            emptyMap["searchCriteria[filter_groups][0][filters][" + countFrom1 + "][value]"] = "4"
            emptyMap["searchCriteria[filter_groups][0][filters][" + countFrom1 + "][condition_type]"] = "eq"
            Log.e("TAG", "countFromDDD "+emptyMap.toString())


            when(viewModel.sortFilter){
                  0 -> {

                  }
                  1 -> {

                  }
                  2 -> {
                      countFrom2 += 1
                      emptyMap["searchCriteria[sortOrders][" + countFrom2 + "][field]"] = "price"
                      emptyMap["searchCriteria[sortOrders][" + countFrom2 + "][direction]"] = "ASC"
                  }
                  3 -> {
                      countFrom2 += 1
                      emptyMap["searchCriteria[sortOrders][" + countFrom2 + "][field]"] = "price"
                      emptyMap["searchCriteria[sortOrders][" + countFrom2 + "][direction]"] = "DESC"
                  }
            }
            Log.e("TAG", "countFrom2EEE "+countFrom2)


            Log.e("TAG", "count ${count}")
            textFilter.text = if (count == 0) "Filter" else "Filter ($count)"




            binding.rvList2.adapter = adapter2
            readData(ADMIN_TOKEN) { token ->
                viewModel.getProducts(token.toString(), requireView(), emptyMap) {
//                    Log.e("TAG", "itAAA " + this)
                    adapter2.submitData(this.items)
                    adapter2.notifyDataSetChanged()
                }
            }

        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
//        MainActivity.mainActivity.get()!!.callBack(true)
    }

}
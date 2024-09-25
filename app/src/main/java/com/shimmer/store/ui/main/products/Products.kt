package com.shimmer.store.ui.main.products

//import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.mainCategory
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.shimmer.store.R
import com.shimmer.store.databinding.DialogSortBinding
import com.shimmer.store.databinding.ProductsBinding
import com.shimmer.store.datastore.DataStoreKeys.ADMIN_TOKEN
import com.shimmer.store.datastore.DataStoreUtil.readData
import com.shimmer.store.models.products.ItemProduct
import com.shimmer.store.ui.mainActivity.MainActivity
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.hideValueOff
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.isBackStack
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.typefacenunitosans_light
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.typefacenunitosans_semibold
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.cartItemCount
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.cartItemLiveData
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.mainCategory
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.mainPrice
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.mainShopFor
import com.shimmer.store.utils.mainThread
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class Products : Fragment() {
    private val viewModel: ProductsVM by viewModels()
    private var _binding: ProductsBinding? = null
    private val binding get() = _binding!!

    var sortAlert: BottomSheetDialog? = null

    var page: Int = 1

//    companion object {
//        @JvmStatic
//        lateinit var adapter2: ProductsAdapter
//    }


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
        MainActivity.mainActivity.get()!!.callCartApi()

//        viewModel.searchAdapter = ProductsAdapter()
//        binding.rvList2.adapter = viewModel.searchAdapter

        binding.apply {
            topBarProducts.includeBackButton.apply {
                layoutBack.singleClick {
                    findNavController().navigateUp()
                }
            }

            topBarProducts.ivSearch.singleClick {
                findNavController().navigate(R.id.action_products_to_search)
            }

            topBarProducts.ivCart.singleClick {
                findNavController().navigate(R.id.action_products_to_cart)
            }


            cartItemLiveData.value = false
            cartItemLiveData.observe(viewLifecycleOwner) {
                topBarProducts.menuBadge.text = "$cartItemCount"
                topBarProducts.menuBadge.visibility =
                    if (cartItemCount != 0) View.VISIBLE else View.GONE
            }


            rvList2.setHasFixedSize(true)
            rvList2.adapter = viewModel.productAdapter

            viewModel.sortFilter = 1
            filters()





            viewModel.itemProducts?.observe(viewLifecycleOwner) {
                mainThread {
                    if (it.items.size != 0) {
//                        it.items.forEach { itemApi ->
//                            val bool = getArrayValue(viewModel.itemsProduct, itemApi)
//                            if (!bool) {
//                                viewModel.itemsProduct.add(itemApi)
//                            }
//                        }

//                        if (viewModel.itemsProduct.size == 0) {
                        it.items.forEach {
                            viewModel.itemsProduct.add(it)
                        }

//                        viewModel.itemsProduct.addAll(it.items)
//                        } else {
//                            it.items.forEach { itemApi ->
//                                viewModel.itemsProduct.forEach { itemArray ->
//                                    if (itemApi.id != itemArray.id) {
//                                        viewModel.itemsProduct.add(itemApi)
//                                    }
//                                }
//                            }
//                        }
                        Log.e("TAG", "it.itemsAAA " + it.items.size)
                    } else {
                        idPBLoading.visibility = View.GONE
                        Log.e("TAG", "it.itemsBBB " + it.items.size)
                    }

                    viewModel.productAdapter.submitList(viewModel.itemsProduct)
                    viewModel.productAdapter.notifyDataSetChanged()


                    Log.e("TAG", "it.itemsCCC " + it.items.size)

                    if (viewModel.itemsProduct.size == 0) {
                        binding.idDataNotFound.root.visibility = View.VISIBLE
                    } else {
                        binding.idDataNotFound.root.visibility = View.GONE
                    }
                }
            }


            idNestedSV.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                // on scroll change we are checking when users scroll as bottom.
                val aa = v.getChildAt(0).measuredHeight - v.measuredHeight

                Log.e("TAG", "scrollYAA $scrollY  ::  $aa")
                if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                    // in this method we are incrementing page number,
                    // making progress bar visible and calling get data method.
                    page++
                    idPBLoading.visibility = View.VISIBLE
//
//                    getDataFromAPI(page)
                    filters()
                }
            })






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

                    layoutClear.singleClick {
                        textDefaultSort.setTypeface(typefacenunitosans_light)
                        textPriceLowToHighSort.setTypeface(typefacenunitosans_light)
                        textPriceHighToLowSort.setTypeface(typefacenunitosans_light)
                        page = 1
                        viewModel.itemsProduct.clear()
                        viewModel.sortFilter = 0
                        filters()
                        dialog.dismiss()
                    }

                    layoutApply.singleClick {
                        page = 1
                        viewModel.itemsProduct.clear()
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

    private fun getArrayValue(itemProductArray: ArrayList<ItemProduct>, itemApi: ItemProduct): Boolean {

        itemProductArray.forEach {
            if(it.id == itemApi.id){
                return true
            } else {
                return false
            }
        }
        return false
    }


    @SuppressLint("NotifyDataSetChanged")
    fun filters() {
        binding.apply {
////            if (viewModel.productAdapter != null) {
//                if (page > viewModel.productAdapter.getItemCount()) {
//                    idPBLoading.visibility = View.GONE
//                    return
//                }
////            }


//            if (viewModel.itemsProduct.size == 10 && viewModel.itemsProduct.size <= 1){
//                return
//            } else {
//
//            }


            val emptyMap = mutableMapOf<String, String>()
            var count = 0

            var categoryIds: String = ""
            var countFrom1 = 0
            var countFrom2 = 0
            var mainCategoryBoolean = false
            mainCategory.forEach {
                if (it.isSelected) {
                    categoryIds += "" + it.id + ","
                    mainCategoryBoolean = true
                }

                it.subCategory.forEach { sub ->
                    if (sub.isSelected && sub.isAll == false) {
                        Log.e("TAG", "it.isSelected ${sub.isSelected} ids:${sub.id}")
                        count += 1
                        categoryIds += "" + sub.id + ","
                    }
                }
            }
//            categoryIds =
//                if (categoryIds.length > 0) categoryIds.substring(0, categoryIds.length - 1) else ""
            Log.e("TAG", "mainCategoryXX " + categoryIds)
            if (mainCategoryBoolean) {
                emptyMap["searchCriteria[filter_groups][0][filters][" + countFrom1 + "][field]"] =
                    "category_id"
                emptyMap["searchCriteria[filter_groups][0][filters][" + countFrom1 + "][value]"] =
                    categoryIds
                emptyMap["searchCriteria[filter_groups][0][filters][" + countFrom1 + "][condition_type]"] =
                    "in"
            }
            Log.e("TAG", "countFromAAA " + emptyMap.toString())


            var genderIds: String = ""
            var mainShopForBoolean = false
            mainShopFor.forEach {
                if (it.isSelected) {
                    genderIds += "" + it.id + ","
                    count += 1
//                    countFrom1 += 1
                    mainShopForBoolean = true
                }
            }
            Log.e("TAG", "countFromDDD " + countFrom1)
            if (mainShopForBoolean) {
                emptyMap["searchCriteria[filter_groups][0][filters][" + countFrom1 + "][field]"] =
                    "category_id"
                emptyMap["searchCriteria[filter_groups][0][filters][" + countFrom1 + "][value]"] =
                    categoryIds + genderIds
                emptyMap["searchCriteria[filter_groups][0][filters][" + countFrom1 + "][condition_type]"] =
                    "in"
            }


            var mainPriceBoolean = false
            var priceFrom: Double = 0.0
            var priceTo: Double = 0.0
            var xCount = 0
            mainPrice.forEach {
                if (it.isSelected) {
                    count += 1
                    countFrom1 += 1
                    mainPriceBoolean = true
                    if (xCount == 0) {
                        priceFrom = it.name.replace("₹", "").split("-")[0].trim().toDouble()
                        xCount = 1
                    }
                    priceTo = if (it.name.replace("₹", "")
                            .split("-")[1].trim() == "Above"
                    ) 10000000.00 else it.name.replace("₹", "").split("-")[1].trim().toDouble()
                }
            }

            Log.e("TAG", "countFrom2BBB " + priceFrom)
            Log.e("TAG", "countFrom2CCC " + priceTo)

//            Log.e("TAG", "countFrom2BBB "+countFrom1)
            if (mainPriceBoolean) {
                countFrom2 += 1
                emptyMap["searchCriteria[filter_groups][" + countFrom2 + "][filters][0][field]"] =
                    "price"
                emptyMap["searchCriteria[filter_groups][" + countFrom2 + "][filters][0][value]"] =
                    priceFrom.toString()
                emptyMap["searchCriteria[filter_groups][" + countFrom2 + "][filters][0][condition_type]"] =
                    "from"

                countFrom2 += 1
                emptyMap["searchCriteria[filter_groups][" + countFrom2 + "][filters][0][field]"] =
                    "price"
                emptyMap["searchCriteria[filter_groups][" + countFrom2 + "][filters][0][value]"] =
                    priceTo.toString()
                emptyMap["searchCriteria[filter_groups][" + countFrom2 + "][filters][0][condition_type]"] =
                    "to"
            }
            Log.e("TAG", "countFrom2BBB " + emptyMap.toString())

//            var materialIds : String = ""
//            var mainMaterialBoolean = false
//            mainMaterial.forEach {
//                if (it.isSelected) {
//                    materialIds += ""+it.id+","
//                    count += 1
//                    countFrom1 += 1
//                    mainMaterialBoolean = true
//                }
//            }
////            Log.e("TAG", "countFromCCC "+countFrom1)
//            if (mainMaterialBoolean){
//                emptyMap["searchCriteria[filter_groups][0][filters][" + countFrom1 + "][field]"] = "metal_type"
//                emptyMap["searchCriteria[filter_groups][0][filters][" + countFrom1 + "][value]"] = materialIds
//                emptyMap["searchCriteria[filter_groups][0][filters][" + countFrom1 + "][condition_type]"] = "in"
//            }
//            Log.e("TAG", "countFromCCC "+emptyMap.toString())
//
//            var genderIds : String = ""
//            var mainShopForBoolean = false
//            mainShopFor.forEach {
//                if (it.isSelected) {
//                    genderIds += ""+it.id+","
//                    count += 1
//                    countFrom1 += 1
//                    mainShopForBoolean = true
//                }
//            }
//            Log.e("TAG", "countFromDDD "+countFrom1)
//            if (mainShopForBoolean){
//                emptyMap["searchCriteria[filter_groups][0][filters][" + countFrom1 + "][field]"] = "gender"
//                emptyMap["searchCriteria[filter_groups][0][filters][" + countFrom1 + "][value]"] = genderIds
//                emptyMap["searchCriteria[filter_groups][0][filters][" + countFrom1 + "][condition_type]"] = "in"
//            }
//            Log.e("TAG", "countFromDDD "+emptyMap.toString())

            countFrom1 += 1
            emptyMap["searchCriteria[filter_groups][0][filters][" + countFrom1 + "][field]"] =
                "visibility"
            emptyMap["searchCriteria[filter_groups][0][filters][" + countFrom1 + "][value]"] = "4"
            emptyMap["searchCriteria[filter_groups][0][filters][" + countFrom1 + "][condition_type]"] =
                "eq"
            Log.e("TAG", "countFromDDD " + emptyMap.toString())


            emptyMap["searchCriteria[sortOrders][0][field]"] = "created_at"
            emptyMap["searchCriteria[sortOrders][0][direction]"] = "DESC"

//            emptyMap["searchCriteria[sortOrders][0][field]"] = "price"
//            emptyMap["searchCriteria[sortOrders][0][direction]"] = "DESC"


//            when(viewModel.sortFilter){
//                  0 -> {
//
//                  }
//                  1 -> {
//
//                  }
//                  2 -> {
//                      countFrom2 += 1
//                      emptyMap["searchCriteria[sortOrders][" + countFrom2 + "][field]"] = "price"
//                      emptyMap["searchCriteria[sortOrders][" + countFrom2 + "][direction]"] = "ASC"
//                  }
//                  3 -> {
//                      countFrom2 += 1
//                      emptyMap["searchCriteria[sortOrders][" + countFrom2 + "][field]"] = "price"
//                      emptyMap["searchCriteria[sortOrders][" + countFrom2 + "][direction]"] = "DESC"
//                  }
//            }
//            Log.e("TAG", "countFrom2EEE "+countFrom2)


            Log.e("TAG", "count ${count}")
            textFilter.text = if (count == 0) "Filter" else "Filter ($count)"





            when (viewModel.sortFilter) {
                0 -> {
                    Log.e("TAG", "AAAAAAAAAAA")
                }

                1 -> {
                    Log.e("TAG", "BBBBBBBBBBB")
                    emptyMap["searchCriteria[sortOrders][0][field]"] = "created_at"
                    emptyMap["searchCriteria[sortOrders][0][direction]"] = "DESC"
                }

                2 -> {
                    Log.e("TAG", "CCCCCCCCCCCC")
                    emptyMap["searchCriteria[sortOrders][0][field]"] = "price"
                    emptyMap["searchCriteria[sortOrders][0][direction]"] = "ASC"
                }

                3 -> {
                    Log.e("TAG", "DDDDDDDDDDDD")
                    emptyMap["searchCriteria[sortOrders][0][field]"] = "price"
                    emptyMap["searchCriteria[sortOrders][0][direction]"] = "DESC"
                }
            }

            emptyMap["searchCriteria[currentPage]"] = "" + page
            emptyMap["searchCriteria[pageSize]"] = "10"

            readData(ADMIN_TOKEN) { token ->
                viewModel.getProducts(token.toString(), requireView(), emptyMap)
//                {
//                    if(this.items.size == 0){
//                        binding.idDataNotFound.root.visibility = View.VISIBLE
//                    }else{
//                        binding.idDataNotFound.root.visibility = View.GONE
//                    }
//                }
            }

//            cartItemLiveData.observe(viewLifecycleOwner) {
//
//            }


//            readData(ADMIN_TOKEN) { token ->
//                viewModel.getProducts(token.toString(), requireView(), emptyMap) {
////                    Log.e("TAG", "itAAA " + this)
//                    val items = this.items
//                    when(viewModel.sortFilter){
//                        0 -> {
//                            Log.e("TAG", "AAAAAAAAAAA")
//                        }
//                        1 -> {
//                            Log.e("TAG", "BBBBBBBBBBB")
//                        }
//                        2 -> {
//                            Log.e("TAG", "CCCCCCCCCCCC")
////                            emptyMap["searchCriteria[sortOrders][0][field]"] = "price"
////                            emptyMap["searchCriteria[sortOrders][0][direction]"] = "ASC"
//                            Collections.sort(items, SortByPriceAsc())
//                        }
//                        3 -> {
//                            Log.e("TAG", "DDDDDDDDDDDD")
////                            emptyMap["searchCriteria[sortOrders][0][field]"] = "price"
////                            emptyMap["searchCriteria[sortOrders][0][direction]"] = "DESC"
//                            Collections.sort(items, SortByPriceDesc())
//                        }
//                    }
//
//
//                    adapter2.submitData(items)
//                    adapter2.notifyDataSetChanged()
//
//                    if(this.items.size == 0){
//                        binding.idDataNotFound.root.visibility = View.VISIBLE
//                    }else{
//                        binding.idDataNotFound.root.visibility = View.GONE
//                    }
//                }
//            }

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
//        MainActivity.mainActivity.get()!!.callBack(true)
    }

}
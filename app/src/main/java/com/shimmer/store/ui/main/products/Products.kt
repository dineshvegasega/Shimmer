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
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.typefaceroboto_light
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.typefaceroboto_medium
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

    @SuppressLint("NotifyDataSetChanged", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isBackStack = true
        hideValueOff = 1
        MainActivity.mainActivity.get()!!.callBack(2)

        adapter2 = ProductsAdapter()
        binding.rvList2.adapter = adapter2

        binding.apply {
//            button.setOnClickListener {
//                findNavController().navigate(R.id.action_products_to_productsDetail)
//            }


//           val leftMaxima = requireArguments().serializable("filters") as HashMap<String, Any?>?
//
//            Log.e("TAG", "leftMaxima "+leftMaxima?.get("category"))


//            mainCategory.forEach {
//                Log.e("TAG", "itmainCategory "+it.isSelected)
//            }


            topBar.apply {
                textViewTitle.visibility = View.GONE
//                cardSearch.visibility = View.GONE

                appicon.setImageDrawable(
                    ContextCompat.getDrawable(
                        MainActivity.context.get()!!,
                        R.drawable.baseline_west_24
                    )
                )

                appicon.singleClick {
                    findNavController().navigateUp()
                }

                ivSearch.singleClick {
                    findNavController().navigate(R.id.action_products_to_search)
                }

                ivCart.singleClick {
                    findNavController().navigate(R.id.action_products_to_cart)
                }

                badgeCount.observe(viewLifecycleOwner) { badgeCount ->
                    viewModel.getCartCount() {
                        Log.e("TAG", "countAA: $this")
                        menuBadge.text = "${this}"
                        menuBadge.visibility = if (this != 0) View.VISIBLE else View.GONE
                    }
                }


            }

            var sortFilter = 0
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

                    when (sortFilter) {
                        1 -> {
                            textDefaultSort.setTypeface(typefaceroboto_medium)
                            textPriceLowToHighSort.setTypeface(typefaceroboto_light)
                            textPriceHighToLowSort.setTypeface(typefaceroboto_light)
                        }

                        2 -> {
                            textDefaultSort.setTypeface(typefaceroboto_light)
                            textPriceLowToHighSort.setTypeface(typefaceroboto_medium)
                            textPriceHighToLowSort.setTypeface(typefaceroboto_light)
                        }

                        3 -> {
                            textDefaultSort.setTypeface(typefaceroboto_light)
                            textPriceLowToHighSort.setTypeface(typefaceroboto_light)
                            textPriceHighToLowSort.setTypeface(typefaceroboto_medium)
                        }
                    }

                    textDefaultSort.singleClick {
                        // textDefaultSort.setTextColor(ContextCompat.getColor(binding.root.context, R.color.white))
                        textDefaultSort.setTypeface(typefaceroboto_medium)
                        textPriceLowToHighSort.setTypeface(typefaceroboto_light)
                        textPriceHighToLowSort.setTypeface(typefaceroboto_light)
                        sortFilter = 1
//                        Handler(Looper.getMainLooper()).postDelayed({
//                            dialog.dismiss()
//                        }, 700)
                    }

                    textPriceLowToHighSort.singleClick {
                        textDefaultSort.setTypeface(typefaceroboto_light)
                        textPriceLowToHighSort.setTypeface(typefaceroboto_medium)
                        textPriceHighToLowSort.setTypeface(typefaceroboto_light)
                        sortFilter = 2
//                        Handler(Looper.getMainLooper()).postDelayed({
//                            dialog.dismiss()
//                        }, 700)
                    }

                    textPriceHighToLowSort.singleClick {
                        textDefaultSort.setTypeface(typefaceroboto_light)
                        textPriceLowToHighSort.setTypeface(typefaceroboto_light)
                        textPriceHighToLowSort.setTypeface(typefaceroboto_medium)
                        sortFilter = 3
//                        Handler(Looper.getMainLooper()).postDelayed({
//                            dialog.dismiss()
//                        }, 700)
                    }

                    btClear.singleClick {
                        textDefaultSort.setTypeface(typefaceroboto_light)
                        textPriceLowToHighSort.setTypeface(typefaceroboto_light)
                        textPriceHighToLowSort.setTypeface(typefaceroboto_light)
                        sortFilter = 0
                    }

                    btApply.singleClick {
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

            val emptyMap = mutableMapOf<String, String>()
            var ids : String = ""
            var count = 0

            var countFrom = 0
            var mainCategoryBoolean = false
            mainCategory.forEach {
                if (it.isSelected) {
                    ids += ""+it.id+","
                    mainCategoryBoolean = true
                }

                it.subCategory.forEach { sub ->
                    if (sub.isSelected && sub.isAll == false) {
                        Log.e("TAG", "it.isSelected ${sub.isSelected} ids:${sub.id}")
                        count += 1
                        ids += ""+sub.id+","
                    }
                }
            }
            if (mainCategoryBoolean){
                emptyMap["searchCriteria[filter_groups][0][filters][" + countFrom + "][field]"] = "category_id"
                emptyMap["searchCriteria[filter_groups][0][filters][" + countFrom + "][value]"] = ids
                emptyMap["searchCriteria[filter_groups][0][filters][" + countFrom + "][condition_type]"] = "in"
            }



            var mainPriceBoolean = false
            mainPrice.forEach {
                if (it.isSelected) {
                    count += 1
                    countFrom += 1
                    mainPriceBoolean = true
                }
            }
            if (mainPriceBoolean){
                emptyMap["searchCriteria[filter_groups][0][filters][" + countFrom + "][field]"] = "category_id"
                emptyMap["searchCriteria[filter_groups][0][filters][" + countFrom + "][value]"] = "categoryFrom2"
                emptyMap["searchCriteria[filter_groups][0][filters][" + countFrom + "][condition_type]"] = "in"
            }



            var mainMaterialBoolean = false
            mainMaterial.forEach {
                if (it.isSelected) {
                    count += 1
                    countFrom += 1
                    mainMaterialBoolean = true
                }
            }
            if (mainMaterialBoolean){
                emptyMap["searchCriteria[filter_groups][0][filters][" + countFrom + "][field]"] = "category_id"
                emptyMap["searchCriteria[filter_groups][0][filters][" + countFrom + "][value]"] = "categoryFrom3"
                emptyMap["searchCriteria[filter_groups][0][filters][" + countFrom + "][condition_type]"] = "in"
            }



            var mainShopForBoolean = false
            mainShopFor.forEach {
                if (it.isSelected) {
                    count += 1
                    countFrom += 1
                    mainShopForBoolean = true
                }
            }
            if (mainShopForBoolean){
                emptyMap["searchCriteria[filter_groups][0][filters][" + countFrom + "][field]"] = "gender"
                emptyMap["searchCriteria[filter_groups][0][filters][" + countFrom + "][value]"] = "categoryFrom4"
                emptyMap["searchCriteria[filter_groups][0][filters][" + countFrom + "][condition_type]"] = "in"
            }



            Log.e("TAG", "count ${count}")
            textFilter.text = if (count == 0) "Filter" else "Filter ($count)"




            binding.rvList2.adapter = adapter2
            readData(ADMIN_TOKEN) { token ->
                viewModel.getProducts(token.toString(), view, emptyMap) {
                    Log.e("TAG", "itAAA " + this)
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
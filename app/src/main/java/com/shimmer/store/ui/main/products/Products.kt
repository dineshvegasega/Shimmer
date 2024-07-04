package com.shimmer.store.ui.main.products

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.shimmer.store.ui.mainActivity.MainActivity
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.hideValueOff
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.isBackStack
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.typefaceroboto_light
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.typefaceroboto_medium
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.badgeCount
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.mainCategory
import com.shimmer.store.utils.serializable
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

        binding.apply {
//            button.setOnClickListener {
//                findNavController().navigate(R.id.action_products_to_productsDetail)
//            }


//           val leftMaxima = requireArguments().serializable("filters") as HashMap<String, Any?>?
//
//            Log.e("TAG", "leftMaxima "+leftMaxima?.get("category"))


            mainCategory.forEach {
                Log.e("TAG", "itmainCategory "+it.isSelected)
            }


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


                badgeCount.observe(viewLifecycleOwner) {
                    menuBadge.text = "$it"
                    menuBadge.visibility = if (it != 0) View.VISIBLE else View.GONE
                }
            }

//            val typefaceroboto_light = resources.getFont(R.font.roboto_light)
//            val typefaceroboto_medium = resources.getFont(R.font.roboto_medium)


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

                    when(sortFilter){
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




            adapter2.submitData(viewModel.item1)
            adapter2.notifyDataSetChanged()
            binding.rvList2.adapter = adapter2
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
//        MainActivity.mainActivity.get()!!.callBack(true)
    }

}
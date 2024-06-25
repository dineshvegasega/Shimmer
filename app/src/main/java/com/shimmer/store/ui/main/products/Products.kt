package com.shimmer.store.ui.main.products

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.shimmer.store.R
import com.shimmer.store.databinding.DialogSortBinding
import com.shimmer.store.databinding.ProductsBinding
import com.shimmer.store.ui.main.home.Home
import com.shimmer.store.ui.main.home.Home.Companion
import com.shimmer.store.ui.main.home.Home.Companion.adapter2
import com.shimmer.store.ui.main.home.ListAdapter1
import com.shimmer.store.ui.main.home.ListAdapter2
import com.shimmer.store.ui.main.home.ListAdapter3
import com.shimmer.store.ui.mainActivity.MainActivity
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.hideValueOff
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.isBackStack
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Products : Fragment() {
    private val viewModel: ProductsVM by viewModels()
    private var _binding: ProductsBinding? = null
    private val binding get() = _binding!!

    var sortAlert : BottomSheetDialog?= null


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

            layoutSort.setOnClickListener {
                if(sortAlert?.isShowing == true) {
                    return@setOnClickListener
                }
                val dialogBinding = DialogSortBinding.inflate(root.context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
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

            }

            layoutFilter.setOnClickListener {
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
package com.shimmer.store.ui.main.productDetail

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OVER_SCROLL_NEVER
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator
import com.shimmer.store.R
import com.shimmer.store.databinding.DialogPdfBinding
import com.shimmer.store.databinding.DialogSizesBinding
import com.shimmer.store.databinding.ProductDetailBinding
import com.shimmer.store.datastore.db.CartModel
import com.shimmer.store.models.ItemParcelable
import com.shimmer.store.models.Items
import com.shimmer.store.ui.interfaces.CallBackListener
import com.shimmer.store.ui.main.products.Products
import com.shimmer.store.ui.main.products.Products.Companion
import com.shimmer.store.ui.main.products.ProductsAdapter
import com.shimmer.store.ui.mainActivity.MainActivity
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.db
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.hideValueOff
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.isBackStack
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.badgeCount
import com.shimmer.store.utils.getRecyclerView
import com.shimmer.store.utils.ioThread
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetail : Fragment() , CallBackListener {
    private val viewModel: ProductDetailVM by viewModels()
    private var _binding: ProductDetailBinding? = null
    private val binding get() = _binding!!

    companion object {

        @JvmStatic
        lateinit var pagerAdapter: FragmentStateAdapter

        var callBackListener: CallBackListener? = null


        @JvmStatic
        lateinit var adapter2: RelatedProductAdapter


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProductDetailBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged", "ClickableViewAccessibility", "SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callBackListener = this
        isBackStack = true
        hideValueOff = 2
        MainActivity.mainActivity.get()!!.callBack(2)

        binding.apply {

            pagerAdapter = ProductDetailPagerAdapter(requireActivity(), viewModel.item1)
            rvList1.offscreenPageLimit = 1
            rvList1.overScrollMode = OVER_SCROLL_NEVER
            rvList1.adapter = pagerAdapter
            rvList1.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            Log.e("TAG", "videoList "+viewModel.item1.size)
            (rvList1.getRecyclerView()
                .getItemAnimator() as SimpleItemAnimator).supportsChangeAnimations =
                false

//            TabLayoutMediator(tabLayout, rvList1) { tab, position ->
//            }.attach()

            viewModel.indicator(binding, viewModel.item1, 1)

            rvList1.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
//                    if (pageChangeValue != position) {
//                        Log.e("TAG", "positionA" + position)
//                    }
//                    pageChangeValue = position
                }

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
//                    adapter1.updatePosition(position)
                    viewModel.indicator(binding, viewModel.item1, position)
                }

                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    Log.e("TAG", "state" + state)
//                    if (state == 0) {
//                    adapter1.notifyItemChanged(adapter1.counter)
//                        onClickItem(pageChangeValue)
//                    }
                }
            })

            viewModel.colors(binding, 1)

            ivPink.singleClick {
                viewModel.colors(binding, 1)
            }
            ivSilver.singleClick {
                viewModel.colors(binding, 2)
            }
            ivGold.singleClick {
                viewModel.colors(binding, 3)
            }

            btRingSize.singleClick {
                openDialogSize()
            }

            btGuide.singleClick {
                viewModel.openDialogPdf(1, "quote.pdf")
            }

            rvList2.setHasFixedSize(true)
            rvList2.adapter = viewModel.recentAdapter
            viewModel.recentAdapter.notifyDataSetChanged()
            viewModel.recentAdapter.submitList(viewModel.item3)





            bt14.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color._000000))
            bt14.setTextColor(ContextCompat.getColor(requireContext(), R.color._ffffff))
            bt18.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color._a5a8ab))
            bt18.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))

            bt14.singleClick {
                bt14.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color._000000))
                bt14.setTextColor(ContextCompat.getColor(requireContext(), R.color._ffffff))
                bt18.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color._a5a8ab))
                bt18.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            }
            bt18.singleClick {
                bt14.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color._a5a8ab))
                bt14.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                bt18.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color._000000))
                bt18.setTextColor(ContextCompat.getColor(requireContext(), R.color._ffffff))
            }


            topBar.apply {
                textViewTitle.visibility = View.VISIBLE
//                cardSearch.visibility = View.VISIBLE
                ivSearch.visibility = View.GONE
                ivCart.visibility = View.VISIBLE
                textViewTitle.text = "Product Detail"

                appicon.setImageDrawable(
                    ContextCompat.getDrawable(
                        MainActivity.context.get()!!,
                        R.drawable.baseline_west_24
                    )
                )

                appicon.singleClick {
                    findNavController().navigateUp()
                }

                ivCart.singleClick {
                    findNavController().navigate(R.id.action_productDetail_to_cart)
                }


                badgeCount.observe(viewLifecycleOwner) {
                    menuBadge.text = "$it"
                    menuBadge.visibility = if (it != 0) View.VISIBLE else View.GONE
                }
            }

            btAddToCart.singleClick {
                badgeCount.value = 1
                ioThread {
                    Log.e("TAG", "onViewCreated: ReaDY")

                    val newUser = CartModel(product_id = 1, name = "test")
//                    Log.e("TAG", "onViewCreated: ReaDY2")

//                    if(model.isSelected == true){
//                        db?.cartDao()?.insertAll(newUser)
//                    } else {
                        db?.cartDao()?.delete(CartModel())
//                    }
//                    Log.e("TAG", "onViewCreated: ReaDY3")

                    val userList: List<CartModel> ?= db?.cartDao()?.getAll()
                    userList?.forEach {
                        Log.e("TAG", "onViewCreated: "+it.name + " it.currentTime "+it.currentTime)
                    }
                }

            }

            btByNow.singleClick {
                findNavController().navigate(R.id.action_productDetail_to_cart)
            }

            adapter2 = RelatedProductAdapter()
            adapter2.submitData(viewModel.item1)
            adapter2.notifyDataSetChanged()
            binding.rvRelatedProducts.adapter = adapter2

        }



    }

    override fun onCallBack(pos: Int) {
        findNavController().navigate(R.id.action_productDetail_to_productZoom, Bundle().apply {
            putParcelable("arrayList", ItemParcelable(viewModel.item1, binding.rvList1.currentItem))
        })
    }

    override fun onCallBackHideShow() {
        TODO("Not yet implemented")
    }



    @SuppressLint("NotifyDataSetChanged")
    fun openDialogSize() {
        val dialogBinding = DialogSizesBinding.inflate(MainActivity.activity.get()?.getSystemService(
            Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        )
        val dialog = BottomSheetDialog(MainActivity.context.get()!!)
        dialog.setContentView(dialogBinding.root)
        dialog.setOnShowListener { dia ->
            val bottomSheetDialog = dia as BottomSheetDialog
            val bottomSheetInternal: FrameLayout =
                bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)!!
            bottomSheetInternal.setBackgroundResource(R.drawable.bg_top_round_corner)
        }
        dialog.show()


        dialogBinding.rvListSize.setHasFixedSize(true)
        dialogBinding.rvListSize.adapter = viewModel.sizeAdapter
        viewModel.sizeAdapter.notifyDataSetChanged()
        viewModel.sizeAdapter.submitList(viewModel.arraySizes)


        dialogBinding.ivIconCross.singleClick {
            dialog.dismiss()
        }

    }

}
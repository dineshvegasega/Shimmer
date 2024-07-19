package com.shimmer.store.ui.main.category

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OVER_SCROLL_NEVER
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.shimmer.store.R
import com.shimmer.store.databinding.CategoryBinding
import com.shimmer.store.datastore.db.CartModel
import com.shimmer.store.ui.main.productDetail.ProductDetail.Companion.pagerAdapter
import com.shimmer.store.ui.main.productDetail.ProductDetailPagerAdapter
import com.shimmer.store.ui.mainActivity.MainActivity
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.db
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.hideValueOff
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.isBackStack
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.badgeCount
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.mainShopFor
import com.shimmer.store.utils.ioThread
import com.shimmer.store.utils.mainThread
import com.shimmer.store.utils.singleClick
import com.shimmer.store.utils.updatePagerHeightForChild
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class Category : Fragment() {
    private val viewModel: CategoryVM by viewModels()
    private var _binding: CategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CategoryBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isBackStack = false
        hideValueOff = 2
        MainActivity.mainActivity.get()!!.callBack(1)
        binding.apply {
//            button.setOnClickListener {
//                findNavController().navigate(R.id.action_category_to_products)
//            }


            layoutCustomDesign.ivCustomDesign.setOnClickListener {
                findNavController().navigate(R.id.action_category_to_customDesign)
            }


            topBar.apply {
                textViewTitle.visibility = View.VISIBLE
                ivSearch.visibility = View.VISIBLE
                ivCart.visibility = View.VISIBLE

                ivSearch.singleClick {
                    findNavController().navigate(R.id.action_category_to_search)
                }

                ivCart.singleClick {
                    findNavController().navigate(R.id.action_category_to_cart)
                }


                badgeCount.observe(viewLifecycleOwner) {
                    viewModel.getCartCount() {
                        Log.e("TAG", "count: $this")
                        menuBadge.text = "${this}"
                        menuBadge.visibility = if (this != 0) View.VISIBLE else View.GONE
                    }
                }

                viewModel.show()
                mainThread {
                    val pagerAdapter = CategoryPagerAdapter(requireActivity(), mainShopFor)
//                    pagerAdapter.notifyDataSetChanged()
                    rvList1.offscreenPageLimit = 3
                    rvList1.overScrollMode = OVER_SCROLL_NEVER

                    rvList1.adapter = pagerAdapter
                    delay(200)

                    rvList1.setPageTransformer { page, position ->
                        rvList1.updatePagerHeightForChild(page)
                    }
                    TabLayoutMediator(tabLayout, rvList1) { tab, position ->
                        tab.text = mainShopFor[position].name
                    }.attach()
                    viewModel.hide()
                }


//                    rvList1.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//                        override fun onPageScrolled(
//                            position: Int,
//                            positionOffset: Float,
//                            positionOffsetPixels: Int
//                        ) {
//                            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
////                    if (pageChangeValue != position) {
////                        Log.e("TAG", "positionA" + position)
////                    }
////                    pageChangeValue = position
//                        }
//
//                        override fun onPageSelected(position: Int) {
//                            super.onPageSelected(position)
////                    adapter1.updatePosition(position)
////                            viewModel.indicator(binding, viewModel.item1, position)
////                            pagerAdapter.notifyDataSetChanged()
//                        }
//
//                        override fun onPageScrollStateChanged(state: Int) {
//                            super.onPageScrollStateChanged(state)
//                            Log.e("TAG", "state" + state)
////                    if (state == 0) {
////                    adapter1.notifyItemChanged(adapter1.counter)
////                        onClickItem(pageChangeValue)
////                    }
//                        }
//                    })
            }
        }

    }
}
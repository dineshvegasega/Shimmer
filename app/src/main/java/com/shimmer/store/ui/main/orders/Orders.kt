package com.shimmer.store.ui.main.orders

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OVER_SCROLL_NEVER
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.shimmer.store.R
import com.shimmer.store.databinding.OrdersBinding
import com.shimmer.store.ui.main.category.CategoryPagerAdapter
import com.shimmer.store.ui.mainActivity.MainActivity
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.hideValueOff
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.isBackStack
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.cartItemCount
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.cartItemLiveData
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.mainShopFor
import com.shimmer.store.utils.mainThread
import com.shimmer.store.utils.singleClick
import com.shimmer.store.utils.updatePagerHeightForChild
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class Orders : Fragment() {
    private val viewModel: OrdersVM by viewModels()
    private var _binding: OrdersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = OrdersBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isBackStack = false
        hideValueOff = 2
        MainActivity.mainActivity.get()!!.callBack(2)
        MainActivity.mainActivity.get()!!.callCartApi()

        binding.apply {

            topBarBack.includeBackButton.apply {
                layoutBack.singleClick {
                    findNavController().navigateUp()
                }

                topBarBack.ivCart.singleClick {
                    findNavController().navigate(R.id.action_orders_to_cart)
                }
            }


            cartItemLiveData.value = false
            cartItemLiveData.observe(viewLifecycleOwner) {
                topBarBack.menuBadge.text = "$cartItemCount"
                topBarBack.menuBadge.visibility = if (cartItemCount != 0) View.VISIBLE else View.GONE
            }

//            topBar.apply {
//                textViewTitle.visibility = View.VISIBLE
//                ivSearch.visibility = View.VISIBLE
//                ivCart.visibility = View.VISIBLE
//                textViewTitle.text = "Orders"
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
//
//                ivSearch.singleClick {
//                    findNavController().navigate(R.id.action_orders_to_search)
//                }
//
//                ivCart.singleClick {
//                    findNavController().navigate(R.id.action_orders_to_cart)
//                }
//
//                badgeCount.observe(viewLifecycleOwner) {
//                    viewModel.getCartCount(){
//                        Log.e("TAG", "count: $this")
//                        menuBadge.text = "${this}"
//                        menuBadge.visibility = if (this != 0) View.VISIBLE else View.GONE
//                    }
////                    mainThread {
////                        val userList: List<CartModel> ?= db?.cartDao()?.getAll()
////                        var countBadge = 0
////                        userList?.forEach {
////                            countBadge += it.quantity
////                        }
////                        menuBadge.text = "${countBadge}"
////                        menuBadge.visibility = if (countBadge != 0) View.VISIBLE else View.GONE
////                    }
//                }
//            }

            viewModel.show()
            mainThread {
                val pagerAdapter = OrdersPagerAdapter(requireActivity(), viewModel.ordersTypesArray)
//                    pagerAdapter.notifyDataSetChanged()
                rvList1.offscreenPageLimit = 3
                rvList1.overScrollMode = OVER_SCROLL_NEVER

                rvList1.adapter = pagerAdapter
                delay(200)

                rvList1.setPageTransformer { page, position ->
                    rvList1.updatePagerHeightForChild(page)
                }
//                TabLayoutMediator(tabLayout, rvList1) { tab, position ->
//                    tab.text = viewModel.ordersTypesArray[position].name
//                }.attach()
                viewModel.hide()

                positionSelectFun()


                layoutCustomerOrders.singleClick {
                    rvList1.currentItem = 0
                }

                layoutOrderHistory.singleClick {
                    rvList1.currentItem = 1
                }

                rvList1.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageScrolled(
                        position: Int,
                        positionOffset: Float,
                        positionOffsetPixels: Int
                    ) {
                        super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                    }


                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        Log.e("TAG", "onPageSelectedAA: $position")
                        viewModel.positionSelect = position
                        positionSelectFun()
                    }


                    override fun onPageScrollStateChanged(state: Int) {
                        super.onPageScrollStateChanged(state)
                        Log.e("TAG", "onPageScrollStateChangedAA: $state")
                    }

                })
            }
        }
    }



    private fun positionSelectFun() {
        binding.apply {
            if(viewModel.positionSelect == 0){
                layoutCustomerOrders.background =  ContextCompat.getDrawable(binding.root.context, R.drawable.tab_round_light_blue)
                layoutOrderHistory.background =  ContextCompat.getDrawable(binding.root.context, R.drawable.tab_round_light_white)

                textCustomerOrders.setTextColor(ContextCompat.getColorStateList(binding.root.context,R.color.white))
                textOrderHistory.setTextColor(ContextCompat.getColorStateList(binding.root.context,R.color.black))
            } else if(viewModel.positionSelect == 1){
                layoutCustomerOrders.background =  ContextCompat.getDrawable(binding.root.context, R.drawable.tab_round_light_white)
                layoutOrderHistory.background =  ContextCompat.getDrawable(binding.root.context, R.drawable.tab_round_light_blue)

                textCustomerOrders.setTextColor(ContextCompat.getColorStateList(binding.root.context,R.color.black))
                textOrderHistory.setTextColor(ContextCompat.getColorStateList(binding.root.context,R.color.white))
            }
        }
    }
}
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
import com.shimmer.store.datastore.DataStoreKeys.ADMIN_TOKEN
import com.shimmer.store.datastore.DataStoreUtil.readData
import com.shimmer.store.datastore.db.CartModel
import com.shimmer.store.models.ItemParcelable
import com.shimmer.store.models.Items
import com.shimmer.store.models.products.MediaGalleryEntry
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
//import com.shimmer.store.utils.getRecyclerView
import com.shimmer.store.utils.ioThread
import com.shimmer.store.utils.mainThread
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

        var images : ArrayList<MediaGalleryEntry> = ArrayList()
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

            var skuId = arguments?.getString("sku")

            readData(ADMIN_TOKEN) { token ->
                viewModel.getProductDetail(token.toString(), view, skuId!!) {
                    Log.e("TAG", "getProductDetailAA " + this.id)

                    images =  this.media_gallery_entries

                    pagerAdapter = ProductDetailPagerAdapter(requireActivity(), images)
                    rvList1.offscreenPageLimit = 1
                    rvList1.overScrollMode = OVER_SCROLL_NEVER
                    rvList1.adapter = pagerAdapter
                    rvList1.orientation = ViewPager2.ORIENTATION_HORIZONTAL
                    Log.e("TAG", "videoList "+viewModel.item1.size)
                    (rvList1.getRecyclerView()
                        .getItemAnimator() as SimpleItemAnimator).supportsChangeAnimations =
                        false

                    viewModel.indicator(binding, this.media_gallery_entries, 1)

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
                            viewModel.indicator(binding, images, position)
                        }

                        override fun onPageScrollStateChanged(state: Int) {
                            super.onPageScrollStateChanged(state)
                            Log.e("TAG", "state" + state)
                        }
                    })


                    textTitle.text = this.name
                    textPrice.text = "₹ "+this.price
                    textSKU.text = "SKU: "+this.sku


                    this.custom_attributes.forEach {
                        if (it.attribute_code == "size"){
                            btRingSize.text = ""+it.value
                        }

                        if (it.attribute_code == "metal_type"){
                            if (it.value == "12"){
                                this.custom_attributes.forEach { againAttributes ->
                                    if (againAttributes.attribute_code == "metal_purity"){
                                        if (againAttributes.value == "14"){
                                            bt14.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color._000000))
                                            bt14.setTextColor(ContextCompat.getColor(requireContext(), R.color._ffffff))
                                            bt18.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color._a5a8ab))
                                            bt18.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                                        }

                                        if (againAttributes.value == "18"){
                                            bt14.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color._a5a8ab))
                                            bt14.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                                            bt18.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color._000000))
                                            bt18.setTextColor(ContextCompat.getColor(requireContext(), R.color._ffffff))
                                        }
                                    }
                                }
                            }
                        }
                    }



                    val newUser = CartModel(product_id = this.id, name = this.name, price = this.price, quantity = 1, currentTime = System.currentTimeMillis())
                    btAddToCart.singleClick {
                        ioThread {
                            db?.cartDao()?.insertAll(newUser)
                            badgeCount.value = true
                        }
                    }

                    btAddToCart.singleClick {
                        ioThread {
                            db?.cartDao()?.insertAll(newUser)
                            badgeCount.value = true
                            findNavController().navigate(R.id.action_productDetail_to_cart)
                        }
                    }
                }
            }


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
                    viewModel.getCartCount(){
                        Log.e("TAG", "count: $this")
                        menuBadge.text = "${this}"
                        menuBadge.visibility = if (this != 0) View.VISIBLE else View.GONE
                    }
                }
            }


            adapter2 = RelatedProductAdapter()
            adapter2.submitData(viewModel.item1)
            adapter2.notifyDataSetChanged()
            binding.rvRelatedProducts.adapter = adapter2

        }



    }

    override fun onCallBack(pos: Int) {
        Log.e("TAG", "onCallBack: ${images.toString()}")
        findNavController().navigate(R.id.action_productDetail_to_productZoom, Bundle().apply {
            putParcelable("arrayList", ItemParcelable(images, binding.rvList1.currentItem))
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
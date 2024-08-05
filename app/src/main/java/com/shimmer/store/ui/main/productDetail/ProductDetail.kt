package com.shimmer.store.ui.main.productDetail

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OVER_SCROLL_NEVER
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.shimmer.store.R
import com.shimmer.store.databinding.DialogSizesBinding
import com.shimmer.store.databinding.ProductDetailBinding
import com.shimmer.store.datastore.DataStoreKeys.ADMIN_TOKEN
import com.shimmer.store.datastore.DataStoreUtil.readData
import com.shimmer.store.models.ItemParcelable
import com.shimmer.store.models.ItemProductOptions
import com.shimmer.store.models.products.ItemProduct
import com.shimmer.store.models.products.MediaGalleryEntry
import com.shimmer.store.models.products.Value
import com.shimmer.store.ui.interfaces.CallBackListener
import com.shimmer.store.ui.mainActivity.MainActivity
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.hideValueOff
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.isBackStack
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.badgeCount
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.isApiCall
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.mainCategory
import com.shimmer.store.utils.getPatternFormat
import com.shimmer.store.utils.getRecyclerView
import com.shimmer.store.utils.mainThread
//import com.shimmer.store.utils.getRecyclerView
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class ProductDetail : Fragment(), CallBackListener {
    private val viewModel: ProductDetailVM by viewModels()
    private var _binding: ProductDetailBinding? = null
    private val binding get() = _binding!!

    companion object {

        @JvmStatic
        lateinit var pagerAdapter: FragmentStateAdapter

        var callBackListener: CallBackListener? = null


        @JvmStatic
        lateinit var adapter2: RelatedProductAdapter

        var images: ArrayList<MediaGalleryEntry> = ArrayList()
    }


    var currentSKU: String = ""
    var currentColor: String = ""

//    var arrayColors = ArrayList<ItemColor>()
//    var arrayPurity = ArrayList<ItemColor>()


    var arrayAllProduct: ArrayList<ItemProduct> = ArrayList()
    lateinit var currentProduct: ItemProduct

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProductDetailBinding.inflate(inflater)
        return binding.root
    }

    @RequiresApi(35)
    @SuppressLint("NotifyDataSetChanged", "ClickableViewAccessibility", "SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callBackListener = this
        isBackStack = true
        hideValueOff = 2
        MainActivity.mainActivity.get()!!.callBack(2)


        binding.apply {
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
                    viewModel.getCartCount() {
                        Log.e("TAG", "count: $this")
                        menuBadge.text = "${this}"
                        menuBadge.visibility = if (this != 0) View.VISIBLE else View.GONE
                    }
                }
            }


//            val model = arguments?.parcelable<ItemProduct>("model")
//            val model = arguments?.getString("model")

//            if (isApiCall == false){
//                isApiCall = true
            callApiDetails2("SRI0006")
//            }

            Log.e("TAG", "isApiCall " + isApiCall)

            btRingSize.singleClick {
                openDialogSize()
            }

        }


    }

    override fun onCallBack(pos: Int) {
        Log.e("TAG", "onCallBack: ${images.toString()}")
        findNavController().navigate(R.id.action_productDetail_to_productZoom, Bundle().apply {
            putParcelable("arrayList", ItemParcelable(images, binding.rvList1.currentItem))
        })
    }

    override fun onCallBackHideShow() {
    }


    @SuppressLint("NotifyDataSetChanged")
    fun openDialogSize() {
        val dialogBinding = DialogSizesBinding.inflate(
            MainActivity.activity.get()?.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE
            ) as LayoutInflater
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
        viewModel.sizeAdapter.submitList(ArrayList(viewModel.arrayItemProductOptionsSize))



        dialogBinding.ivIconCross.singleClick {
            dialog.dismiss()
        }


        viewModel.sizeMutableListClose.value = false
        viewModel.sizeMutableListClose.observe(viewLifecycleOwner) {
            if (it) {
                mainThread {
                    dialog.dismiss()
                }
            }
        }
    }

//
//    fun callApiDetails(skuId: String) {
//
//        binding.apply {
//            mainThread {
//                readData(ADMIN_TOKEN) { token ->
//
//                    viewModel.allProducts(
//                        token.toString(),
//                        requireView(),
//                        skuId
//                    ) {
//                        arrayAllProduct = this
////                        _allProductArray.forEach {
////                            val _allProduct = it
////                            Log.e("TAG", "arrayColorsSize: " + _allProduct.name)
////                        }
//
//
//                        bt12.visibility = View.GONE
//                        bt13.visibility = View.GONE
//                        bt14.visibility = View.GONE
//                        bt18.visibility = View.GONE
//                        bt95.visibility = View.GONE
//
//                        ivGold.visibility = View.GONE
//                        ivSilver.visibility = View.GONE
//                        ivPink.visibility = View.GONE
//
//
//                        if (arrayAllProduct.size > 0) {
//                            Log.e("TAG", "_allProductArray.size " + arrayAllProduct.size)
//                            currentProduct = arrayAllProduct[0]
//                            setOneDetail(arrayAllProduct[0])
//                        }
//
//
//
//                        arrayAllProduct.forEach { itemProduct ->
//                            itemProduct.custom_attributes.forEach { customAttr ->
//                                if (customAttr.attribute_code == "metal_color") {
//                                    if (customAttr.value == "19") {
//                                        ivGold.visibility = View.VISIBLE
//                                    }
//
//                                    if (customAttr.value == "20") {
//                                        ivSilver.visibility = View.VISIBLE
//                                    }
//
//                                    if (customAttr.value == "21") {
//                                        ivPink.visibility = View.VISIBLE
//                                    }
//                                }
//
//
//
//
//                                if (customAttr.attribute_code == "metal_purity") {
//                                    if (customAttr.value == "14") {
//                                        bt12.visibility = View.VISIBLE
//                                        bt14.visibility = View.VISIBLE
//                                    }
//
//                                    if (customAttr.value == "15") {
//                                        bt12.visibility = View.VISIBLE
//                                        bt18.visibility = View.VISIBLE
//                                    }
//
//                                    if (customAttr.value == "16") {
//                                        bt13.visibility = View.VISIBLE
//                                        bt95.visibility = View.VISIBLE
//                                    }
//                                }
//
//
//
//
//                                if (customAttr.attribute_code == "size") {
//                                    Log.e("TAG", "sizeAAA: ${customAttr.value}")
//                                    viewModel.arraySizes.add(ItemSizes(inch = "" + customAttr.value))
//                                }
//
//                                if (customAttr.attribute_code == "size") {
//                                    textWeight2.text = "Platinum 95"
//                                }
//
//
//                            }
//
//
////                            viewModel.arraySizes.forEach { itemSize->
////                                if (itemSize.inch == it.value) {
////                                    itemSize.apply {
////                                        isSelected = true
////                                    }
////                                }
////                            }
//
//                        }
//
//
////                        Log.e("TAG", "arrayColorsSize: " + arrayColors.size)
////                        Log.e("TAG", "arrayPuritySize: " + arrayPurity.size)
//
////                            arrayColors.forEach {
////                                Handler(Looper.getMainLooper()).postDelayed({
////                                    if (it.color == "19") {
////                                        Log.e("TAG", "itAAA11 " + it.toString())
////                                        binding.layoutColor.visibility = View.VISIBLE
////                                        binding.ivGold.visibility = View.VISIBLE
////                                        //viewModel.colors(binding, 3)
////                                    }
////
////                                    if (it.color == "20") {
////                                        Log.e("TAG", "itAAA22 " + it.toString())
////                                        binding.layoutColor.visibility = View.VISIBLE
////                                        binding.ivSilver.visibility = View.VISIBLE
////                                        //  viewModel.colors(binding, 2)
////                                    }
////                                }, 50)
////                            }
////
////
////                            arrayPurity.forEach {
////                                Handler(Looper.getMainLooper()).postDelayed({
////                                    if (it.purity == "14") {
////                                        Log.e("TAG", "itAAA33 " + it.toString())
////                                        layoutPuritySelect.visibility = View.VISIBLE
////                                        bt14.visibility = View.VISIBLE
////////                                    bt14.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color._000000))
////////                                    bt14.setTextColor(ContextCompat.getColor(requireContext(), R.color._ffffff))
////                                    }
////
////                                    if (it.purity == "15") {
////                                        Log.e("TAG", "itAAA44 " + it.toString())
////                                        layoutPuritySelect.visibility = View.VISIBLE
////                                        bt18.visibility = View.VISIBLE
////////                                    bt18.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color._000000))
////////                                    bt18.setTextColor(ContextCompat.getColor(requireContext(), R.color._ffffff))
////                                    }
////                                }, 50)
////                            }
//
//
////                            arrayPurity.forEach {
//////                                Log.e("TAG", "itPurity "+it)
//////                                if (it.first == "14"){
//////                                    Log.e("TAG", "CCCCCCCCCCCCCCC")
//////                                    layoutPuritySelect.visibility = View.VISIBLE
//////                                    bt14.visibility = View.VISIBLE
//////                                    bt14.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color._000000))
//////                                    bt14.setTextColor(ContextCompat.getColor(requireContext(), R.color._ffffff))
//////                                }
//////
//////                                if (it.first == "15"){
//////                                    Log.e("TAG", "DDDDDDDDDDDDDD")
//////                                    layoutPuritySelect.visibility = View.VISIBLE
//////                                    bt18.visibility = View.VISIBLE
//////                                    bt18.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color._000000))
//////                                    bt18.setTextColor(ContextCompat.getColor(requireContext(), R.color._ffffff))
//////                                }
//////                            }
////                    }
//                    }
//
////
////                    viewModel.getProductDetail(token.toString(), requireView(), skuId!!) {
////                        Log.e("TAG", "getProductDetailAA " + this.id)
////                        currentSKU = this.sku
////                        images = this.media_gallery_entries
////
////                        pagerAdapter = ProductDetailPagerAdapter(requireActivity(), images)
////                        rvList1.offscreenPageLimit = 1
////                        rvList1.overScrollMode = OVER_SCROLL_NEVER
////                        rvList1.adapter = pagerAdapter
////                        rvList1.orientation = ViewPager2.ORIENTATION_HORIZONTAL
////                        Log.e("TAG", "videoList " + viewModel.item1.size)
////                        (rvList1.getRecyclerView()
////                            .getItemAnimator() as SimpleItemAnimator).supportsChangeAnimations =
////                            false
////
////                        viewModel.indicator(binding, this.media_gallery_entries, 1)
////
////                        rvList1.registerOnPageChangeCallback(object :
////                            ViewPager2.OnPageChangeCallback() {
////                            override fun onPageScrolled(
////                                position: Int,
////                                positionOffset: Float,
////                                positionOffsetPixels: Int
////                            ) {
////                                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
////                            }
////
////                            override fun onPageSelected(position: Int) {
////                                super.onPageSelected(position)
////                                viewModel.indicator(binding, images, position)
////                            }
////
////                            override fun onPageScrollStateChanged(state: Int) {
////                                super.onPageScrollStateChanged(state)
////                                Log.e("TAG", "state" + state)
////                            }
////                        })
////
////
////                        textTitle.text = this.name
////                        textPrice.text = "₹ " + this.price
////                        textSKU.text = "SKU: " + this.sku
////
////
////                        this.custom_attributes.forEach {
////                            if (it.attribute_code == "size") {
////                                btRingSize.text = "" + it.value
////                            }
////
////
////
////
////                            if (it.attribute_code == "metal_color") {
////                                layoutColor.visibility = View.GONE
////                                ivPink.visibility = View.GONE
////                                ivSilver.visibility = View.GONE
////                                ivGold.visibility = View.GONE
////
////                                Log.e("TAG", "it.valueAAAAA "+it.value)
////
////                                if ("" + it.value == "18") {
////                                    layoutColor.visibility = View.VISIBLE
////                                    ivPink.visibility = View.VISIBLE
////                                    viewModel.colors(binding, 1)
////                                    currentColor = "" + it.value
////                                }
////                                if ("" + it.value == "20") {
////                                    layoutColor.visibility = View.VISIBLE
////                                    ivSilver.visibility = View.VISIBLE
////                                    viewModel.colors(binding, 2)
////                                    currentColor = "" + it.value
////                                }
////                                if ("" + it.value == "19") {
////                                    layoutColor.visibility = View.VISIBLE
////                                    ivGold.visibility = View.VISIBLE
////                                    viewModel.colors(binding, 3)
////                                    currentColor = "" + it.value
////                                }
////
////                            }
////
////
////
////                            if (it.attribute_code == "metal_type") {
////                                layoutPuritySelect.visibility = View.GONE
////                                bt14.visibility = View.GONE
////                                bt18.visibility = View.GONE
////                                if (it.value == "12") {
////                                    Log.e("TAG", "AAAAAAAAAAAA")
////                                    this.custom_attributes.forEach { againAttributes ->
////                                        if (againAttributes.attribute_code == "metal_purity") {
////                                            if (againAttributes.value == "14") {
////                                                Log.e("TAG", "CCCCCCCCCCCCCCC")
////                                                layoutPuritySelect.visibility = View.VISIBLE
////                                                bt14.visibility = View.VISIBLE
////                                                bt14.backgroundTintList = ColorStateList.valueOf(
////                                                    ContextCompat.getColor(
////                                                        requireContext(),
////                                                        R.color._000000
////                                                    )
////                                                )
////                                                bt14.setTextColor(
////                                                    ContextCompat.getColor(
////                                                        requireContext(),
////                                                        R.color._ffffff
////                                                    )
////                                                )
////                                                bt18.backgroundTintList = ColorStateList.valueOf(
////                                                    ContextCompat.getColor(
////                                                        requireContext(),
////                                                        R.color._a5a8ab
////                                                    )
////                                                )
////                                                bt18.setTextColor(
////                                                    ContextCompat.getColor(
////                                                        requireContext(),
////                                                        R.color.black
////                                                    )
////                                                )
////                                            }
////
////                                            if (againAttributes.value == "15") {
////                                                Log.e("TAG", "DDDDDDDDDDDDDD")
////                                                layoutPuritySelect.visibility = View.VISIBLE
////                                                bt18.visibility = View.VISIBLE
////                                                bt14.backgroundTintList = ColorStateList.valueOf(
////                                                    ContextCompat.getColor(
////                                                        requireContext(),
////                                                        R.color._a5a8ab
////                                                    )
////                                                )
////                                                bt14.setTextColor(
////                                                    ContextCompat.getColor(
////                                                        requireContext(),
////                                                        R.color.black
////                                                    )
////                                                )
////                                                bt18.backgroundTintList = ColorStateList.valueOf(
////                                                    ContextCompat.getColor(
////                                                        requireContext(),
////                                                        R.color._000000
////                                                    )
////                                                )
////                                                bt18.setTextColor(
////                                                    ContextCompat.getColor(
////                                                        requireContext(),
////                                                        R.color._ffffff
////                                                    )
////                                                )
////                                            }
////                                        }
////                                    }
////                                }
////                            } else {
////                                layoutPuritySelect.visibility = View.GONE
////                                bt14.visibility = View.GONE
////                                bt18.visibility = View.GONE
////                                Log.e("TAG", "BBBBBBBBBBB")
////                                this.custom_attributes.forEach { againAttributes ->
////                                    if (againAttributes.attribute_code == "metal_purity") {
////                                        if (againAttributes.value == "14") {
////                                            Log.e("TAG", "EEEEEEEEEEE")
////                                            layoutPuritySelect.visibility = View.VISIBLE
////                                            bt14.visibility = View.VISIBLE
////                                            bt14.backgroundTintList = ColorStateList.valueOf(
////                                                ContextCompat.getColor(
////                                                    requireContext(),
////                                                    R.color._000000
////                                                )
////                                            )
////                                            bt14.setTextColor(
////                                                ContextCompat.getColor(
////                                                    requireContext(),
////                                                    R.color._ffffff
////                                                )
////                                            )
////                                            bt18.backgroundTintList = ColorStateList.valueOf(
////                                                ContextCompat.getColor(
////                                                    requireContext(),
////                                                    R.color._a5a8ab
////                                                )
////                                            )
////                                            bt18.setTextColor(
////                                                ContextCompat.getColor(
////                                                    requireContext(),
////                                                    R.color.black
////                                                )
////                                            )
////                                        }
////
////                                        if (againAttributes.value == "15") {
////                                            Log.e("TAG", "FFFFFFFFFF")
////                                            layoutPuritySelect.visibility = View.VISIBLE
////                                            bt18.visibility = View.VISIBLE
////                                            bt14.backgroundTintList = ColorStateList.valueOf(
////                                                ContextCompat.getColor(
////                                                    requireContext(),
////                                                    R.color._a5a8ab
////                                                )
////                                            )
////                                            bt14.setTextColor(
////                                                ContextCompat.getColor(
////                                                    requireContext(),
////                                                    R.color.black
////                                                )
////                                            )
////                                            bt18.backgroundTintList = ColorStateList.valueOf(
////                                                ContextCompat.getColor(
////                                                    requireContext(),
////                                                    R.color._000000
////                                                )
////                                            )
////                                            bt18.setTextColor(
////                                                ContextCompat.getColor(
////                                                    requireContext(),
////                                                    R.color._ffffff
////                                                )
////                                            )
////                                        }
////                                    }
////                                }
////                            }
////                        }
////
////
////                        val newUser = CartModel(
////                            product_id = this.id,
////                            name = this.name,
////                            price = this.price,
////                            quantity = 1,
////                            sku = this.sku,
////                            currentTime = System.currentTimeMillis()
////                        )
////                        this.custom_attributes.forEach {
////                            if (it.attribute_code == "size") {
////                                newUser.apply {
////                                    this.size = "" + it.value
////                                }
////                            }
////
////
////                            if (it.attribute_code == "metal_color") {
////                                newUser.apply {
////                                    this.color = "" + it.value
////                                }
////                            }
////
////                            if (it.attribute_code == "metal_type") {
////                                if (it.value == "12") {
////                                    newUser.apply {
////                                        this.material_type = "" + it.value
////                                    }
////                                    this.custom_attributes.forEach { againAttributes ->
////                                        if (againAttributes.attribute_code == "metal_purity") {
////                                            newUser.apply {
////                                                this.purity = "" + it.value
////                                            }
////                                        }
////                                    }
////                                }
////                            } else {
////                                this.custom_attributes.forEach { againAttributes ->
////                                    if (againAttributes.attribute_code == "metal_purity") {
////                                        newUser.apply {
////                                            this.purity = "" + it.value
////                                        }
////                                    }
////                                }
////                            }
////                        }
////
////
////                        btAddToCart.singleClick {
////                            ioThread {
////                                db?.cartDao()?.insertAll(newUser)
////                                badgeCount.value = true
////                            }
////                        }
////
////                        btAddToCart.singleClick {
////                            ioThread {
////                                db?.cartDao()?.insertAll(newUser)
////                                badgeCount.value = true
////                                findNavController().navigate(R.id.action_productDetail_to_cart)
////                            }
////                        }
////
////
////
////
////
////
////                        arrayColors.clear()
////                        arrayPurity.clear()
////
////                        viewModel.allProducts(
////                            token.toString(),
////                            requireView(),
////                            skuId.split("-")[0]
////                        ) {
////                            val _allProductArray = this
////                            _allProductArray.forEach {
////                                val _allProduct = it
////                                val karat = _allProduct.sku.split("-")
////                                if (karat[2] == "14") {
////                                    if (karat[1].toString() == "YG") {
////                                        if (!arrayColors.toString().contains("19", false)) {
////                                            arrayColors.add(
////                                                ItemColor(
////                                                    "19",
////                                                    "14",
////                                                    "" + _allProduct.sku
////                                                )
////                                            )
////                                        }
////                                        Log.e("TAG", "_attributesAA: " + 19)
////                                    }
////
////                                    if (karat[1].toString() == "GW") {
////                                        if (!arrayColors.toString().contains("20", false)) {
////                                            arrayColors.add(
////                                                ItemColor(
////                                                    "20",
////                                                    "14",
////                                                    "" + _allProduct.sku
////                                                )
////                                            )
////                                        }
////                                        Log.e("TAG", "_attributesBB: " + 20)
////                                    }
////                                }
////
////                                if (karat[2] == "18") {
////                                    if (karat[1].toString() == "YG") {
////                                        if (!arrayColors.toString().contains("19", false)) {
////                                            arrayColors.add(
////                                                ItemColor(
////                                                    "19",
////                                                    "15",
////                                                    "" + _allProduct.sku
////                                                )
////                                            )
////                                        }
////                                        Log.e("TAG", "_attributesCC: " + 19)
////                                    }
////
////                                    if (karat[1].toString() == "GW") {
////                                        if (!arrayColors.toString().contains("20", false)) {
////                                            arrayColors.add(
////                                                ItemColor(
////                                                    "20",
////                                                    "15",
////                                                    "" + _allProduct.sku
////                                                )
////                                            )
////                                        }
////                                        Log.e("TAG", "_attributesDD: " + 20)
////                                    }
////                                }
////
////
////
////
////
////
//////                                if (karat[1].toString() == "GW") {
//////                                    if (karat[2] == "14") {
//////                                        if (!arrayPurity.toString().contains("14", false)) {
//////                                            arrayPurity.add(
//////                                                ItemColor(
//////                                                    "20",
//////                                                    "14",
//////                                                    "" + _allProduct.sku
//////                                                )
//////                                            )
//////                                        }
//////                                    }
//////
//////                                    if (karat[2] == "18") {
//////                                        if (!arrayPurity.toString().contains("15", false)) {
//////                                            arrayPurity.add(
//////                                                ItemColor(
//////                                                    "20",
//////                                                    "15",
//////                                                    "" + _allProduct.sku
//////                                                )
//////                                            )
//////                                        }
//////                                    }
//////                                }
//////
//////                                if (karat[1].toString() == "YG") {
//////                                    if (karat[2] == "14") {
//////                                        if (!arrayPurity.toString().contains("14", false)) {
//////                                            arrayPurity.add(
//////                                                ItemColor(
//////                                                    "19",
//////                                                    "14",
//////                                                    "" + _allProduct.sku
//////                                                )
//////                                            )
//////                                        }
//////                                    }
//////
//////                                    if (karat[2] == "18") {
//////                                        if (!arrayPurity.toString().contains("15", false)) {
//////                                            arrayPurity.add(
//////                                                ItemColor(
//////                                                    "19",
//////                                                    "15",
//////                                                    "" + _allProduct.sku
//////                                                )
//////                                            )
//////                                        }
//////                                    }
//////                                }
////
////
////                            }
////
////
////                            Log.e("TAG", "arrayColorsSize: " + arrayColors.size)
////                            Log.e("TAG", "arrayPuritySize: " + arrayPurity.size)
////
//////                            arrayColors.forEach {
//////                                Handler(Looper.getMainLooper()).postDelayed({
//////                                    if (it.color == "19") {
//////                                        Log.e("TAG", "itAAA11 " + it.toString())
//////                                        binding.layoutColor.visibility = View.VISIBLE
//////                                        binding.ivGold.visibility = View.VISIBLE
//////                                        //viewModel.colors(binding, 3)
//////                                    }
//////
//////                                    if (it.color == "20") {
//////                                        Log.e("TAG", "itAAA22 " + it.toString())
//////                                        binding.layoutColor.visibility = View.VISIBLE
//////                                        binding.ivSilver.visibility = View.VISIBLE
//////                                        //  viewModel.colors(binding, 2)
//////                                    }
//////                                }, 50)
//////                            }
//////
//////
//////                            arrayPurity.forEach {
//////                                Handler(Looper.getMainLooper()).postDelayed({
//////                                    if (it.purity == "14") {
//////                                        Log.e("TAG", "itAAA33 " + it.toString())
//////                                        layoutPuritySelect.visibility = View.VISIBLE
//////                                        bt14.visibility = View.VISIBLE
//////////                                    bt14.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color._000000))
//////////                                    bt14.setTextColor(ContextCompat.getColor(requireContext(), R.color._ffffff))
//////                                    }
//////
//////                                    if (it.purity == "15") {
//////                                        Log.e("TAG", "itAAA44 " + it.toString())
//////                                        layoutPuritySelect.visibility = View.VISIBLE
//////                                        bt18.visibility = View.VISIBLE
//////////                                    bt18.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color._000000))
//////////                                    bt18.setTextColor(ContextCompat.getColor(requireContext(), R.color._ffffff))
//////                                    }
//////                                }, 50)
//////                            }
////
////
////
////
//////                            arrayPurity.forEach {
////////                                Log.e("TAG", "itPurity "+it)
////////                                if (it.first == "14"){
////////                                    Log.e("TAG", "CCCCCCCCCCCCCCC")
////////                                    layoutPuritySelect.visibility = View.VISIBLE
////////                                    bt14.visibility = View.VISIBLE
////////                                    bt14.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color._000000))
////////                                    bt14.setTextColor(ContextCompat.getColor(requireContext(), R.color._ffffff))
////////                                }
////////
////////                                if (it.first == "15"){
////////                                    Log.e("TAG", "DDDDDDDDDDDDDD")
////////                                    layoutPuritySelect.visibility = View.VISIBLE
////////                                    bt18.visibility = View.VISIBLE
////////                                    bt18.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color._000000))
////////                                    bt18.setTextColor(ContextCompat.getColor(requireContext(), R.color._ffffff))
////////                                }
////////                            }
//////                    }
////                        }
////                    }
//
//
//                    // Log.e("TAG", "arrayColorsSizeA: " + arrayColors.size)
//
//
////                viewModel.allProducts(token.toString(), requireView(), skuId.split("-")[0]) {
////                    val _allProductArray = this
////                        _allProductArray.forEach {
////                            val _allProduct = it
//////                                if(_allProduct.sku == itemProduct.sku){
//////                                    Log.e("TAG", "allProductsAA: "+itemProduct.sku)
//////                                } else {
////
////                            val karat =_allProduct.sku.split("-")
//////                            Log.e("TAG", "_attributesAA: "+karat[1])
////
////                            if(karat[2] == "14"){
//////                                    Log.e("TAG", "_attributesAA: "+14)
////                                if (karat[1].toString() == "YG"){
////                                    if(!arrayColors.toString().contains("19", false)){
//////                                        Log.e("TAG", "_attributesAA: "+19)
////                                        arrayColors.add(ItemColor("19", ""+_allProduct.sku))
////                                    }
////                                }
////
////                                if (karat[1].toString() == "GW"){
////                                    if(!arrayColors.toString().contains("20", false)){
//////                                        Log.e("TAG", "_attributesBB: "+20)
////                                        arrayColors.add(ItemColor("20", ""+_allProduct.sku))
////                                    }
////                                }
////                            }
////
////
////
////                            if(karat[2] == "18"){
//////                                    Log.e("TAG", "_attributesBB: "+15)
////                                if (karat[1].toString()  == "YG"){
////                                    if(!arrayColors.toString().contains("19", false)){
//////                                        Log.e("TAG", "_attributesAA: "+19)
////                                        arrayColors.add(ItemColor("19", ""+_allProduct.sku))
////                                    }
////                                }
////
////                                if (karat[1].toString()  == "GW"){
////                                    if(!arrayColors.toString().contains("20", false)){
//////                                        Log.e("TAG", "_attributesBB: "+20)
////                                        arrayColors.add(ItemColor("20", ""+_allProduct.sku))
////                                    }
////                                }
////                            }
////
//////                            _allProduct.custom_attributes.forEach { _attributes_metal_color ->
////////                                        Log.e("TAG", "_attributesAA: "+_attributes)
////////                                val gw =_allProduct.sku.split("-")
////////                                Log.e("TAG", "_attributesAA: "+gw[1])
//////                                if(gw[1] == "GW"){
////////                                    Log.e("TAG", "_attributesAA: "+_attributes_metal_color)
//////                                    if (_attributes_metal_color.attribute_code == "metal_color"){
//////                                        if(!arrayColors.toString().contains(""+_attributes_metal_color.value, false)){
////////                                        Log.e("TAG", "_attributesAA: "+_attributes_metal_color)
//////                                            arrayColors.add(ItemColor(""+_attributes_metal_color.value, ""+_allProduct.sku))
//////                                        }
//////                                    }
//////                                }
//////
////////                                if (_attributes_metal_color.attribute_code == "metal_color"){
////////                                    if(!arrayColors.toString().contains(""+_attributes_metal_color.value, false)){
//////////                                        Log.e("TAG", "_attributesAA: "+_attributes_metal_color)
////////
////////                                        arrayColors.add(ItemColor(""+_attributes_metal_color.value, ""+_allProduct.sku))
//////////                                        colorCount ++
////////                                    }
//////////                                            _allProduct.custom_attributes.forEach { _attributes_metal_purity ->
//////////                                                if (_attributes_metal_purity.attribute_code == "metal_purity"){
////////////                                                    Log.e("TAG", "allProductsAA: "+_allProduct.sku)
//////////                                                    if (_attributes_metal_color.value == "20"){
////////////                                                        if (_attributes_metal_purity.value== "14"){
//////////                                                            Log.e("TAG", "allProductsAA: "+_allProduct.sku)
////////////                                                        } else {
////////////                                                            Log.e("TAG", "allProductsCC: "+_allProduct.sku)
////////////                                                        }
//////////                                                        if(!arrayColors.contains(_attributes_metal_color.value)){
//////////                                                            arrayColors.add(""+_attributes_metal_color.value)
//////////                                                        }
//////////                                                    } else {
//////////                                                        Log.e("TAG", "allProductsBB: "+_allProduct.sku)
//////////                                                    }
//////////                                                }
//////////                                            }
////////
//////////                                            if (_attributes.attribute_code == "metal_purity"){
//////////                                                Log.e("TAG", "allProductsAA: "+_allProduct.sku)
//////////                                            } else {
//////////                                                Log.e("TAG", "allProductsBB: "+_allProduct.sku)
//////////                                            }
////////
//////////                                            itemProduct.custom_attributes.forEach { _attributesTop ->
//////////                                                if (_attributesTop.attribute_code == "metal_color"){
//////////                                                    if (_attributes.value == _attributesTop.value){
////////////                                                        if(_allProduct.sku != itemProduct.sku){
//////////                                                            Log.e("TAG", "allProductsBB: "+_allProduct.sku)
////////
//////////                                                            if (""+_attributes.value== "20" && _attributes.value == "14"){
//////////                                                                layoutColor.visibility = View.VISIBLE
//////////                                                                ivSilver.visibility = View.VISIBLE
////////////                                                                viewModel.colors(binding, 2)
//////////                                                                Log.e("TAG", "allProductsAA: "+_allProduct.sku)
//////////                                                            }
//////////                                                            if (""+_attributes.value== "19" && _attributes.value == "14"){
//////////                                                                layoutColor.visibility = View.VISIBLE
//////////                                                                ivGold.visibility = View.VISIBLE
////////////                                                                viewModel.colors(binding, 3)
//////////                                                                Log.e("TAG", "allProductsBB: "+_allProduct.sku)
//////////                                                            }
//////////                                                       // }
//////////                                                    }
//////////                                                }
//////////                                            }
////////                                }
//////
//////
//////
////////                                if (_attributes_metal_color.attribute_code == "metal_purity") {
////////                                    if (!arrayPurity.contains(_attributes_metal_color.value)) {
////////                                        arrayPurity.add(Pair(""+_attributes_metal_color.value, ""+_allProduct.sku))
////////                                    }
////////                                }
//////                            }
//////                                }
//////                                it.custom_attributes.forEach { _attributes ->
//////                                    if (_attributes.attribute_code == "metal_color"){
//////                                        itemProduct.custom_attributes.forEach {
//////                                            if (_attributes.value == it.attribute_code){
//////                                                Log.e("TAG", "allProductsAA: "+itemProduct.sku)
//////                                            } else {
//////                                                Log.e("TAG", "allProductsBB: "+itemProduct.sku)
//////                                            }
//////                                        }
//////                                    }
//////                                }
////                        }
////
////
////                        Log.e("TAG", "arrayColorsSize: "+arrayColors.size)
////
////                        arrayColors.forEach {
////                            Log.e("TAG", "itSSSS "+it)
////                            if (it.color == "19") {
////                                binding.layoutColor.visibility = View.VISIBLE
////                                binding.ivGold.visibility = View.VISIBLE
////                                //viewModel.colors(binding, 3)
////                            }
////
////                            if (it.color == "20") {
////                                binding.layoutColor.visibility = View.VISIBLE
////                                binding.ivSilver.visibility = View.VISIBLE
////                                //  viewModel.colors(binding, 2)
////                            }
////                        }
////
////
////
//////                            arrayPurity.forEach {
//////                                Log.e("TAG", "itPurity "+it)
//////                                if (it.first == "14"){
//////                                    Log.e("TAG", "CCCCCCCCCCCCCCC")
//////                                    layoutPuritySelect.visibility = View.VISIBLE
//////                                    bt14.visibility = View.VISIBLE
//////                                    bt14.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color._000000))
//////                                    bt14.setTextColor(ContextCompat.getColor(requireContext(), R.color._ffffff))
//////                                }
//////
//////                                if (it.first == "15"){
//////                                    Log.e("TAG", "DDDDDDDDDDDDDD")
//////                                    layoutPuritySelect.visibility = View.VISIBLE
//////                                    bt18.visibility = View.VISIBLE
//////                                    bt18.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color._000000))
//////                                    bt18.setTextColor(ContextCompat.getColor(requireContext(), R.color._ffffff))
//////                                }
//////                            }
////                    }
//
//                }
//
//            }
//
//
//        }
//
//
//    }

//
//    private fun setOneDetail(itemProduct: ItemProduct) {
//        Log.e("TAG", "itemProduct.name " + itemProduct.name)
////        images = itemProduct.media_gallery_entries
////        pagerAdapter = ProductDetailPagerAdapter(requireActivity(), images)
//
//        binding.apply {
//            mainThread {
//                Log.e("TAG", "itemProduct.name " + itemProduct.name)
//                textTitle.text = itemProduct.name
//                textPrice.text = "₹ " + getPatternFormat("1", itemProduct.price)
//                textSKU.text = "SKU: " + itemProduct.sku
//
//
//                itemProduct.custom_attributes.forEach {
//                    if (it.attribute_code == "category_ids") {
////                    Log.e("TAG", "itemProduct.attribute_code "+it.value)
//                        var idvalues = ""
//                        val ids =
//                            it.value.toString().replace("[", "").replace("]", "").replace(" ", "")
//                                .split(",")
//                        ids.forEach { idsInner ->
////                        Log.e("TAG", "itemProduct.ss "+idsInner.toString())
//                            val filteredNot = mainCategory.filter { idsInner.toInt() == it.id }
//                            filteredNot.forEach { filteredNotInner ->
//                                idvalues += filteredNotInner.name + ", "
////                            Log.e("TAG", "itemProduct.ss "+filteredNot.toString())
//                            }
//                        }
//                        textCategories.text = "CATEGORIES: " + idvalues
//                    }
//
//
//                    var isColor : String = ""
//                    if (it.attribute_code == "metal_color") {
//                        if (it.value == "19") {
//                            ivGold.visibility = View.VISIBLE
//                            viewModel.colors(binding, 3)
//                            isColor = "19"
//                            Log.e("TAG", "isColor "+isColor)
//                        }
//
//                        if (it.value == "20") {
//                            ivSilver.visibility = View.VISIBLE
//                            viewModel.colors(binding, 2)
//                            isColor = "20"
//                            Log.e("TAG", "isColor "+isColor)
//                        }
//
//                        if (it.value == "21") {
//                            ivPink.visibility = View.VISIBLE
//                            viewModel.colors(binding, 1)
//                            isColor = "21"
//                            Log.e("TAG", "isColor "+isColor)
//                        }
//                    }
//
//                    if (it.attribute_code == "metal_purity") {
//                        if (it.value == "14") {
//                            bt12.backgroundTintList = ColorStateList.valueOf(
//                                ContextCompat.getColor(
//                                    requireContext(),
//                                    R.color._000000
//                                )
//                            )
//                            bt12.setTextColor(
//                                ContextCompat.getColor(
//                                    requireContext(),
//                                    R.color._ffffff
//                                )
//                            )
//                            bt12.visibility = View.VISIBLE
//
//                            bt14.backgroundTintList = ColorStateList.valueOf(
//                                ContextCompat.getColor(
//                                    requireContext(),
//                                    R.color._000000
//                                )
//                            )
//                            bt14.setTextColor(
//                                ContextCompat.getColor(
//                                    requireContext(),
//                                    R.color._ffffff
//                                )
//                            )
//
//                            bt18.backgroundTintList = ColorStateList.valueOf(
//                                ContextCompat.getColor(
//                                    requireContext(),
//                                    R.color._ffffff
//                                )
//                            )
//                            bt18.setTextColor(
//                                ContextCompat.getColor(
//                                    requireContext(),
//                                    R.color._000000
//                                )
//                            )
//
//                            bt14.visibility = View.VISIBLE
//                            Log.e("TAG", "textPurity111 ")
//                            if (isColor == "19") {
//                                textPurity1.text = "14 kt Yellow Gold"
//                            } else if (isColor == "20") {
//                                textPurity1.text = "14 kt White Gold"
//                            } else if (isColor == "21") {
//                                textPurity1.text = "14 kt Rose Gold"
//                            }
//                        }
//
//                        if (it.value == "15") {
//                            bt12.backgroundTintList = ColorStateList.valueOf(
//                                ContextCompat.getColor(
//                                    requireContext(),
//                                    R.color._000000
//                                )
//                            )
//                            bt12.setTextColor(
//                                ContextCompat.getColor(
//                                    requireContext(),
//                                    R.color._ffffff
//                                )
//                            )
//                            bt12.visibility = View.VISIBLE
//
//
//                            bt14.backgroundTintList = ColorStateList.valueOf(
//                                ContextCompat.getColor(
//                                    requireContext(),
//                                    R.color._ffffff
//                                )
//                            )
//                            bt14.setTextColor(
//                                ContextCompat.getColor(
//                                    requireContext(),
//                                    R.color._000000
//                                )
//                            )
//
//                            bt18.backgroundTintList = ColorStateList.valueOf(
//                                ContextCompat.getColor(
//                                    requireContext(),
//                                    R.color._000000
//                                )
//                            )
//                            bt18.setTextColor(
//                                ContextCompat.getColor(
//                                    requireContext(),
//                                    R.color._ffffff
//                                )
//                            )
//                            bt18.visibility = View.VISIBLE
//                            Log.e("TAG", "textPurity222 ")
//                            if (isColor == "19") {
//                                textPurity1.text = "18 kt Yellow Gold"
//                            } else if (isColor == "20") {
//                                textPurity1.text = "18 kt White Gold"
//                            } else if (isColor == "21") {
//                                textPurity1.text = "18 kt Rose Gold"
//                            }
//                        }
//
//                        if (it.value == "16") {
//                            bt13.backgroundTintList = ColorStateList.valueOf(
//                                ContextCompat.getColor(
//                                    requireContext(),
//                                    R.color._000000
//                                )
//                            )
//                            bt13.setTextColor(
//                                ContextCompat.getColor(
//                                    requireContext(),
//                                    R.color._ffffff
//                                )
//                            )
//                            bt13.visibility = View.VISIBLE
//
//
//                            bt95.backgroundTintList = ColorStateList.valueOf(
//                                ContextCompat.getColor(
//                                    requireContext(),
//                                    R.color._000000
//                                )
//                            )
//                            bt95.setTextColor(
//                                ContextCompat.getColor(
//                                    requireContext(),
//                                    R.color._ffffff
//                                )
//                            )
//                            bt95.visibility = View.VISIBLE
//
//                            Log.e("TAG", "textPurity333 ")
//                            textPurity1.text = "Platinum 95"
//
//                        }
//                    }
//
//
//
//
//
//                    if (it.attribute_code == "size") {
//                        btRingSize.text = "" + it.value
////                        viewModel.sizeValue = it.value.toString().toInt()
//
//                        viewModel.arraySizes.forEach { itemSize ->
////                            Log.e("TAG", "itemSize.inchAA " + itemSize.inch)
//                            itemSize.apply {
//                                isSelected = false
//                            }
//                            if (itemSize.inch == "" + it.value) {
////                                Log.e("TAG", "itemSize.inchBB " + itemSize.inch)
//                                itemSize.apply {
//                                    isSelected = true
//                                }
//                            }
//                        }
//                    }
//
//                }
//
//            }
//        }
//
//    }


    @SuppressLint("NotifyDataSetChanged")
    fun callApiDetails2(skuId: String?) {
        arrayAllProduct.clear()

        binding.apply {
            textCategories.visibility = View.GONE
            ivPink.visibility = View.GONE
            ivSilver.visibility = View.GONE
            ivGold.visibility = View.GONE

            bt12.visibility = View.GONE
            bt13.visibility = View.GONE

            bt14.visibility = View.GONE
            bt18.visibility = View.GONE
            bt95.visibility = View.GONE

            layoutRingSize.visibility = View.GONE
            layoutGuide.visibility = View.GONE

            mainThread {
                readData(ADMIN_TOKEN) { token ->
                    viewModel.getProductDetail(token.toString(), requireView(), skuId!!) {
                        val itemProduct = this

                        if (itemProduct.type_id == "simple") {
                            arrayAllProduct.add(itemProduct)
                            Log.e("TAG", "getProductDetailAA " + itemProduct.id)

                            images = itemProduct.media_gallery_entries

                            pagerAdapter = ProductDetailPagerAdapter(requireActivity(), images)
                            rvList1.offscreenPageLimit = 1
                            rvList1.overScrollMode = OVER_SCROLL_NEVER
                            rvList1.adapter = pagerAdapter
                            rvList1.orientation = ViewPager2.ORIENTATION_HORIZONTAL
                            Log.e("TAG", "videoList " + viewModel.item1.size)
                            (rvList1.getRecyclerView()
                                .getItemAnimator() as SimpleItemAnimator).supportsChangeAnimations =
                                false

                            viewModel.indicator(binding, itemProduct.media_gallery_entries, 1)

                            rvList1.registerOnPageChangeCallback(object :
                                ViewPager2.OnPageChangeCallback() {
                                override fun onPageScrolled(
                                    position: Int,
                                    positionOffset: Float,
                                    positionOffsetPixels: Int
                                ) {
                                    super.onPageScrolled(
                                        position,
                                        positionOffset,
                                        positionOffsetPixels
                                    )
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


                            textTitle.text = itemProduct.name
                            textPrice.text = "₹ " + getPatternFormat("1", itemProduct.price)
                            textSKU.text = "SKU: " + itemProduct.sku
                            textWeight2.text = "" + itemProduct.weight + " gram"


                            textCategories.visibility = View.GONE
                            ivPink.visibility = View.GONE
                            ivSilver.visibility = View.GONE
                            ivGold.visibility = View.GONE

                            bt12.visibility = View.GONE
                            bt13.visibility = View.GONE

                            bt14.visibility = View.GONE
                            bt18.visibility = View.GONE
                            bt95.visibility = View.GONE

                            layoutRingSize.visibility = View.GONE
                            layoutGuide.visibility = View.GONE

                            itemProduct.custom_attributes.forEach { itemProductAttr ->
                                mainThread {

                                    if (itemProductAttr.attribute_code == "category_ids") {
                                        var idvalues = ""
                                        val ids =
                                            itemProductAttr.value.toString().replace("[", "")
                                                .replace("]", "").replace(" ", "")
                                                .split(",")
                                        ids.forEach { idsInner ->
                                            val filteredNot =
                                                mainCategory.filter { idsInner.toInt() == it.id }
                                            filteredNot.forEach { filteredNotInner ->
                                                idvalues += filteredNotInner.name + ", "
                                            }
                                        }
                                        textCategories.text = "CATEGORIES: " + idvalues
                                        textCategories.visibility = View.VISIBLE
                                    }

                                    if (itemProductAttr.attribute_code == "metal_color") {
                                        if ("" + itemProductAttr.value == "18") {
                                            ivPink.visibility = View.VISIBLE
                                            viewModel.colors(binding, 1)
                                        }
                                        if ("" + itemProductAttr.value == "19") {
                                            ivGold.visibility = View.VISIBLE
                                            viewModel.colors(binding, 3)
                                        }
                                        if ("" + itemProductAttr.value == "20") {
                                            ivSilver.visibility = View.VISIBLE
                                            viewModel.colors(binding, 2)
                                        }
                                    }


                                    if (itemProductAttr.attribute_code == "metal_purity") {
                                        if (itemProductAttr.value == "14") {
                                            bt12.visibility = View.VISIBLE
                                            bt14.visibility = View.VISIBLE
                                            bt12.backgroundTintList = ColorStateList.valueOf(
                                                ContextCompat.getColor(
                                                    requireContext(),
                                                    R.color._000000
                                                )
                                            )
                                            bt12.setTextColor(
                                                ContextCompat.getColor(
                                                    requireContext(),
                                                    R.color._ffffff
                                                )
                                            )

                                            bt14.backgroundTintList = ColorStateList.valueOf(
                                                ContextCompat.getColor(
                                                    requireContext(),
                                                    R.color._000000
                                                )
                                            )
                                            bt14.setTextColor(
                                                ContextCompat.getColor(
                                                    requireContext(),
                                                    R.color._ffffff
                                                )
                                            )
                                        }

                                        if (itemProductAttr.value == "15") {
                                            bt12.visibility = View.VISIBLE
                                            bt18.visibility = View.VISIBLE

                                            bt12.backgroundTintList = ColorStateList.valueOf(
                                                ContextCompat.getColor(
                                                    requireContext(),
                                                    R.color._000000
                                                )
                                            )
                                            bt12.setTextColor(
                                                ContextCompat.getColor(
                                                    requireContext(),
                                                    R.color._ffffff
                                                )
                                            )

                                            bt18.backgroundTintList = ColorStateList.valueOf(
                                                ContextCompat.getColor(
                                                    requireContext(),
                                                    R.color._000000
                                                )
                                            )
                                            bt18.setTextColor(
                                                ContextCompat.getColor(
                                                    requireContext(),
                                                    R.color._ffffff
                                                )
                                            )
                                        }

                                        if (itemProductAttr.value == "16") {
                                            bt13.visibility = View.VISIBLE
                                            bt95.visibility = View.VISIBLE

                                            bt13.backgroundTintList = ColorStateList.valueOf(
                                                ContextCompat.getColor(
                                                    requireContext(),
                                                    R.color._000000
                                                )
                                            )
                                            bt13.setTextColor(
                                                ContextCompat.getColor(
                                                    requireContext(),
                                                    R.color._ffffff
                                                )
                                            )

                                            bt95.backgroundTintList = ColorStateList.valueOf(
                                                ContextCompat.getColor(
                                                    requireContext(),
                                                    R.color._000000
                                                )
                                            )
                                            bt95.setTextColor(
                                                ContextCompat.getColor(
                                                    requireContext(),
                                                    R.color._ffffff
                                                )
                                            )
                                        }
                                    }



                                    if (itemProductAttr.attribute_code == "metal_purity") {
                                        if (itemProductAttr.value == "14") {
                                            itemProduct.custom_attributes.forEach { itemProductChildAttr ->
                                                if (itemProductChildAttr.attribute_code == "metal_color") {
                                                    if (itemProductChildAttr.value == "19") {
                                                        textPurity1.text = "14 kt Yellow Gold"
                                                    } else if (itemProductChildAttr.value == "20") {
                                                        textPurity1.text = "14 kt White Gold"
                                                    } else if (itemProductChildAttr.value == "18") {
                                                        textPurity1.text = "14 kt Rose Gold"
                                                    }
                                                }
                                            }
                                        }

                                        if (itemProductAttr.value == "15") {
                                            itemProduct.custom_attributes.forEach { itemProductChildAttr ->
                                                if (itemProductChildAttr.attribute_code == "metal_color") {
                                                    if (itemProductChildAttr.value == "19") {
                                                        textPurity1.text = "18 kt Yellow Gold"
                                                    } else if (itemProductChildAttr.value == "20") {
                                                        textPurity1.text = "18 kt White Gold"
                                                    } else if (itemProductChildAttr.value == "18") {
                                                        textPurity1.text = "18 kt Rose Gold"
                                                    }
                                                }
                                            }
                                        }

                                        if (itemProductAttr.value == "16") {
                                            textPurity1.text = "Platinum 95"
                                        }
                                    }



                                    if (itemProductAttr.attribute_code == "size") {
                                        btRingSize.text = "" + itemProductAttr.value
                                        layoutRingSize.visibility = View.VISIBLE
                                        layoutGuide.visibility = View.VISIBLE
                                    }



                                    if (itemProductAttr.attribute_code == "short_description") {
                                        textProductDetails.setText(
                                            Html.fromHtml(
                                                "" + itemProductAttr.value,
                                                Html.FROM_HTML_MODE_COMPACT
                                            )
                                        )
                                    }


                                }


                            }


                            rvList2.setHasFixedSize(true)
                            rvList2.adapter = viewModel.recentAdapter
                            viewModel.recentAdapter.notifyDataSetChanged()
                            viewModel.recentAdapter.submitList(arrayAllProduct)

                        } else if (itemProduct.type_id == "configurable") {
                            viewModel.getProductOptions("" + itemProduct.id) {
                                val jsonObject = JSONObject(this.toString())
                                val ids =
                                    itemProduct.extension_attributes.configurable_product_links.toString()
                                        .replace("[", "").replace("]", "").replace(" ", "")
                                        .split(",")


                                val arrayItemProduct = mutableListOf<ItemProductOptions>()
                                ids.forEach { idsInner ->
                                    val jsonObjectData = jsonObject.getJSONObject(idsInner)
                                    val jsonString = Gson().fromJson(
                                        jsonObjectData.toString(),
                                        ItemProductOptions::class.java
                                    )
                                    arrayItemProduct.add(jsonString)
                                }

                                if (arrayItemProduct.size > 0) {
                                    callApiDetails3(
                                        arrayItemProduct[0].sku,
                                        arrayItemProduct,
                                        itemProduct
                                    )
                                }


                            }
                        }
                    }
                }
            }

        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun callApiDetails3(
        skuId: String?,
        arrayItemProductOptions: MutableList<ItemProductOptions>,
        itemProduct: ItemProduct
    ) {

        arrayAllProduct.clear()

        binding.apply {
            mainThread {
                readData(ADMIN_TOKEN) { token ->
                    viewModel.getProductDetail(token.toString(), requireView(), skuId!!) {
                        val itemProductThis = this

                        if (itemProductThis.type_id == "simple") {
                            arrayAllProduct.add(itemProductThis)
                            Log.e("TAG", "getProductDetailAA " + itemProductThis.id)

                            images = itemProductThis.media_gallery_entries

                            pagerAdapter = ProductDetailPagerAdapter(requireActivity(), images)
                            rvList1.offscreenPageLimit = 1
                            rvList1.overScrollMode = OVER_SCROLL_NEVER
                            rvList1.adapter = pagerAdapter
                            rvList1.orientation = ViewPager2.ORIENTATION_HORIZONTAL
                            Log.e("TAG", "videoList " + viewModel.item1.size)
                            (rvList1.getRecyclerView()
                                .getItemAnimator() as SimpleItemAnimator).supportsChangeAnimations =
                                false

                            viewModel.indicator(binding, itemProductThis.media_gallery_entries, 1)

                            rvList1.registerOnPageChangeCallback(object :
                                ViewPager2.OnPageChangeCallback() {
                                override fun onPageScrolled(
                                    position: Int,
                                    positionOffset: Float,
                                    positionOffsetPixels: Int
                                ) {
                                    super.onPageScrolled(
                                        position,
                                        positionOffset,
                                        positionOffsetPixels
                                    )
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


                            textTitle.text = itemProductThis.name
                            textPrice.text = "₹ " + getPatternFormat("1", itemProductThis.price)
                            textSKU.text = "SKU: " + itemProductThis.sku
                            textWeight2.text = "" + itemProductThis.weight + " gram"


                            itemProductThis.custom_attributes.forEach { itemProductAttr ->


                                if (itemProductAttr.attribute_code == "category_ids") {
                                    var idvalues = ""
                                    val ids =
                                        itemProductAttr.value.toString().replace("[", "")
                                            .replace("]", "").replace(" ", "")
                                            .split(",")
                                    ids.forEach { idsInner ->
                                        val filteredNot =
                                            mainCategory.filter { idsInner.toInt() == it.id }
                                        filteredNot.forEach { filteredNotInner ->
                                            idvalues += filteredNotInner.name + ", "
                                        }
                                    }
                                    textCategories.text = "CATEGORIES: " + idvalues
                                    textCategories.visibility = View.VISIBLE
                                }
                                if (itemProductAttr.attribute_code == "metal_color") {
                                    if ("" + itemProductAttr.value == "18") {
                                        ivPink.visibility = View.VISIBLE
                                        viewModel.colors(binding, 1)
                                    }
                                    if ("" + itemProductAttr.value == "19") {
                                        ivGold.visibility = View.VISIBLE
                                        viewModel.colors(binding, 3)
                                    }
                                    if ("" + itemProductAttr.value == "20") {
                                        ivSilver.visibility = View.VISIBLE
                                        viewModel.colors(binding, 2)
                                    }
                                }


                                if (itemProductAttr.attribute_code == "metal_purity") {
                                    if (itemProductAttr.value == "14") {
                                        bt12.visibility = View.VISIBLE
                                        bt14.visibility = View.VISIBLE
                                        bt12.backgroundTintList = ColorStateList.valueOf(
                                            ContextCompat.getColor(
                                                requireContext(),
                                                R.color._000000
                                            )
                                        )
                                        bt12.setTextColor(
                                            ContextCompat.getColor(
                                                requireContext(),
                                                R.color._ffffff
                                            )
                                        )

                                        bt14.backgroundTintList = ColorStateList.valueOf(
                                            ContextCompat.getColor(
                                                requireContext(),
                                                R.color._000000
                                            )
                                        )
                                        bt14.setTextColor(
                                            ContextCompat.getColor(
                                                requireContext(),
                                                R.color._ffffff
                                            )
                                        )
                                    }

                                    if (itemProductAttr.value == "15") {
                                        bt12.visibility = View.VISIBLE
                                        bt18.visibility = View.VISIBLE

                                        bt12.backgroundTintList = ColorStateList.valueOf(
                                            ContextCompat.getColor(
                                                requireContext(),
                                                R.color._000000
                                            )
                                        )
                                        bt12.setTextColor(
                                            ContextCompat.getColor(
                                                requireContext(),
                                                R.color._ffffff
                                            )
                                        )

                                        bt18.backgroundTintList = ColorStateList.valueOf(
                                            ContextCompat.getColor(
                                                requireContext(),
                                                R.color._000000
                                            )
                                        )
                                        bt18.setTextColor(
                                            ContextCompat.getColor(
                                                requireContext(),
                                                R.color._ffffff
                                            )
                                        )
                                    }

                                    if (itemProductAttr.value == "16") {
                                        bt13.visibility = View.VISIBLE
                                        bt95.visibility = View.VISIBLE

                                        bt13.backgroundTintList = ColorStateList.valueOf(
                                            ContextCompat.getColor(
                                                requireContext(),
                                                R.color._000000
                                            )
                                        )
                                        bt13.setTextColor(
                                            ContextCompat.getColor(
                                                requireContext(),
                                                R.color._ffffff
                                            )
                                        )

                                        bt95.backgroundTintList = ColorStateList.valueOf(
                                            ContextCompat.getColor(
                                                requireContext(),
                                                R.color._000000
                                            )
                                        )
                                        bt95.setTextColor(
                                            ContextCompat.getColor(
                                                requireContext(),
                                                R.color._ffffff
                                            )
                                        )
                                    }
                                }




                                if (itemProductAttr.attribute_code == "metal_purity") {
                                    if (itemProductAttr.value == "14") {
                                        itemProductThis.custom_attributes.forEach { itemProductChildAttr ->
                                            if (itemProductChildAttr.attribute_code == "metal_color") {
                                                if (itemProductChildAttr.value == "19") {
                                                    textPurity1.text = "14 kt Yellow Gold"
                                                } else if (itemProductChildAttr.value == "20") {
                                                    textPurity1.text = "14 kt White Gold"
                                                } else if (itemProductChildAttr.value == "18") {
                                                    textPurity1.text = "14 kt Rose Gold"
                                                }
                                            }
                                        }
                                    }

                                    if (itemProductAttr.value == "15") {
                                        itemProductThis.custom_attributes.forEach { itemProductChildAttr ->
                                            if (itemProductChildAttr.attribute_code == "metal_color") {
                                                if (itemProductChildAttr.value == "19") {
                                                    textPurity1.text = "18 kt Yellow Gold"
                                                } else if (itemProductChildAttr.value == "20") {
                                                    textPurity1.text = "18 kt White Gold"
                                                } else if (itemProductChildAttr.value == "18") {
                                                    textPurity1.text = "18 kt Rose Gold"
                                                }
                                            }
                                        }
                                    }

                                    if (itemProductAttr.value == "16") {
                                        textPurity1.text = "Platinum 95"
                                    }
                                }



                                if (itemProductAttr.attribute_code == "size") {
                                    btRingSize.text = "" + itemProductAttr.value
                                    layoutRingSize.visibility = View.VISIBLE
                                    layoutGuide.visibility = View.VISIBLE
                                }
                            }


//                        arrayItemProductOptions.forEach { itemProductOptions ->
//                            itemProductThis.custom_attributes.forEach { itemCustomSizeAttr ->
//                                if(itemCustomSizeAttr.attribute_code == "size"){
//                                    itemProductThis.custom_attributes.forEach { itemCustomPurityAttr ->
//                                        if(itemCustomPurityAttr.attribute_code == "metal_purity"){
//                                            if (itemProductOptions.size == itemCustomSizeAttr.value && itemProductOptions.metal_purity == itemCustomPurityAttr.value){
//                                                Log.e("TAG", "jsonObjectColorAA: ${itemProductOptions.toString()}")
//                                            }
//
//                                         //   Log.e("TAG", "jsonObjectColorAA: ${itemProductOptions.size} :: ${itemCustomSizeAttr.value}")
//                                        }
//                                    }
//                                }
//                            }
//                        }


//                        arrayItemProductOptions.forEach { itemProductOptions ->
//                            itemProductThis.custom_attributes.forEach { itemCustomSizeAttr ->
//                                if(itemCustomSizeAttr.attribute_code == "size"){
//                                    itemProductThis.custom_attributes.forEach { itemCustomColorAttr ->
//                                        if(itemCustomColorAttr.attribute_code == "metal_color"){
//                                            if (itemProductOptions.size == itemCustomSizeAttr.value && itemProductOptions.metal_color == itemCustomColorAttr.value){
//                                                Log.e("TAG", "jsonObjectPurityAA: ${itemProductOptions.toString()}")
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }


                            arrayItemProductOptions.forEach { itemProductOptions ->
                                itemProductThis.custom_attributes.forEach { itemCustomColorAttr ->
                                    if (itemCustomColorAttr.attribute_code == "metal_color") {
                                        itemProductThis.custom_attributes.forEach { itemCustomPurityAttr ->
                                            if (itemCustomPurityAttr.attribute_code == "metal_purity") {
                                                if (itemProductOptions.metal_color == itemCustomColorAttr.value && itemProductOptions.metal_purity == itemCustomPurityAttr.value) {
                                                    Log.e(
                                                        "TAG",
                                                        "jsonObjectSizeAA: ${itemProductOptions.toString()}"
                                                    )
//                                                viewModel.arrayItemProductOptionsSize.add(itemProductOptions)
                                                }
                                            }
                                        }
                                    }
                                }
                            }


//                        if (arrayItemProduct.size > 0){
//                            callApiDetails3(arrayItemProduct[0].sku, arrayItemProduct, itemProduct)
//                                arrayItemProduct.forEach {itemProductColor->
////                                    Log.e("TAG", "jsonObject: ${itemProductColor.toString()}")
////                                    if(itemProductColor.metal_color == "19"){
//                                        arrayItemProduct.forEach { itemProductPurity ->
//                                            arrayItemProduct.forEach { itemProductSize ->
//                                                if(itemProductColor.metal_color == "19"){
//
//                                                }
//                                            }
//                                        }
////                                    }
//                                }
//                        }


//                        rvList2.setHasFixedSize(true)
//                        rvList2.adapter = viewModel.recentAdapter
//                        viewModel.recentAdapter.notifyDataSetChanged()
//                        viewModel.recentAdapter.submitList(arrayAllProduct)

                        }




                        itemProduct.custom_attributes.forEach { itemProductAttr ->
                            if (itemProductAttr.attribute_code == "short_description") {
                                binding.webView.loadDataWithBaseURL(
                                    null,
                                    "" + itemProductAttr.value,
                                    "text/html",
                                    "utf-8",
                                    null
                                );
                            }
                        }

                        itemProduct.extension_attributes.configurable_product_options.forEach { itemConfigurableProductAttr ->
                            if (itemConfigurableProductAttr.label == "Size") {
                                Log.e(
                                    "TAG",
                                    "itemConfigurableProductSizeAttr " + itemConfigurableProductAttr.values
                                )


                                itemConfigurableProductAttr.values.forEach { itemConfigurableProductSizeAttr->
                                    itemProductThis.custom_attributes.forEach { itemCustomSizeAttr ->
                                        if(itemCustomSizeAttr.attribute_code == "size"){

                                            if(itemConfigurableProductSizeAttr.value_index == itemCustomSizeAttr.value.toString().toInt()){
                                                 viewModel.arrayItemProductOptionsSize.add(Value(itemConfigurableProductSizeAttr.value_index, true))
//                                                Log.e("TAG", "itemCustomSizeAttr.attribute_codeAA "+itemConfigurableProductSizeAttr.value_index + " :: "+ itemCustomSizeAttr.value)
                                            } else {
                                                viewModel.arrayItemProductOptionsSize.add(Value(itemConfigurableProductSizeAttr.value_index, false))
//                                                Log.e("TAG", "itemCustomSizeAttr.attribute_codeBB "+itemConfigurableProductSizeAttr.value_index + " :: "+itemCustomSizeAttr.value)
                                            }
                                        }
                                    }

                                }






                            }

                            if (itemConfigurableProductAttr.label == "Metal Type") {
                                Log.e(
                                    "TAG",
                                    "itemConfigurableProductMetalTypeAttr " + itemConfigurableProductAttr.values
                                )
                                itemConfigurableProductAttr.values.forEach { itemConfigurableProductMetalTypeAttr ->
                                    if (itemConfigurableProductMetalTypeAttr.value_index == 12) {
                                        bt12.visibility = View.VISIBLE
                                    }
                                    if (itemConfigurableProductMetalTypeAttr.value_index == 13) {
                                        bt13.visibility = View.VISIBLE
                                    }
                                }
                            }

                            if (itemConfigurableProductAttr.label == "Metal Purity") {
                                Log.e(
                                    "TAG",
                                    "itemConfigurableProductMetalPurityAttr " + itemConfigurableProductAttr.values
                                )
                                itemConfigurableProductAttr.values.forEach { itemConfigurableProductMetalPurityAttr ->
                                    if (itemConfigurableProductMetalPurityAttr.value_index == 14) {
                                        bt14.visibility = View.VISIBLE
                                    }
                                    if (itemConfigurableProductMetalPurityAttr.value_index == 15) {
                                        bt18.visibility = View.VISIBLE
                                    }
                                    if (itemConfigurableProductMetalPurityAttr.value_index == 16) {
                                        bt95.visibility = View.VISIBLE
                                    }
                                }

                            }

                            if (itemConfigurableProductAttr.label == "Metal Color") {
                                Log.e(
                                    "TAG",
                                    "itemConfigurableProductMetalColorAttr " + itemConfigurableProductAttr.values
                                )
                                itemConfigurableProductAttr.values.forEach { itemConfigurableProductMetalColorAttr ->
                                    if (itemConfigurableProductMetalColorAttr.value_index == 19) {
                                        bt12.visibility = View.VISIBLE
                                    }
                                    if (itemConfigurableProductMetalColorAttr.value_index == 20) {
                                        bt13.visibility = View.VISIBLE
                                    }
                                }
                            }
                        }

                    }
                }
            }


//            data class ItemColor(
//                var color: String = "",
//                var purity: String = "",
//                var sku: String = ""
//            )
        }

        }

    }
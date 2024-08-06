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


    val arrayItemProduct = mutableListOf<ItemProductOptions>()
//    var arrayAllProduct: ArrayList<ItemProduct> = ArrayList()

    lateinit var itemProductThis: ItemProduct

    var currentSku: String = ""

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

//            SAS0005
//            SRI0006
            callApiDetails("SRI0006")
//            }

            Log.e("TAG", "isApiCall " + isApiCall)

            btRingSize.singleClick {
                openDialogSize()
            }


            viewModel.sizeMutableList.value = -1
            viewModel.sizeMutableList.observe(viewLifecycleOwner) {
                if (it != -1) {
                    mainThread {
                        //Log.e("TAG", "sizeMutableList " + viewModel.sizeMutableList.value)
                        arrayItemProduct.forEach { itemProductOptions ->
                            itemProductThis.custom_attributes.forEach { itemCustomColorAttr ->
                                if (itemCustomColorAttr.attribute_code == "metal_color") {
                                    itemProductThis.custom_attributes.forEach { itemCustomPurityAttr ->
                                        if (itemCustomPurityAttr.attribute_code == "metal_purity") {
                                            if (itemProductOptions.metal_color == itemCustomColorAttr.value && itemProductOptions.metal_purity == itemCustomPurityAttr.value && itemProductOptions.size == "" + viewModel.sizeMutableList.value) {
                                                Log.e(
                                                    "TAG",
                                                    "jsonObjectSizeAA: ${itemProductOptions.toString()}"
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }


            ivPink.singleClick { // 25
                arrayItemProduct.forEach { itemProductOptions ->
                    itemProductThis.custom_attributes.forEach { itemCustomSizeAttr ->
                        if (itemCustomSizeAttr.attribute_code == "size") {
                            itemProductThis.custom_attributes.forEach { itemCustomPurityAttr ->
                                if (itemCustomPurityAttr.attribute_code == "metal_purity") {
                                    if (itemProductOptions.size == itemCustomSizeAttr.value && itemProductOptions.metal_purity == itemCustomPurityAttr.value && itemProductOptions.metal_color == "25") {
                                        Log.e(
                                            "TAG",
                                            "jsonObjectColorAA: ${itemProductOptions.toString()}"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            ivSilver.singleClick { // 20
                arrayItemProduct.forEach { itemProductOptions ->
                    itemProductThis.custom_attributes.forEach { itemCustomSizeAttr ->
                        if (itemCustomSizeAttr.attribute_code == "size") {
                            itemProductThis.custom_attributes.forEach { itemCustomPurityAttr ->
                                if (itemCustomPurityAttr.attribute_code == "metal_purity") {
                                    if (itemProductOptions.size == itemCustomSizeAttr.value && itemProductOptions.metal_purity == itemCustomPurityAttr.value && itemProductOptions.metal_color == "20") {
                                        Log.e(
                                            "TAG",
                                            "jsonObjectColorAA: ${itemProductOptions.toString()}"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            ivGold.singleClick { // 19
                arrayItemProduct.forEach { itemProductOptions ->
                    itemProductThis.custom_attributes.forEach { itemCustomSizeAttr ->
                        if (itemCustomSizeAttr.attribute_code == "size") {
                            itemProductThis.custom_attributes.forEach { itemCustomPurityAttr ->
                                if (itemCustomPurityAttr.attribute_code == "metal_purity") {
                                    if (itemProductOptions.size == itemCustomSizeAttr.value && itemProductOptions.metal_purity == itemCustomPurityAttr.value && itemProductOptions.metal_color == "19") {
                                        Log.e(
                                            "TAG",
                                            "jsonObjectColorAA: ${itemProductOptions.toString()}"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }


            bt9.singleClick {
                arrayItemProduct.forEach { itemProductOptions ->
                    itemProductThis.custom_attributes.forEach { itemCustomSizeAttr ->
                        if (itemCustomSizeAttr.attribute_code == "size") {
                            itemProductThis.custom_attributes.forEach { itemCustomColorAttr ->
                                if (itemCustomColorAttr.attribute_code == "metal_color") {
                                    if (itemProductOptions.size == itemCustomSizeAttr.value && itemProductOptions.metal_color == itemCustomColorAttr.value && itemProductOptions.metal_purity == "26") {
                                        Log.e(
                                            "TAG",
                                            "jsonObjectPurityAA14: ${itemProductOptions.toString()}"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }


            bt14.singleClick {
                arrayItemProduct.forEach { itemProductOptions ->
                    itemProductThis.custom_attributes.forEach { itemCustomSizeAttr ->
                        if (itemCustomSizeAttr.attribute_code == "size") {
                            itemProductThis.custom_attributes.forEach { itemCustomColorAttr ->
                                if (itemCustomColorAttr.attribute_code == "metal_color") {
                                    if (itemProductOptions.size == itemCustomSizeAttr.value && itemProductOptions.metal_color == itemCustomColorAttr.value && itemProductOptions.metal_purity == "14") {
                                        Log.e(
                                            "TAG",
                                            "jsonObjectPurityAA14: ${itemProductOptions.toString()}"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            bt18.singleClick {
                arrayItemProduct.forEach { itemProductOptions ->
                    itemProductThis.custom_attributes.forEach { itemCustomSizeAttr ->
                        if (itemCustomSizeAttr.attribute_code == "size") {
                            itemProductThis.custom_attributes.forEach { itemCustomColorAttr ->
                                if (itemCustomColorAttr.attribute_code == "metal_color") {
                                    if (itemProductOptions.size == itemCustomSizeAttr.value && itemProductOptions.metal_color == itemCustomColorAttr.value && itemProductOptions.metal_purity == "15") {
                                        Log.e(
                                            "TAG",
                                            "jsonObjectPurityAA18: ${itemProductOptions.toString()}"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            bt95.singleClick {
                arrayItemProduct.forEach { itemProductOptions ->
                    itemProductThis.custom_attributes.forEach { itemCustomSizeAttr ->
                        if (itemCustomSizeAttr.attribute_code == "size") {
                            itemProductThis.custom_attributes.forEach { itemCustomColorAttr ->
                                if (itemCustomColorAttr.attribute_code == "metal_color") {
                                    if (itemProductOptions.size == itemCustomSizeAttr.value && itemProductOptions.metal_color == itemCustomColorAttr.value && itemProductOptions.metal_purity == "18") {
                                        Log.e(
                                            "TAG",
                                            "jsonObjectPurityAA18: ${itemProductOptions.toString()}"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
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


    @SuppressLint("NotifyDataSetChanged")
    fun callApiDetails(skuId: String?) {
//        arrayAllProduct.clear()
        arrayItemProduct.clear()

        binding.apply {
            textCategories.visibility = View.GONE
            ivPink.visibility = View.GONE
            ivSilver.visibility = View.GONE
            ivGold.visibility = View.GONE

            bt12.visibility = View.GONE
            bt13.visibility = View.GONE

            bt9.visibility = View.GONE
            bt14.visibility = View.GONE
            bt18.visibility = View.GONE
            bt95.visibility = View.GONE

            layoutRingSize.visibility = View.GONE
            layoutGuide.visibility = View.GONE

            mainThread {
                readData(ADMIN_TOKEN) { token ->
                    viewModel.getProductDetail(token.toString(), requireView(), skuId!!) {
                        val itemProduct = this

                        if (itemProduct.type_id == "simple" || itemProduct.type_id == "virtual") {
//                            arrayAllProduct.add(itemProduct)
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
                                        if ("" + itemProductAttr.value == "25") {
                                            ivSilver.visibility = View.VISIBLE
                                            viewModel.colors(binding, 2)
                                        }
                                    }


                                    if (itemProductAttr.attribute_code == "metal_purity") {
                                        if (itemProductAttr.value == "26") {
                                            bt12.visibility = View.VISIBLE
                                            bt9.visibility = View.VISIBLE
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

                                            bt9.backgroundTintList = ColorStateList.valueOf(
                                                ContextCompat.getColor(
                                                    requireContext(),
                                                    R.color._000000
                                                )
                                            )
                                            bt9.setTextColor(
                                                ContextCompat.getColor(
                                                    requireContext(),
                                                    R.color._ffffff
                                                )
                                            )
                                        }

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

                                        if (itemProductAttr.value == "18") {
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
                                        if (itemProductAttr.value == "26") {
                                            itemProduct.custom_attributes.forEach { itemProductChildAttr ->
                                                if (itemProductChildAttr.attribute_code == "metal_color") {
                                                    if (itemProductChildAttr.value == "19") {
                                                        textPurity1.text = "9 kt Yellow Gold"
                                                    } else if (itemProductChildAttr.value == "20") {
                                                        textPurity1.text = "9 kt White Gold"
                                                    } else if (itemProductChildAttr.value == "25") {
                                                        textPurity1.text = "9 kt Rose Gold"
                                                    }
                                                }
                                            }
                                        }

                                        if (itemProductAttr.value == "14") {
                                            itemProduct.custom_attributes.forEach { itemProductChildAttr ->
                                                if (itemProductChildAttr.attribute_code == "metal_color") {
                                                    if (itemProductChildAttr.value == "19") {
                                                        textPurity1.text = "14 kt Yellow Gold"
                                                    } else if (itemProductChildAttr.value == "20") {
                                                        textPurity1.text = "14 kt White Gold"
                                                    } else if (itemProductChildAttr.value == "25") {
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
                                                    } else if (itemProductChildAttr.value == "25") {
                                                        textPurity1.text = "18 kt Rose Gold"
                                                    }
                                                }
                                            }
                                        }

                                        if (itemProductAttr.value == "18") {
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


//                            rvList2.setHasFixedSize(true)
//                            rvList2.adapter = viewModel.recentAdapter
//                            viewModel.recentAdapter.notifyDataSetChanged()
//                            viewModel.recentAdapter.submitList(arrayAllProduct)

                        } else if (itemProduct.type_id == "configurable") {
                            viewModel.getProductOptions("" + itemProduct.id) {
                                val jsonObject = JSONObject(this.toString())
                                val ids =
                                    itemProduct.extension_attributes.configurable_product_links.toString()
                                        .replace("[", "").replace("]", "").replace(" ", "")
                                        .split(",")


                                ids.forEach { idsInner ->
                                    val jsonObjectData = jsonObject.getJSONObject(idsInner)
                                    val jsonString = Gson().fromJson(
                                        jsonObjectData.toString(),
                                        ItemProductOptions::class.java
                                    )
                                    arrayItemProduct.add(jsonString)
                                }

                                if (arrayItemProduct.size > 0) {
                                    currentSku = arrayItemProduct[0].sku
                                    callApiDetails(
                                        currentSku,
//                                        "SRI0006-G-9-YG-7",
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
    fun callApiDetails(
        skuId: String?,
        arrayItemProductOptions: MutableList<ItemProductOptions>,
        itemProduct: ItemProduct
    ) {

//        arrayAllProduct.clear()

        binding.apply {
            mainThread {
                readData(ADMIN_TOKEN) { token ->
                    viewModel.getProductDetail(token.toString(), requireView(), skuId!!) {
                        itemProductThis = this

                        if (itemProductThis.type_id == "simple" || itemProduct.type_id == "virtual") {
//                            arrayAllProduct.add(itemProductThis)
                            Log.e("TAG", "getProductDetailAA " + itemProductThis.id)

                            images = itemProductThis.media_gallery_entries

                            pagerAdapter = ProductDetailPagerAdapter(requireActivity(), images)
                            rvList1.offscreenPageLimit = 1
                            rvList1.overScrollMode = OVER_SCROLL_NEVER
                            rvList1.adapter = pagerAdapter
                            rvList1.orientation = ViewPager2.ORIENTATION_HORIZONTAL
                            Log.e("TAG", "videoList " + images.size)
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
                                    if ("" + itemProductAttr.value == "25") {
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
                                    if (itemProductAttr.value == "26") {
                                        bt12.visibility = View.VISIBLE
                                        bt9.visibility = View.VISIBLE
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

                                        bt9.backgroundTintList = ColorStateList.valueOf(
                                            ContextCompat.getColor(
                                                requireContext(),
                                                R.color._000000
                                            )
                                        )
                                        bt9.setTextColor(
                                            ContextCompat.getColor(
                                                requireContext(),
                                                R.color._ffffff
                                            )
                                        )
                                    }
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

                                    if (itemProductAttr.value == "18") {
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
                                    if (itemProductAttr.value == "26") {
                                        itemProduct.custom_attributes.forEach { itemProductChildAttr ->
                                            if (itemProductChildAttr.attribute_code == "metal_color") {
                                                if (itemProductChildAttr.value == "19") {
                                                    textPurity1.text = "9 kt Yellow Gold"
                                                } else if (itemProductChildAttr.value == "20") {
                                                    textPurity1.text = "9 kt White Gold"
                                                } else if (itemProductChildAttr.value == "25") {
                                                    textPurity1.text = "9 kt Rose Gold"
                                                }
                                            }
                                        }
                                    }

                                    if (itemProductAttr.value == "14") {
                                        itemProductThis.custom_attributes.forEach { itemProductChildAttr ->
                                            if (itemProductChildAttr.attribute_code == "metal_color") {
                                                if (itemProductChildAttr.value == "19") {
                                                    textPurity1.text = "14 kt Yellow Gold"
                                                } else if (itemProductChildAttr.value == "20") {
                                                    textPurity1.text = "14 kt White Gold"
                                                } else if (itemProductChildAttr.value == "25") {
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
                                                } else if (itemProductChildAttr.value == "25") {
                                                    textPurity1.text = "18 kt Rose Gold"
                                                }
                                            }
                                        }
                                    }

                                    if (itemProductAttr.value == "18") {
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


//                            arrayItemProductOptions.forEach { itemProductOptions ->
//                                itemProductThis.custom_attributes.forEach { itemCustomColorAttr ->
//                                    if (itemCustomColorAttr.attribute_code == "metal_color") {
//                                        itemProductThis.custom_attributes.forEach { itemCustomPurityAttr ->
//                                            if (itemCustomPurityAttr.attribute_code == "metal_purity") {
//                                                if (itemProductOptions.metal_color == itemCustomColorAttr.value && itemProductOptions.metal_purity == itemCustomPurityAttr.value) {
//                                                    Log.e(
//                                                        "TAG",
//                                                        "jsonObjectSizeAA: ${itemProductOptions.toString()}"
//                                                    )
////                                                viewModel.arrayItemProductOptionsSize.add(itemProductOptions)
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//                            }


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



                        itemProduct.custom_attributes.forEach { itemProductAttr ->
                            if (itemProductAttr.attribute_code == "short_description") {
                                binding.webView.loadDataWithBaseURL(
                                    null,
                                    "" + itemProductAttr.value,
                                    "text/html",
                                    "utf-8",
                                    null
                                )
                                layoutDiamondAndGemstones.visibility = View.VISIBLE
                            }
                        }

                        itemProduct.extension_attributes.configurable_product_options.forEach { itemConfigurableProductAttr ->
                            if (itemConfigurableProductAttr.label == "Size") {
                                Log.e(
                                    "TAG",
                                    "itemConfigurableProductSizeAttr " + itemConfigurableProductAttr.values
                                )


                                itemConfigurableProductAttr.values.forEach { itemConfigurableProductSizeAttr ->
                                    itemProductThis.custom_attributes.forEach { itemCustomSizeAttr ->
                                        if (itemCustomSizeAttr.attribute_code == "size") {

                                            if (itemConfigurableProductSizeAttr.value_index == itemCustomSizeAttr.value.toString()
                                                    .toInt()
                                            ) {
                                                viewModel.arrayItemProductOptionsSize.add(
                                                    Value(
                                                        itemConfigurableProductSizeAttr.value_index,
                                                        true
                                                    )
                                                )
//                                                Log.e("TAG", "itemCustomSizeAttr.attribute_codeAA "+itemConfigurableProductSizeAttr.value_index + " :: "+ itemCustomSizeAttr.value)
                                            } else {
                                                viewModel.arrayItemProductOptionsSize.add(
                                                    Value(
                                                        itemConfigurableProductSizeAttr.value_index,
                                                        false
                                                    )
                                                )
//                                                Log.e("TAG", "itemCustomSizeAttr.attribute_codeBB "+itemConfigurableProductSizeAttr.value_index + " :: "+itemCustomSizeAttr.value)
                                            }
                                        }
                                    }

                                }


                            }

                            if (itemConfigurableProductAttr.label == "Metal Type") {
                                itemConfigurableProductAttr.values.forEach { itemConfigurableProductMetalTypeAttr ->
                                    if (itemConfigurableProductMetalTypeAttr.value_index == 12) {
                                        bt12.visibility = View.VISIBLE
                                        itemProduct.extension_attributes.configurable_product_options.forEach { itemConfigurableProductAttr ->
                                            if (itemConfigurableProductAttr.label == "Metal Purity") {
                                                itemConfigurableProductAttr.values.forEach { itemConfigurableProductMetalPurityAttr ->
                                                    if (itemConfigurableProductMetalPurityAttr.value_index == 26) {
                                                        bt9.visibility = View.VISIBLE
                                                    }
                                                    if (itemConfigurableProductMetalPurityAttr.value_index == 14) {
                                                        bt14.visibility = View.VISIBLE
                                                    }
                                                    if (itemConfigurableProductMetalPurityAttr.value_index == 15) {
                                                        bt18.visibility = View.VISIBLE
                                                    }
                                                    if (itemConfigurableProductMetalPurityAttr.value_index == 18) {
                                                        bt95.visibility = View.GONE
                                                    }
                                                }
                                            }
                                        }
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
                                            ivGold.visibility = View.VISIBLE
                                        }
                                        if (itemConfigurableProductMetalColorAttr.value_index == 20) {
                                            ivSilver.visibility = View.VISIBLE
                                        }
                                        if (itemConfigurableProductMetalColorAttr.value_index == 25) {
                                            ivPink.visibility = View.VISIBLE
                                        }
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
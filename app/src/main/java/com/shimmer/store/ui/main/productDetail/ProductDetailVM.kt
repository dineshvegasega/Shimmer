package com.shimmer.store.ui.main.productDetail

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.net.Uri
import android.text.TextUtils
import android.util.ArraySet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.shimmer.store.R
import com.shimmer.store.databinding.DialogPdfBinding
import com.shimmer.store.databinding.ItemProductDiamondsBinding
import com.shimmer.store.databinding.ItemSizeBinding
import com.shimmer.store.databinding.ProductDetailBinding
import com.shimmer.store.datastore.db.CartModel
import com.shimmer.store.genericAdapter.GenericAdapter
import com.shimmer.store.models.ItemSizes
import com.shimmer.store.models.Items
import com.shimmer.store.models.products.ItemProduct
import com.shimmer.store.models.products.ItemProductRoot
import com.shimmer.store.models.products.MediaGalleryEntry
import com.shimmer.store.networking.ApiInterface
import com.shimmer.store.networking.CallHandler
import com.shimmer.store.networking.Repository
import com.shimmer.store.ui.mainActivity.MainActivity
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.db
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.storeWebUrl
import com.shimmer.store.utils.pdfviewer.PdfRendererView
import com.shimmer.store.utils.pdfviewer.util.FileUtils.fileFromAsset
import com.shimmer.store.utils.pdfviewer.util.FileUtils.uriToFile
import com.shimmer.store.utils.sessionExpired
import com.shimmer.store.utils.showSnackBar
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProductDetailVM @Inject constructor(private val repository: Repository) : ViewModel() {

    var item1 : ArrayList<Items> = ArrayList()
    var item2 : ArrayList<String> = ArrayList()
    var item3 : ArrayList<Items> = ArrayList()

    var arraySizes : ArraySet<ItemSizes> = ArraySet()


    init {
        item1.add(Items(name = "https://v2.streetsaarthi.in//uploads//1704703414Vishwakarma%20Scheme.jpeg"))
        item1.add(Items(name = "https://v2.streetsaarthi.in//uploads//1704703414Vishwakarma%20Scheme.jpeg"))
        item1.add(Items(name = "https://v2.streetsaarthi.in//uploads//1704703414Vishwakarma%20Scheme.jpeg"))
        item1.add(Items(name = "https://v2.streetsaarthi.in//uploads//1704703414Vishwakarma%20Scheme.jpeg"))
        item1.add(Items(name = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"))
        item1.add(Items(name = "https://v2.streetsaarthi.in//uploads//1704703414Vishwakarma%20Scheme.jpeg"))
        item1.add(Items(name = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4"))
        item1.add(Items(name = "https://v2.streetsaarthi.in//uploads//1704703414Vishwakarma%20Scheme.jpeg"))



        item2.add("1")
        item2.add("2")

        item3.add(Items(name = "https://v2.streetsaarthi.in//uploads//1704703414Vishwakarma%20Scheme.jpeg"))
        item3.add(Items(name = "https://v2.streetsaarthi.in//uploads//1704703414Vishwakarma%20Scheme.jpeg"))
        item3.add(Items(name = "https://v2.streetsaarthi.in//uploads//1704703414Vishwakarma%20Scheme.jpeg"))
        item3.add(Items(name = "https://v2.streetsaarthi.in//uploads//1704703414Vishwakarma%20Scheme.jpeg"))


//        arraySizes.add(ItemSizes(5 , 11.1))
//        arraySizes.add(ItemSizes(6 , 12.1))
//        arraySizes.add(ItemSizes(7 , 13.1))
//        arraySizes.add(ItemSizes(8 , 14.1))
//        arraySizes.add(ItemSizes(9 , 15.1))
//        arraySizes.add(ItemSizes(10 , 16.1))
//        arraySizes.add(ItemSizes(11 , 17.1))
//        arraySizes.add(ItemSizes(12 , 18.1))
//        arraySizes.add(ItemSizes(13 , 19.1))
//        arraySizes.add(ItemSizes(14 , 20.1))
//        arraySizes.add(ItemSizes(15 , 21.1))
//        arraySizes.add(ItemSizes(16 , 22.1))
//        arraySizes.add(ItemSizes(17 , 23.1))
//        arraySizes.add(ItemSizes(18 , 24.1))
//        arraySizes.add(ItemSizes(19 , 25.1))
//        arraySizes.add(ItemSizes(20 , 26.1))
//        arraySizes.add(ItemSizes(21 , 27.1))
//        arraySizes.add(ItemSizes(22 , 28.1))
//        arraySizes.add(ItemSizes(23 , 29.1))
//        arraySizes.add(ItemSizes(24 , 30.1))
//        arraySizes.add(ItemSizes(25 , 31.1))
    }


    fun getCartCount(callBack: Int.() -> Unit){
        viewModelScope.launch {
            val userList: List<CartModel> ?= db?.cartDao()?.getAll()
            var countBadge = 0
            userList?.forEach {
                countBadge += it.quantity
            }
            callBack(countBadge)
        }
    }





    fun getProductDetail(adminToken: String, view: View, skuId: String, callBack: ItemProduct.() -> Unit) =
        viewModelScope.launch {
            repository.callApi(
                callHandler = object : CallHandler<Response<JsonElement>> {
                    override suspend fun sendRequest(apiInterface: ApiInterface) =
                        apiInterface.productsDetail("Bearer " +adminToken, storeWebUrl, skuId)
                    @SuppressLint("SuspiciousIndentation")
                    override fun success(response: Response<JsonElement>) {
                        if (response.isSuccessful) {
                            try {
                                Log.e("TAG", "successAA: ${response.body().toString()}")
                                val mMineUserEntity = Gson().fromJson(response.body(), ItemProduct::class.java)

                                viewModelScope.launch {
//                                    mMineUserEntity.forEach {items ->
                                        val userList: List<CartModel>? = db?.cartDao()?.getAll()
                                        userList?.forEach { user ->
                                            if (mMineUserEntity.id == user.product_id) {
                                                mMineUserEntity.apply {
                                                    isSelected = true
                                                }
//                                                Log.e( "TAG", "YYYYYYYYY: " )
                                            } else {
                                                mMineUserEntity.apply {
                                                    isSelected = false
                                                }
//                                                Log.e( "TAG", "NNNNNNNNNN: " )
                                            }
                                        }
//                                    }


                                        callBack(mMineUserEntity)
                                }


                            } catch (e: Exception) {
                            }
                        }
                    }

                    override fun error(message: String) {
//                        Log.e("TAG", "successAA: ${message}")
//                        super.error(message)
//                        showSnackBar(message)
//                        callBack(message.toString())

                        if(message.contains("fieldName")){
                            showSnackBar("Something went wrong!")
                        } else if(message.contains("The product that was requested doesn't exist")){
                            showSnackBar(message)
                        } else {
                            sessionExpired()
                        }

                    }

                    override fun loading() {
                        super.loading()
                    }
                }
            )
        }


    fun getProductOptions(_id: String, callBack: JsonElement.() -> Unit) =
        viewModelScope.launch {
            repository.callApi(
                callHandler = object : CallHandler<Response<JsonElement>> {
                    override suspend fun sendRequest(apiInterface: ApiInterface) =
                        apiInterface.productsOptions(_id)
                    @SuppressLint("SuspiciousIndentation")
                    override fun success(response: Response<JsonElement>) {
                        if (response.isSuccessful) {
                            try {
                                Log.e("TAG", "successAACCC: ${response.body().toString()}")
                           //     val mMineUserEntity = Gson().fromJson(response.body(), ItemProduct::class.java)
                                callBack(response.body()!!)

                            } catch (e: Exception) {
                            }
                        }
                    }

                    override fun error(message: String) {
//                        Log.e("TAG", "successAA: ${message}")
//                        super.error(message)
//                        showSnackBar(message)
//                        callBack(message.toString())

                        if(message.contains("fieldName")){
                            showSnackBar("Something went wrong!")
                        } else if(message.contains("The product that was requested doesn't exist")){
                            showSnackBar(message)
                        } else {
                            sessionExpired()
                        }

                    }

                    override fun loading() {
                        super.loading()
                    }
                }
            )
        }



    fun allProducts(adminToken: String, view: View, skuId: String, callBack: ArrayList<ItemProduct>.() -> Unit) =
        viewModelScope.launch {
            repository.callApi(
                callHandler = object : CallHandler<Response<JsonElement>> {
                    override suspend fun sendRequest(apiInterface: ApiInterface) =
                        apiInterface.allProducts("Bearer " +adminToken, skuId)
                    @SuppressLint("SuspiciousIndentation")
                    override fun success(response: Response<JsonElement>) {
                        if (response.isSuccessful) {
                            try {
                                Log.e("TAG", "successAABBCC: ${response.body().toString()}")
                                val typeToken = object : TypeToken<ArrayList<ItemProduct>>() {}.type
                                val changeValue = Gson().fromJson<ArrayList<ItemProduct>>(response.body(), typeToken)
//                                changeValue.forEach {
//                                    Log.e("TAG", "allProductsAA: "+it.sku)
//                                }
                                if(changeValue.isNotEmpty()){
                                    callBack(changeValue)
                                }
                            } catch (e: Exception) {
                            }
                        }
                    }

                    override fun error(message: String) {
//                        Log.e("TAG", "successAA: ${message}")
//                        super.error(message)
//                        showSnackBar(message)
//                        callBack(message.toString())

                        if(message.contains("fieldName")){
                            showSnackBar("Something went wrong!")
                        } else if(message.contains("The product that was requested doesn't exist")){
                            showSnackBar(message)
                        } else {
                            sessionExpired()
                        }

                    }

                    override fun loading() {
                        super.loading()
                    }
                }
            )
        }




    val recentAdapter = object : GenericAdapter<ItemProductDiamondsBinding, ItemProduct>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemProductDiamondsBinding.inflate(inflater, parent, false)

        override fun onBindHolder(
            binding: ItemProductDiamondsBinding,
            dataClass: ItemProduct,
            position: Int
        ) {
            binding.apply {
                dataClass.custom_attributes.forEach{ itemProductAttr ->

//                    if (itemProductAttr.attribute_code == "metal_color") {
//                        textDiamondColorText.text = "" + itemProductAttr.value
//                    }
                    if (itemProductAttr.attribute_code == "size") {
                        textDiamondSizeText.text = "" + itemProductAttr.value
                    }

                    if (itemProductAttr.attribute_code == "metal_purity") {
                        if (itemProductAttr.value == "14") {
                            dataClass.custom_attributes.forEach { itemProductChildAttr ->
                                if (itemProductChildAttr.attribute_code == "metal_color") {
                                    if (itemProductChildAttr.value == "19") {
                                        textDiamondColorText.text = "14 kt Yellow Gold"
                                    } else if (itemProductChildAttr.value == "20") {
                                        textDiamondColorText.text = "14 kt White Gold"
                                    } else if (itemProductChildAttr.value == "18") {
                                        textDiamondColorText.text = "14 kt Rose Gold"
                                    }
                                }
                            }
                        }

                        if (itemProductAttr.value == "15") {
                            dataClass.custom_attributes.forEach { itemProductChildAttr ->
                                if (itemProductChildAttr.attribute_code == "metal_color") {
                                    if (itemProductChildAttr.value == "19") {
                                        textDiamondColorText.text = "18 kt Yellow Gold"
                                    } else if (itemProductChildAttr.value == "20") {
                                        textDiamondColorText.text = "18 kt White Gold"
                                    } else if (itemProductChildAttr.value == "18") {
                                        textDiamondColorText.text = "18 kt Rose Gold"
                                    }
                                }
                            }
                        }

                        if (itemProductAttr.value == "16") {
                            textDiamondColorText.text = "Platinum 95"
                        }
                    }

                }

                textDiamondClarityText.text = ""
                textDiamondShapeText.text = ""
                textDiamondNoText.text = ""
                textDiamondTotalWeightText.text = ""+dataClass.weight

            }
        }
    }



    var sizeMutableListClose = MutableLiveData<Boolean>(false)
    var sizeMutableList = MutableLiveData<Int>(0)
//    var sizeValue : Int = 0
    var selectedPosition = -1
    val sizeAdapter = object : GenericAdapter<ItemSizeBinding, ItemSizes>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemSizeBinding.inflate(inflater, parent, false)

        @SuppressLint("NotifyDataSetChanged")
        override fun onBindHolder(
            binding: ItemSizeBinding,
            dataClass: ItemSizes,
            position: Int
        ) {
            binding.apply {
                textSize.text = dataClass.inch.toString()
//                textMM.text = dataClass.mm.toString() +" mm"

                ivIcon.singleClick {
                    selectedPosition = position
                    val list = currentList
                    list.apply {
                        forEach {
                            it.isSelected = false
                        }
                        this[position].isSelected = true
                    }
////                    dataClass.isSelected = !dataClass.isSelected
//                    notifyItemChanged(position)



                    sizeMutableList.value = dataClass.inch.toInt()
                    sizeMutableListClose.value = true
                    notifyDataSetChanged()
                }

//                if (selectedPosition == position) {
//                    ivIcon.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.app_color))
//                } else {
//                    ivIcon.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color._E1E2DB))
//                }

                ivIcon.setBackgroundColor( if(dataClass.isSelected) ContextCompat.getColor(binding.root.context, R.color.app_color)
                else ContextCompat.getColor(binding.root.context, R.color._E1E2DB))
            }
        }
    }



    @SuppressLint("UseCompatLoadingForDrawables")
    public fun indicator(binding: ProductDetailBinding, arrayList: ArrayList<MediaGalleryEntry>, current: Int) {
        val views : ArrayList<View> = ArrayList()
        views.add(binding.indicatorLayout.view1)
        views.add(binding.indicatorLayout.view2)
        views.add(binding.indicatorLayout.view3)
        views.add(binding.indicatorLayout.view4)
        views.add(binding.indicatorLayout.view5)
        views.add(binding.indicatorLayout.view6)
        views.add(binding.indicatorLayout.view7)
        views.add(binding.indicatorLayout.view8)
        views.add(binding.indicatorLayout.view9)
        views.add(binding.indicatorLayout.view10)
        views.add(binding.indicatorLayout.view11)
        views.add(binding.indicatorLayout.view12)
        views.add(binding.indicatorLayout.view13)
        views.add(binding.indicatorLayout.view14)
        views.add(binding.indicatorLayout.view15)
        views.add(binding.indicatorLayout.view16)
        views.add(binding.indicatorLayout.view17)
        views.add(binding.indicatorLayout.view18)
        views.add(binding.indicatorLayout.view19)
        views.add(binding.indicatorLayout.view20)

        var index = 0
        views.forEach {
            Log.e("TAG", " index "+index+" size "+arrayList.size+" current "+current)
            if (index <= (arrayList.size -1)){
                it.visibility = VISIBLE
                if (arrayList[index].file.endsWith(".mp4")){
                    it.setBackgroundResource(R.drawable.ic_triangle_right)
                } else {
                    it.setBackgroundResource(R.drawable.bg_all_round_black)
                }
                if (index == current){
                    it.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            MainActivity.context.get()!!, R.color.app_color))
                } else {
                    it.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            MainActivity.context.get()!!, R.color._9A9A9A))
                }
            } else {
                it.visibility = GONE
            }
            index++
        }
    }





    @SuppressLint("UseCompatLoadingForDrawables")
    public fun colors(binding: ProductDetailBinding, type: Int) {
        when (type) {
            1 -> {
                binding.ivPink.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.drawable._26571_check_circle_icon))
                binding.ivSilver.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.drawable._665384_circle_round_icon))
                binding.ivGold.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.drawable._665384_circle_round_icon))
            }
            2 -> {
                binding.ivPink.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.drawable._665384_circle_round_icon))
                binding.ivSilver.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.drawable._26571_check_circle_icon))
                binding.ivGold.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.drawable._665384_circle_round_icon))
            }
            3 -> {
                binding.ivPink.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.drawable._665384_circle_round_icon))
                binding.ivSilver.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.drawable._665384_circle_round_icon))
                binding.ivGold.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.drawable._26571_check_circle_icon))
            }
        }
    }



    public fun openDialogPdf(type: Int, pdf : String) {
        val dialogBinding = DialogPdfBinding.inflate(MainActivity.activity.get()?.getSystemService(
            Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        )
        val dialog = Dialog(MainActivity.context.get()!!)
        dialog.setContentView(dialogBinding.root)
//        dialog.setOnShowListener { dia ->
//            val bottomSheetDialog = dia as BottomSheetDialog
//            val bottomSheetInternal: FrameLayout =
//                bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)!!
//            bottomSheetInternal.setBackgroundResource(R.drawable.bg_top_round_corner)
//        }
        dialog.show()
        val window = dialog.window
        window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
//        window.setBackgroundDrawableResource(R.color._00000000)
//        val yes = mybuilder.findViewById<AppCompatImageView>(R.id.imageCross)
//        val title = mybuilder.findViewById<AppCompatTextView>(R.id.textTitleMain)

        dialogBinding.imageCross.singleClick {
            dialog.dismiss()
        }

        initPdfViewerWithPath(dialogBinding, pdf)
    }





    var isFromAssets = true
    private fun initPdfViewerWithPath(bindingPdf : DialogPdfBinding, filePath: String?) {
        if (TextUtils.isEmpty(filePath)) {
            onPdfError(bindingPdf, "")
            return
        }
        try {
            val file = if (filePath!!.startsWith("content://")) {
                uriToFile(MainActivity.activity.get()!!, Uri.parse(filePath))
            } else if (isFromAssets) {
                fileFromAsset(MainActivity.context.get()!!, filePath)
            } else {
                File(filePath)
            }
            bindingPdf.pdfView.initWithFile(file)
        } catch (e: Exception) {
            onPdfError(bindingPdf, e.toString())
        }


        bindingPdf.pdfView.statusListener = object : PdfRendererView.StatusCallBack {
            override fun onPdfLoadStart() {
                MainActivity.activity.get()?.runOnUiThread {
                    true.showProgressBar(bindingPdf)
                }
            }

            override fun onPdfLoadProgress(
                progress: Int,
                downloadedBytes: Long,
                totalBytes: Long?
            ) {
                //Download is in progress
            }

            override fun onPdfLoadSuccess(absolutePath: String) {
                MainActivity.activity.get()?.runOnUiThread {
                    false.showProgressBar(bindingPdf)
                    //downloadedFilePath = absolutePath
                }
            }

            override fun onError(error: Throwable) {
                MainActivity.activity.get()?.runOnUiThread {
                    false.showProgressBar(bindingPdf)
                    onPdfError(bindingPdf, error.toString())
                }
            }

            override fun onPageChanged(currentPage: Int, totalPage: Int) {
                //Page change. Not require
            }
        }
    }



    private fun onPdfError(bindingPdf : DialogPdfBinding, e: String) {
        Log.e("Pdf render error", e)
        AlertDialog.Builder(MainActivity.context.get())
            .setTitle("pdf_viewer_error")
            .setMessage("error_pdf_corrupted")
            .setPositiveButton("pdf_viewer_retry") { dialog, which ->
                MainActivity.activity.get()?.runOnUiThread {
                    initPdfViewerWithPath(bindingPdf, "quote.pdf")
                }
            }
            .setNegativeButton("pdf_viewer_cancel", null)
            .show()
    }

    private fun Boolean.showProgressBar(bindingPdf: DialogPdfBinding) {
        bindingPdf.progressBar.visibility = if (this) VISIBLE else GONE
    }


}
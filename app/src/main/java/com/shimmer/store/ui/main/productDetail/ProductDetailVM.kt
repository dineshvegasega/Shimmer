package com.shimmer.store.ui.main.productDetail

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.shimmer.store.R
import com.shimmer.store.databinding.DialogPdfBinding
import com.shimmer.store.databinding.ItemProductDiamondsBinding
import com.shimmer.store.databinding.ProductDetailBinding
import com.shimmer.store.genericAdapter.GenericAdapter
import com.shimmer.store.models.Items
import com.shimmer.store.ui.mainActivity.MainActivity
import com.shimmer.store.utils.pdfviewer.PdfRendererView
import com.shimmer.store.utils.pdfviewer.util.FileUtils.fileFromAsset
import com.shimmer.store.utils.pdfviewer.util.FileUtils.uriToFile
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProductDetailVM @Inject constructor() : ViewModel() {

    var item1 : ArrayList<Items> = ArrayList()
    var item2 : ArrayList<String> = ArrayList()
    var item3 : ArrayList<Items> = ArrayList()


    init {
        item1.add(Items("https://v2.streetsaarthi.in//uploads//1704703414Vishwakarma%20Scheme.jpeg"))
        item1.add(Items("https://v2.streetsaarthi.in//uploads//1704703414Vishwakarma%20Scheme.jpeg"))
        item1.add(Items("https://v2.streetsaarthi.in//uploads//1704703414Vishwakarma%20Scheme.jpeg"))
        item1.add(Items("https://v2.streetsaarthi.in//uploads//1704703414Vishwakarma%20Scheme.jpeg"))
        item1.add(Items("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"))
        item1.add(Items("https://v2.streetsaarthi.in//uploads//1704703414Vishwakarma%20Scheme.jpeg"))
        item1.add(Items("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4"))
        item1.add(Items("https://v2.streetsaarthi.in//uploads//1704703414Vishwakarma%20Scheme.jpeg"))



        item2.add("1")
        item2.add("2")

        item3.add(Items("https://v2.streetsaarthi.in//uploads//1704703414Vishwakarma%20Scheme.jpeg"))
        item3.add(Items("https://v2.streetsaarthi.in//uploads//1704703414Vishwakarma%20Scheme.jpeg"))
        item3.add(Items("https://v2.streetsaarthi.in//uploads//1704703414Vishwakarma%20Scheme.jpeg"))
        item3.add(Items("https://v2.streetsaarthi.in//uploads//1704703414Vishwakarma%20Scheme.jpeg"))
    }




    val recentAdapter = object : GenericAdapter<ItemProductDiamondsBinding, Items>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemProductDiamondsBinding.inflate(inflater, parent, false)

        override fun onBindHolder(
            binding: ItemProductDiamondsBinding,
            dataClass: Items,
            position: Int
        ) {
            binding.apply {
//                recyclerViewRecentItems.setHasFixedSize(true)
//                val headlineAdapter = RecentChildAdapter(
//                    binding.root.context,
//                    dataClass.items,
//                    position
//                )
//                recyclerViewRecentItems.adapter = headlineAdapter
//                recyclerViewRecentItems.layoutManager = LinearLayoutManager(binding.root.context)
            }
        }
    }





    @SuppressLint("UseCompatLoadingForDrawables")
    public fun indicator(binding: ProductDetailBinding, arrayList: ArrayList<Items>, current: Int) {
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
                if (arrayList[index].name.endsWith(".mp4")){
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



    public fun openDialogSize(type: Int, pdf : String) {
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
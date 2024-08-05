package com.shimmer.store.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.shimmer.store.R
import com.shimmer.store.databinding.AbcBinding
import com.shimmer.store.databinding.DialogPdfBinding
import com.shimmer.store.utils.pdfviewer.PdfRendererView
import com.shimmer.store.utils.pdfviewer.util.FileUtils.fileFromAsset
import com.shimmer.store.utils.pdfviewer.util.FileUtils.uriToFile
import com.shimmer.store.utils.singleClick
import java.io.File

class ABC  : AppCompatActivity() {

    private val TAG = "MainActivity"

    lateinit var binding: AbcBinding

    private val RECORD_REQUEST_CODE: Int = 101

    lateinit var pagerAdapter: FragmentStateAdapter

    lateinit var videoList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AbcBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setupPermissions()

//        mainViewBinding.btnReqPermission.setOnClickListener { makeRequest() }

//        initViewPagerView()


      //  initPdfViewerWithPath("quote.pdf")

//        binding.button.setOnClickListener {
//            openDialog(1, "quote.pdf")
//        }
//
//        indicator(binding, 20, 10, 1)


        val content = "<table style=width:100%>\n<tr>\n<th>Shape</th>\n<th>Pieces</th>\n<th>Weight</th>\n<th>Price</th>\n</tr>\n<tr>\n<td>Round</td>\n<td>12</td>\n<td>0.03</td>\n<td>30000</td>\n</tr>\n<tr>\n<td>Oval</td>\n<td>1</td>\n<td>0.21</td>\n<td>30000</td>\n</tr>\n<tr>\n<td>Princess</td>\n<td>2</td>\n<td>0.25</td>\n<td>35000</td>\n</tr>\n</table>"
        binding.webView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);


    }



    private fun openDialog(type: Int, pdf : String) {
        val dialogBinding = DialogPdfBinding.inflate(getSystemService(
            Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        )
        val dialog = Dialog(this)
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

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun indicator(binding: AbcBinding, size: Int, current: Int, type : Int) {
        val vv :  ArrayList<View> = ArrayList()
        vv.add(binding.view1)
        vv.add(binding.view2)
        vv.add(binding.view3)
        vv.add(binding.view4)
        vv.add(binding.view5)
        vv.add(binding.view6)
        vv.add(binding.view7)
        vv.add(binding.view8)
        vv.add(binding.view9)
        vv.add(binding.view10)
        vv.add(binding.view11)
        vv.add(binding.view12)
        vv.add(binding.view13)
        vv.add(binding.view14)
        vv.add(binding.view15)
        vv.add(binding.view16)
        vv.add(binding.view17)
        vv.add(binding.view18)
        vv.add(binding.view19)
        vv.add(binding.view20)

        var index = 1
        vv.forEach {
            Log.e(TAG, " index "+index+" size "+size+" size "+vv.size)
            if (index <= size){
                it.visibility = VISIBLE
                if (type == 1){
                    it.setBackgroundResource(R.drawable.bg_all_round_black)
                } else {
                    it.setBackgroundResource(R.drawable.ic_triangle_right)
                }
                if (index == current){
                    it.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.app_color))
                } else {
                    it.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color._9A9A9A))
                }
            } else {
                it.visibility = GONE
            }
            index++
        }



//                binding.view1.visibility = GONE
//                binding.view2.visibility = GONE
//                binding.view3.visibility = GONE
//                binding.view4.visibility = GONE
//                binding.view5.visibility = GONE
//                binding.view6.visibility = GONE
//                binding.view7.visibility = GONE
//                binding.view8.visibility = GONE
//                binding.view9.visibility = GONE
//                binding.view10.visibility = GONE
//                binding.view11.visibility = GONE
//                binding.view12.visibility = GONE
//                binding.view13.visibility = GONE
//                binding.view14.visibility = GONE
//                binding.view15.visibility = GONE
//                binding.view16.visibility = GONE
//                binding.view17.visibility = GONE
//                binding.view18.visibility = GONE
//                binding.view19.visibility = GONE
//                binding.view20.visibility = GONE




//        when(size) {
//            1 -> {
//                binding.view1.visibility = VISIBLE
//                binding.view2.visibility = GONE
//                binding.view3.visibility = GONE
//                binding.view4.visibility = GONE
//                binding.view5.visibility = GONE
//                binding.view6.visibility = GONE
//                binding.view7.visibility = GONE
//                binding.view8.visibility = GONE
//                binding.view9.visibility = GONE
//                binding.view10.visibility = GONE
//                binding.view11.visibility = GONE
//                binding.view12.visibility = GONE
//                binding.view13.visibility = GONE
//                binding.view14.visibility = GONE
//                binding.view15.visibility = GONE
//                binding.view16.visibility = GONE
//                binding.view17.visibility = GONE
//                binding.view18.visibility = GONE
//                binding.view19.visibility = GONE
//                binding.view20.visibility = GONE
//            }
//
//            2 -> {
//                binding.view1.visibility = VISIBLE
//                binding.view2.visibility = VISIBLE
//                binding.view3.visibility = GONE
//                binding.view4.visibility = GONE
//                binding.view5.visibility = GONE
//                binding.view6.visibility = GONE
//                binding.view7.visibility = GONE
//                binding.view8.visibility = GONE
//                binding.view9.visibility = GONE
//                binding.view10.visibility = GONE
//                binding.view11.visibility = GONE
//                binding.view12.visibility = GONE
//                binding.view13.visibility = GONE
//                binding.view14.visibility = GONE
//                binding.view15.visibility = GONE
//                binding.view16.visibility = GONE
//                binding.view17.visibility = GONE
//                binding.view18.visibility = GONE
//                binding.view19.visibility = GONE
//                binding.view20.visibility = GONE
//            }
//
//            3 -> {
//                binding.view1.visibility = VISIBLE
//                binding.view2.visibility = VISIBLE
//                binding.view3.visibility = VISIBLE
//                binding.view4.visibility = GONE
//                binding.view5.visibility = GONE
//                binding.view6.visibility = GONE
//                binding.view7.visibility = GONE
//                binding.view8.visibility = GONE
//                binding.view9.visibility = GONE
//                binding.view10.visibility = GONE
//                binding.view11.visibility = GONE
//                binding.view12.visibility = GONE
//                binding.view13.visibility = GONE
//                binding.view14.visibility = GONE
//                binding.view15.visibility = GONE
//                binding.view16.visibility = GONE
//                binding.view17.visibility = GONE
//                binding.view18.visibility = GONE
//                binding.view19.visibility = GONE
//                binding.view20.visibility = GONE
//            }
//
//            4 -> {
//                binding.view1.visibility = VISIBLE
//                binding.view2.visibility = VISIBLE
//                binding.view3.visibility = VISIBLE
//                binding.view4.visibility = VISIBLE
//                binding.view5.visibility = GONE
//                binding.view6.visibility = GONE
//                binding.view7.visibility = GONE
//                binding.view8.visibility = GONE
//                binding.view9.visibility = GONE
//                binding.view10.visibility = GONE
//                binding.view11.visibility = GONE
//                binding.view12.visibility = GONE
//                binding.view13.visibility = GONE
//                binding.view14.visibility = GONE
//                binding.view15.visibility = GONE
//                binding.view16.visibility = GONE
//                binding.view17.visibility = GONE
//                binding.view18.visibility = GONE
//                binding.view19.visibility = GONE
//                binding.view20.visibility = GONE
//            }
//
//            5 -> {
//                binding.view1.visibility = VISIBLE
//                binding.view2.visibility = VISIBLE
//                binding.view3.visibility = VISIBLE
//                binding.view4.visibility = VISIBLE
//                binding.view5.visibility = VISIBLE
//                binding.view6.visibility = GONE
//                binding.view7.visibility = GONE
//                binding.view8.visibility = GONE
//                binding.view9.visibility = GONE
//                binding.view10.visibility = GONE
//                binding.view11.visibility = GONE
//                binding.view12.visibility = GONE
//                binding.view13.visibility = GONE
//                binding.view14.visibility = GONE
//                binding.view15.visibility = GONE
//                binding.view16.visibility = GONE
//                binding.view17.visibility = GONE
//                binding.view18.visibility = GONE
//                binding.view19.visibility = GONE
//                binding.view20.visibility = GONE
//            }
//
//            6 -> {
//                binding.view1.visibility = VISIBLE
//                binding.view2.visibility = VISIBLE
//                binding.view3.visibility = VISIBLE
//                binding.view4.visibility = VISIBLE
//                binding.view5.visibility = VISIBLE
//                binding.view6.visibility = VISIBLE
//                binding.view7.visibility = GONE
//                binding.view8.visibility = GONE
//                binding.view9.visibility = GONE
//                binding.view10.visibility = GONE
//                binding.view11.visibility = GONE
//                binding.view12.visibility = GONE
//                binding.view13.visibility = GONE
//                binding.view14.visibility = GONE
//                binding.view15.visibility = GONE
//                binding.view16.visibility = GONE
//                binding.view17.visibility = GONE
//                binding.view18.visibility = GONE
//                binding.view19.visibility = GONE
//                binding.view20.visibility = GONE
//            }
//
//            7 -> {
//                binding.view1.visibility = VISIBLE
//                binding.view2.visibility = VISIBLE
//                binding.view3.visibility = VISIBLE
//                binding.view4.visibility = VISIBLE
//                binding.view5.visibility = VISIBLE
//                binding.view6.visibility = VISIBLE
//                binding.view7.visibility = VISIBLE
//                binding.view8.visibility = GONE
//                binding.view9.visibility = GONE
//                binding.view10.visibility = GONE
//                binding.view11.visibility = GONE
//                binding.view12.visibility = GONE
//                binding.view13.visibility = GONE
//                binding.view14.visibility = GONE
//                binding.view15.visibility = GONE
//                binding.view16.visibility = GONE
//                binding.view17.visibility = GONE
//                binding.view18.visibility = GONE
//                binding.view19.visibility = GONE
//                binding.view20.visibility = GONE
//            }
//
//            8 -> {
//                binding.view1.visibility = VISIBLE
//                binding.view2.visibility = VISIBLE
//                binding.view3.visibility = VISIBLE
//                binding.view4.visibility = VISIBLE
//                binding.view5.visibility = VISIBLE
//                binding.view6.visibility = VISIBLE
//                binding.view7.visibility = VISIBLE
//                binding.view8.visibility = VISIBLE
//                binding.view9.visibility = GONE
//                binding.view10.visibility = GONE
//                binding.view11.visibility = GONE
//                binding.view12.visibility = GONE
//                binding.view13.visibility = GONE
//                binding.view14.visibility = GONE
//                binding.view15.visibility = GONE
//                binding.view16.visibility = GONE
//                binding.view17.visibility = GONE
//                binding.view18.visibility = GONE
//                binding.view19.visibility = GONE
//                binding.view20.visibility = GONE
//            }
//
//            9 -> {
//                binding.view1.visibility = VISIBLE
//                binding.view2.visibility = VISIBLE
//                binding.view3.visibility = VISIBLE
//                binding.view4.visibility = VISIBLE
//                binding.view5.visibility = VISIBLE
//                binding.view6.visibility = VISIBLE
//                binding.view7.visibility = VISIBLE
//                binding.view8.visibility = VISIBLE
//                binding.view9.visibility = VISIBLE
//                binding.view10.visibility = GONE
//                binding.view11.visibility = GONE
//                binding.view12.visibility = GONE
//                binding.view13.visibility = GONE
//                binding.view14.visibility = GONE
//                binding.view15.visibility = GONE
//                binding.view16.visibility = GONE
//                binding.view17.visibility = GONE
//                binding.view18.visibility = GONE
//                binding.view19.visibility = GONE
//                binding.view20.visibility = GONE
//            }
//
//            10 -> {
//                binding.view1.visibility = VISIBLE
//                binding.view2.visibility = VISIBLE
//                binding.view3.visibility = VISIBLE
//                binding.view4.visibility = VISIBLE
//                binding.view5.visibility = VISIBLE
//                binding.view6.visibility = VISIBLE
//                binding.view7.visibility = VISIBLE
//                binding.view8.visibility = VISIBLE
//                binding.view9.visibility = VISIBLE
//                binding.view10.visibility = VISIBLE
//                binding.view11.visibility = GONE
//                binding.view12.visibility = GONE
//                binding.view13.visibility = GONE
//                binding.view14.visibility = GONE
//                binding.view15.visibility = GONE
//                binding.view16.visibility = GONE
//                binding.view17.visibility = GONE
//                binding.view18.visibility = GONE
//                binding.view19.visibility = GONE
//                binding.view20.visibility = GONE
//            }
//
//            11 -> {
//                binding.view1.visibility = VISIBLE
//                binding.view2.visibility = VISIBLE
//                binding.view3.visibility = VISIBLE
//                binding.view4.visibility = VISIBLE
//                binding.view5.visibility = VISIBLE
//                binding.view6.visibility = VISIBLE
//                binding.view7.visibility = VISIBLE
//                binding.view8.visibility = VISIBLE
//                binding.view9.visibility = VISIBLE
//                binding.view10.visibility = VISIBLE
//                binding.view11.visibility = VISIBLE
//                binding.view12.visibility = GONE
//                binding.view13.visibility = GONE
//                binding.view14.visibility = GONE
//                binding.view15.visibility = GONE
//                binding.view16.visibility = GONE
//                binding.view17.visibility = GONE
//                binding.view18.visibility = GONE
//                binding.view19.visibility = GONE
//                binding.view20.visibility = GONE
//            }
//
//            12 -> {
//                binding.view1.visibility = VISIBLE
//                binding.view2.visibility = VISIBLE
//                binding.view3.visibility = VISIBLE
//                binding.view4.visibility = VISIBLE
//                binding.view5.visibility = VISIBLE
//                binding.view6.visibility = VISIBLE
//                binding.view7.visibility = VISIBLE
//                binding.view8.visibility = VISIBLE
//                binding.view9.visibility = VISIBLE
//                binding.view10.visibility = VISIBLE
//                binding.view11.visibility = VISIBLE
//                binding.view12.visibility = VISIBLE
//                binding.view13.visibility = GONE
//                binding.view14.visibility = GONE
//                binding.view15.visibility = GONE
//                binding.view16.visibility = GONE
//                binding.view17.visibility = GONE
//                binding.view18.visibility = GONE
//                binding.view19.visibility = GONE
//                binding.view20.visibility = GONE
//            }
//
//            13 -> {
//                binding.view1.visibility = VISIBLE
//                binding.view2.visibility = VISIBLE
//                binding.view3.visibility = VISIBLE
//                binding.view4.visibility = VISIBLE
//                binding.view5.visibility = VISIBLE
//                binding.view6.visibility = VISIBLE
//                binding.view7.visibility = VISIBLE
//                binding.view8.visibility = VISIBLE
//                binding.view9.visibility = VISIBLE
//                binding.view10.visibility = VISIBLE
//                binding.view11.visibility = VISIBLE
//                binding.view12.visibility = VISIBLE
//                binding.view13.visibility = VISIBLE
//                binding.view14.visibility = GONE
//                binding.view15.visibility = GONE
//                binding.view16.visibility = GONE
//                binding.view17.visibility = GONE
//                binding.view18.visibility = GONE
//                binding.view19.visibility = GONE
//                binding.view20.visibility = GONE
//            }
//
//            14 -> {
//                binding.view1.visibility = VISIBLE
//                binding.view2.visibility = VISIBLE
//                binding.view3.visibility = VISIBLE
//                binding.view4.visibility = VISIBLE
//                binding.view5.visibility = VISIBLE
//                binding.view6.visibility = VISIBLE
//                binding.view7.visibility = VISIBLE
//                binding.view8.visibility = VISIBLE
//                binding.view9.visibility = VISIBLE
//                binding.view10.visibility = VISIBLE
//                binding.view11.visibility = VISIBLE
//                binding.view12.visibility = VISIBLE
//                binding.view13.visibility = VISIBLE
//                binding.view14.visibility = VISIBLE
//                binding.view15.visibility = GONE
//                binding.view16.visibility = GONE
//                binding.view17.visibility = GONE
//                binding.view18.visibility = GONE
//                binding.view19.visibility = GONE
//                binding.view20.visibility = GONE
//            }
//
//            15 -> {
//                binding.view1.visibility = VISIBLE
//                binding.view2.visibility = VISIBLE
//                binding.view3.visibility = VISIBLE
//                binding.view4.visibility = VISIBLE
//                binding.view5.visibility = VISIBLE
//                binding.view6.visibility = VISIBLE
//                binding.view7.visibility = VISIBLE
//                binding.view8.visibility = VISIBLE
//                binding.view9.visibility = VISIBLE
//                binding.view10.visibility = VISIBLE
//                binding.view11.visibility = VISIBLE
//                binding.view12.visibility = VISIBLE
//                binding.view13.visibility = VISIBLE
//                binding.view14.visibility = VISIBLE
//                binding.view15.visibility = VISIBLE
//                binding.view16.visibility = GONE
//                binding.view17.visibility = GONE
//                binding.view18.visibility = GONE
//                binding.view19.visibility = GONE
//                binding.view20.visibility = GONE
//            }
//
//            16 -> {
//                binding.view1.visibility = VISIBLE
//                binding.view2.visibility = VISIBLE
//                binding.view3.visibility = VISIBLE
//                binding.view4.visibility = VISIBLE
//                binding.view5.visibility = VISIBLE
//                binding.view6.visibility = VISIBLE
//                binding.view7.visibility = VISIBLE
//                binding.view8.visibility = VISIBLE
//                binding.view9.visibility = VISIBLE
//                binding.view10.visibility = VISIBLE
//                binding.view11.visibility = VISIBLE
//                binding.view12.visibility = VISIBLE
//                binding.view13.visibility = VISIBLE
//                binding.view14.visibility = VISIBLE
//                binding.view15.visibility = VISIBLE
//                binding.view16.visibility = VISIBLE
//                binding.view17.visibility = GONE
//                binding.view18.visibility = GONE
//                binding.view19.visibility = GONE
//                binding.view20.visibility = GONE
//            }
//
//            17 -> {
//                binding.view1.visibility = VISIBLE
//                binding.view2.visibility = VISIBLE
//                binding.view3.visibility = VISIBLE
//                binding.view4.visibility = VISIBLE
//                binding.view5.visibility = VISIBLE
//                binding.view6.visibility = VISIBLE
//                binding.view7.visibility = VISIBLE
//                binding.view8.visibility = VISIBLE
//                binding.view9.visibility = VISIBLE
//                binding.view10.visibility = VISIBLE
//                binding.view11.visibility = VISIBLE
//                binding.view12.visibility = VISIBLE
//                binding.view13.visibility = VISIBLE
//                binding.view14.visibility = VISIBLE
//                binding.view15.visibility = VISIBLE
//                binding.view16.visibility = VISIBLE
//                binding.view17.visibility = VISIBLE
//                binding.view18.visibility = GONE
//                binding.view19.visibility = GONE
//                binding.view20.visibility = GONE
//            }
//
//            18 -> {
//                binding.view1.visibility = VISIBLE
//                binding.view2.visibility = VISIBLE
//                binding.view3.visibility = VISIBLE
//                binding.view4.visibility = VISIBLE
//                binding.view5.visibility = VISIBLE
//                binding.view6.visibility = VISIBLE
//                binding.view7.visibility = VISIBLE
//                binding.view8.visibility = VISIBLE
//                binding.view9.visibility = VISIBLE
//                binding.view10.visibility = VISIBLE
//                binding.view11.visibility = VISIBLE
//                binding.view12.visibility = VISIBLE
//                binding.view13.visibility = VISIBLE
//                binding.view14.visibility = VISIBLE
//                binding.view15.visibility = VISIBLE
//                binding.view16.visibility = VISIBLE
//                binding.view17.visibility = VISIBLE
//                binding.view18.visibility = VISIBLE
//                binding.view19.visibility = GONE
//                binding.view20.visibility = GONE
//            }
//
//            19 -> {
//                binding.view1.visibility = VISIBLE
//                binding.view2.visibility = VISIBLE
//                binding.view3.visibility = VISIBLE
//                binding.view4.visibility = VISIBLE
//                binding.view5.visibility = VISIBLE
//                binding.view6.visibility = VISIBLE
//                binding.view7.visibility = VISIBLE
//                binding.view8.visibility = VISIBLE
//                binding.view9.visibility = VISIBLE
//                binding.view10.visibility = VISIBLE
//                binding.view11.visibility = VISIBLE
//                binding.view12.visibility = VISIBLE
//                binding.view13.visibility = VISIBLE
//                binding.view14.visibility = VISIBLE
//                binding.view15.visibility = VISIBLE
//                binding.view16.visibility = VISIBLE
//                binding.view17.visibility = VISIBLE
//                binding.view18.visibility = VISIBLE
//                binding.view19.visibility = VISIBLE
//                binding.view20.visibility = GONE
//            }
//
//            20 -> {
//                binding.view1.visibility = VISIBLE
//                binding.view2.visibility = VISIBLE
//                binding.view3.visibility = VISIBLE
//                binding.view4.visibility = VISIBLE
//                binding.view5.visibility = VISIBLE
//                binding.view6.visibility = VISIBLE
//                binding.view7.visibility = VISIBLE
//                binding.view8.visibility = VISIBLE
//                binding.view9.visibility = VISIBLE
//                binding.view10.visibility = VISIBLE
//                binding.view11.visibility = VISIBLE
//                binding.view12.visibility = VISIBLE
//                binding.view13.visibility = VISIBLE
//                binding.view14.visibility = VISIBLE
//                binding.view15.visibility = VISIBLE
//                binding.view16.visibility = VISIBLE
//                binding.view17.visibility = VISIBLE
//                binding.view18.visibility = VISIBLE
//                binding.view19.visibility = VISIBLE
//                binding.view20.visibility = VISIBLE
//            }
//        }

    }


    var isFromAssets = true
    private fun initPdfViewerWithPath(bindingPdf : DialogPdfBinding, filePath: String?) {
        if (TextUtils.isEmpty(filePath)) {
            onPdfError(bindingPdf, "")
            return
        }
        try {
            val file = if (filePath!!.startsWith("content://")) {
                uriToFile(applicationContext, Uri.parse(filePath))
            } else if (isFromAssets) {
                fileFromAsset(this, filePath)
            } else {
                File(filePath)
            }
            bindingPdf.pdfView.initWithFile(file)
        } catch (e: Exception) {
            onPdfError(bindingPdf, e.toString())
        }


        bindingPdf.pdfView.statusListener = object : PdfRendererView.StatusCallBack {
            override fun onPdfLoadStart() {
                runOnUiThread {
                    true.showProgressBar()
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
                runOnUiThread {
                    false.showProgressBar()
                    //downloadedFilePath = absolutePath
                }
            }

            override fun onError(error: Throwable) {
                runOnUiThread {
                    false.showProgressBar()
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
        AlertDialog.Builder(this)
            .setTitle("pdf_viewer_error")
            .setMessage("error_pdf_corrupted")
            .setPositiveButton("pdf_viewer_retry") { dialog, which ->
                runOnUiThread {
                    initPdfViewerWithPath(bindingPdf, "quote.pdf")
                }
            }
            .setNegativeButton("pdf_viewer_cancel", null)
            .show()
    }

    private fun Boolean.showProgressBar() {
        binding.progressBar.visibility = if (this) VISIBLE else GONE
    }






    private fun initViewPagerView() {
        initializeList()
//        pagerAdapter = ProductDetailPagerAdapter(this, videoList)
        binding.viewPager.offscreenPageLimit = 1
        binding.viewPager.adapter = pagerAdapter
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        Log.e(TAG, "videoList "+videoList.size)

    }

//    private fun setupPermissions() {
//        val permission = ContextCompat.checkSelfPermission(
//            this,
//            Manifest.permission.READ_EXTERNAL_STORAGE
//        )
//
//        if (permission != PackageManager.PERMISSION_GRANTED) {
//            Log.i(TAG, getString(R.string.permission_denied))
//            makeRequest()
//        } else {
//            initViewPagerView()
//        }
//    }
//
//    private fun makeRequest() {
//        ActivityCompat.requestPermissions(
//            this,
//            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
//            RECORD_REQUEST_CODE
//        )
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>, grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            RECORD_REQUEST_CODE -> {
//
//                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(
//                        this,
//                        getString(R.string.request_permission),
//                        Toast.LENGTH_SHORT
//                    ).show()
////                    mainViewBinding.btnReqPermission.visibility = View.VISIBLE
//                } else {
//                    Log.i(TAG, getString(R.string.permission_granted))
////                    mainViewBinding.btnReqPermission.visibility = View.GONE
//                    initViewPagerView()
//                }
//            }
//        }
//    }

    private fun initializeList() {
        videoList = ArrayList()

        videoList.add("https://v2.streetsaarthi.in//uploads//1704703414Vishwakarma%20Scheme.jpeg")
        videoList.add("https://v2.streetsaarthi.in//uploads//1704703414Vishwakarma%20Scheme.jpeg")
        videoList.add("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
        videoList.add("https://v2.streetsaarthi.in//uploads//1704703414Vishwakarma%20Scheme.jpeg")
        videoList.add("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4")
        videoList.add("https://v2.streetsaarthi.in//uploads//1704703414Vishwakarma%20Scheme.jpeg")
        videoList.add("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4")
        videoList.add("https://v2.streetsaarthi.in//uploads//1704703414Vishwakarma%20Scheme.jpeg")
        videoList.add("https://v2.streetsaarthi.in//uploads//1704703414Vishwakarma%20Scheme.jpeg")
    }
}
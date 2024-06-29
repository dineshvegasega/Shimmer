package com.shimmer.store.ui

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.shimmer.store.databinding.AbcBinding
import com.shimmer.store.ui.main.productDetail.ProductDetailPagerAdapter
import com.shimmer.store.utils.pdfviewer.PdfRendererView
import com.shimmer.store.utils.pdfviewer.util.FileUtils.fileFromAsset
import com.shimmer.store.utils.pdfviewer.util.FileUtils.uriToFile
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


        initPdfViewerWithPath("quote.pdf")

    }

    var isFromAssets = true
    private fun initPdfViewerWithPath(filePath: String?) {
        if (TextUtils.isEmpty(filePath)) {
            onPdfError("")
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
            binding.pdfView.initWithFile(file)
        } catch (e: Exception) {
            onPdfError(e.toString())
        }


        binding.pdfView.statusListener = object : PdfRendererView.StatusCallBack {
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
                    onPdfError(error.toString())
                }
            }

            override fun onPageChanged(currentPage: Int, totalPage: Int) {
                //Page change. Not require
            }
        }
    }



    private fun onPdfError(e: String) {
        Log.e("Pdf render error", e)
        AlertDialog.Builder(this)
            .setTitle("pdf_viewer_error")
            .setMessage("error_pdf_corrupted")
            .setPositiveButton("pdf_viewer_retry") { dialog, which ->
                runOnUiThread {
                    initPdfViewerWithPath("quote.pdf")
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
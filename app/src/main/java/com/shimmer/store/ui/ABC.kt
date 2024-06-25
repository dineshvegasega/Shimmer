package com.shimmer.store.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.shimmer.store.databinding.AbcBinding
import com.shimmer.store.ui.main.productDetail.ViewPagerAdapter

class ABC  : AppCompatActivity() {

    private val TAG = "MainActivity"

    lateinit var mainViewBinding: AbcBinding

    private val RECORD_REQUEST_CODE: Int = 101

    lateinit var pagerAdapter: FragmentStateAdapter

    lateinit var videoList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewBinding = AbcBinding.inflate(layoutInflater)
        setContentView(mainViewBinding.root)

        //setupPermissions()

//        mainViewBinding.btnReqPermission.setOnClickListener { makeRequest() }

        initViewPagerView()
    }

    private fun initViewPagerView() {

        initializeList()
        pagerAdapter = ViewPagerAdapter(this, videoList)
        mainViewBinding.viewPager.offscreenPageLimit = 1
        mainViewBinding.viewPager.adapter = pagerAdapter
        mainViewBinding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

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
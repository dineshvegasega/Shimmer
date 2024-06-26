package com.shimmer.store.ui.main.productZoom

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OVER_SCROLL_NEVER
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.shimmer.store.databinding.ProductZoomBinding
import com.shimmer.store.ui.main.productDetail.ViewPagerAdapter
import com.shimmer.store.ui.mainActivity.MainActivity
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.hideValueOff
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.isBackStack
import com.shimmer.store.utils.getRecyclerView
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint
//import com.stfalcon.imageviewer.StfalconImageViewer


@AndroidEntryPoint
class ProductZoom  : Fragment() {
    private var _binding: ProductZoomBinding? = null
    private val binding get() = _binding!!

    companion object {
        @JvmStatic
        lateinit var pagerAdapter: FragmentStateAdapter
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProductZoomBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isBackStack = true
        hideValueOff = 2
        MainActivity.mainActivity.get()!!.callBack(0)

        binding.apply {
            ivIconCross.singleClick {
                findNavController().navigateUp()
            }

            val arrayList = arguments?.getStringArrayList("arrayList")
            val position = arguments?.getInt("position")

            val item1 : ArrayList<String> = ArrayList()

            arrayList?.forEach {
                if (it.endsWith("jpg") || it.endsWith("jpeg") || it.endsWith("png")){
                    item1.add(it)
                }
            }

            Log.e("TAG", "arrayList: $item1")
            Log.e("TAG", "position: $position")

            pagerAdapter = ProductZoomAdapter(requireActivity(), item1!!)
            rvList1.offscreenPageLimit = 1
            rvList1.overScrollMode = OVER_SCROLL_NEVER
            rvList1.adapter = pagerAdapter
            rvList1.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            rvList1.setCurrentItem(position!!, false)

            Log.e("TAG", "videoList "+item1.size)
            (rvList1.getRecyclerView()
                .getItemAnimator() as SimpleItemAnimator).supportsChangeAnimations =
                false


        }

    }
}
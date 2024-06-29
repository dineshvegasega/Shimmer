package com.shimmer.store.ui.main.productDetail

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
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.shimmer.store.R
import com.shimmer.store.databinding.ProductDetailBinding
import com.shimmer.store.models.ItemParcelable
import com.shimmer.store.models.Items
import com.shimmer.store.ui.interfaces.CallBackListener
import com.shimmer.store.ui.main.products.Products
import com.shimmer.store.ui.main.products.Products.Companion
import com.shimmer.store.ui.main.products.ProductsAdapter
import com.shimmer.store.ui.mainActivity.MainActivity
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.hideValueOff
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.isBackStack
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.badgeCount
import com.shimmer.store.utils.getRecyclerView
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetail : Fragment() , CallBackListener {
    private val viewModel: ProductDetailVM by viewModels()
    private var _binding: ProductDetailBinding? = null
    private val binding get() = _binding!!

    private val mediaUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4"
    private val mediaUrlHls = "http://sample.vodobox.net/skate_phantom_flex_4k/skate_phantom_flex_4k.m3u8"

//    private var player: ExoPlayer?= null
    // Create a data source factory.
    private val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()

    companion object {
//        @JvmStatic
//        lateinit var adapter1: ProductDetailAdapter

        @JvmStatic
        lateinit var pagerAdapter: FragmentStateAdapter

        var callBackListener: CallBackListener? = null


        @JvmStatic
        lateinit var adapter2: RelatedProductAdapter


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProductDetailBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callBackListener = this
        isBackStack = true
        hideValueOff = 2
        MainActivity.mainActivity.get()!!.callBack(2)

        binding.apply {

            pagerAdapter = ProductDetailPagerAdapter(requireActivity(), viewModel.item1)
            rvList1.offscreenPageLimit = 1
            rvList1.overScrollMode = OVER_SCROLL_NEVER
            rvList1.adapter = pagerAdapter
            rvList1.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            Log.e("TAG", "videoList "+viewModel.item1.size)
            (rvList1.getRecyclerView()
                .getItemAnimator() as SimpleItemAnimator).supportsChangeAnimations =
                false

            TabLayoutMediator(tabLayout, rvList1) { tab, position ->
            }.attach()



            rvList2.setHasFixedSize(true)
            rvList2.adapter = viewModel.recentAdapter
            viewModel.recentAdapter.notifyDataSetChanged()
            viewModel.recentAdapter.submitList(viewModel.item1)




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
                    menuBadge.text = "$it"
                    menuBadge.visibility = if (it != 0) View.VISIBLE else View.GONE
                }
            }

            btAddToCart.singleClick {
                badgeCount.value = 1
            }

            btByNow.singleClick {
                findNavController().navigate(R.id.action_productDetail_to_cart)
            }

            adapter2 = RelatedProductAdapter()
            adapter2.submitData(viewModel.item1)
            adapter2.notifyDataSetChanged()
            binding.rvRelatedProducts.adapter = adapter2

        }



//
//        player = ExoPlayer.Builder(requireContext())
//            .build()

//        adapter1 = ProductDetailAdapter()
//
//        binding.apply {
//            adapter1.submitData(viewModel.item1)
//            adapter1.notifyDataSetChanged()
//            (rvList1.getRecyclerView()
//                .getItemAnimator() as SimpleItemAnimator).supportsChangeAnimations =
//                false
//
//            binding.rvList1.offscreenPageLimit = 1
//            binding.rvList1.overScrollMode = OVER_SCROLL_NEVER
//            binding.rvList1.adapter = adapter1
//            binding.rvList1.setPageTransformer { page, position ->
//                binding.rvList1.updatePagerHeightForChild(page)
//            }
//            TabLayoutMediator(tabLayout, rvList1) { tab, position ->
//            }.attach()
//
//            var pageChangeValue = -0
//            rvList1.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//                override fun onPageScrolled(
//                    position: Int,
//                    positionOffset: Float,
//                    positionOffsetPixels: Int
//                ) {
//                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
//                    if (pageChangeValue != position) {
//                        Log.e("TAG", "positionA" + position)
//                    }
//                    pageChangeValue = position
//                }
//
//                override fun onPageSelected(position: Int) {
//                    super.onPageSelected(position)
//                    adapter1.updatePosition(position)
//                }
//
//                override fun onPageScrollStateChanged(state: Int) {
//                    super.onPageScrollStateChanged(state)
//                    Log.e("TAG", "state" + state)
////                    if (state == 0) {
//                    adapter1.notifyItemChanged(adapter1.counter)
////                        onClickItem(pageChangeValue)
////                    }
//                }
//            })
//
//
//
//
//
////            val player = ExoPlayer.Builder(requireContext()).build()
////            binding.playerView.player = player
////            val mediaItem = MediaItem.fromUri("yourURL")
////            player.setMediaItem(mediaItem)
////            player.prepare()
////            player.play()
////
//
////            initPlayer()
//        }

    }

    override fun onCallBack(pos: Int) {
        findNavController().navigate(R.id.action_productDetail_to_productZoom, Bundle().apply {
            putParcelable("arrayList", ItemParcelable(viewModel.item1, binding.rvList1.currentItem))
        })
    }

    override fun onCallBackHideShow() {
        TODO("Not yet implemented")
    }


//
//    @OptIn(UnstableApi::class)
//    private fun initPlayer(){
//        player = ExoPlayer.Builder(requireContext())
//            .build()
//            .apply {
//
//                val source = if (mediaUrl.contains("m3u8"))
//                    getHlsMediaSource()
//                else
//                    getProgressiveMediaSource()
//
//                setMediaSource(source)
//                prepare()
//                addListener(playerListener)
//
//
//            }
//
//
//
////        binding.playerView.player.setShowPreviousButton(false);
//    }
//
//
//    @OptIn(UnstableApi::class)
//    private fun getHlsMediaSource(): MediaSource {
//        // Create a HLS media source pointing to a playlist uri.
//        return HlsMediaSource.Factory(dataSourceFactory).
//        createMediaSource(MediaItem.fromUri(mediaUrl))
//    }
//
//    @OptIn(UnstableApi::class)
//    private fun getProgressiveMediaSource(): MediaSource{
//        // Create a Regular media source pointing to a playlist uri.
//        return ProgressiveMediaSource.Factory(dataSourceFactory)
//            .createMediaSource(MediaItem.fromUri(Uri.parse(mediaUrl)))
//    }
//
//    private fun releasePlayer(){
//        player?.apply {
//            playWhenReady = false
//            release()
//        }
//        player = null
//    }
//
//    private fun pause(){
//        player?.playWhenReady = false
//    }
//
//    private fun play(){
//        player?.playWhenReady = true
//    }
//
//    private fun restartPlayer(){
//        player?.seekTo(0)
//        player?.playWhenReady = true
//    }
//
//    private val playerListener = object: Player.Listener {
//        override fun onPlaybackStateChanged(playbackState: Int) {
//            super.onPlaybackStateChanged(playbackState)
//            when(playbackState){
//                STATE_ENDED -> restartPlayer()
//                STATE_READY -> {
//                   // binding.playerView.player = player
//                    play()
//                }
//            }
//        }
//    }
}
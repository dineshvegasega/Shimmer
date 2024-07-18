package com.shimmer.store.ui.main.profileDetail

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.shimmer.store.R
import com.shimmer.store.databinding.ProfileDetailBinding
import com.shimmer.store.datastore.db.CartModel
import com.shimmer.store.ui.mainActivity.MainActivity
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.db
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.hideValueOff
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.isBackStack
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.badgeCount
import com.shimmer.store.utils.mainThread
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileDetail : Fragment() {
    private val viewModel: ProfileDetailVM by viewModels()
    private var _binding: ProfileDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProfileDetailBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isBackStack = false
        hideValueOff = 2
        MainActivity.mainActivity.get()!!.callBack(2)

        binding.apply {
            topBar.apply {
                textViewTitle.visibility = View.VISIBLE
                ivSearch.visibility = View.VISIBLE
                ivCart.visibility = View.VISIBLE
                textViewTitle.text = "Profile Detail"

                appicon.setImageDrawable(
                    ContextCompat.getDrawable(
                        MainActivity.context.get()!!,
                        R.drawable.baseline_west_24
                    )
                )

                appicon.singleClick {
                    findNavController().navigateUp()
                }


                ivSearch.singleClick {
                    findNavController().navigate(R.id.action_profileDetail_to_search)
                }

                ivCart.singleClick {
                    findNavController().navigate(R.id.action_profileDetail_to_cart)
                }


                badgeCount.observe(viewLifecycleOwner) {
                    viewModel.getCartCount(){
                        Log.e("TAG", "count: $this")
                        menuBadge.text = "${this}"
                        menuBadge.visibility = if (this != 0) View.VISIBLE else View.GONE
                    }
//                    mainThread {
//                        val userList: List<CartModel> ?= db?.cartDao()?.getAll()
//                        var countBadge = 0
//                        userList?.forEach {
//                            countBadge += it.quantity
//                        }
//                        menuBadge.text = "${countBadge}"
//                        menuBadge.visibility = if (countBadge != 0) View.VISIBLE else View.GONE
//                    }
                }
            }
        }


    }
}
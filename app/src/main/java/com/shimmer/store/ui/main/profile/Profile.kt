package com.shimmer.store.ui.main.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.shimmer.store.R
import com.shimmer.store.databinding.ProfileBinding
import com.shimmer.store.datastore.DataStoreKeys.LOGIN_DATA
import com.shimmer.store.datastore.DataStoreKeys.CUSTOMER_TOKEN
import com.shimmer.store.datastore.DataStoreUtil.clearDataStore
import com.shimmer.store.datastore.DataStoreUtil.readData
import com.shimmer.store.datastore.DataStoreUtil.removeKey
import com.shimmer.store.datastore.db.CartModel
import com.shimmer.store.ui.mainActivity.MainActivity
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.db
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.hideValueOff
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.isBackStack
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.badgeCount
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.loginType
import com.shimmer.store.utils.mainThread
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MultipartBody
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.shimmer.store.datastore.DataStoreKeys.WEBSITE_ID
import com.shimmer.store.ui.enums.LoginType
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.storeWebUrl


@AndroidEntryPoint
class Profile : Fragment() {
    private val viewModel: ProfileVM by viewModels()
    private var _binding: ProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProfileBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isBackStack = false
        hideValueOff = 2
        MainActivity.mainActivity.get()!!.callBack(1)


        binding.apply {
//            topBar.apply {
//                textViewTitle.visibility = View.VISIBLE
//                ivSearch.visibility = View.VISIBLE
//                ivCart.visibility = View.VISIBLE
//
//                ivSearch.singleClick {
//                    findNavController().navigate(R.id.action_profile_to_search)
//                }
//
//                ivCart.singleClick {
//                    findNavController().navigate(R.id.action_profile_to_cart)
//                }
//
//
//                badgeCount.observe(viewLifecycleOwner) {
//                    viewModel.getCartCount(){
//                        Log.e("TAG", "count: $this")
//                        menuBadge.text = "${this}"
//                        menuBadge.visibility = if (this != 0) View.VISIBLE else View.GONE
//                    }
////                    mainThread {
////                        val userList: List<CartModel> ?= db?.cartDao()?.getAll()
////                        var countBadge = 0
////                        userList?.forEach {
////                            countBadge += it.quantity
////                        }
////                        menuBadge.text = "${countBadge}"
////                        menuBadge.visibility = if (countBadge != 0) View.VISIBLE else View.GONE
////                    }
//                }
//            }


            when(loginType){
                LoginType.VENDOR ->  {
                    groupVendor.visibility = View.VISIBLE
                    groupGuest.visibility = View.GONE
                }
                LoginType.CUSTOMER ->  {
                    groupVendor.visibility = View.GONE
                    groupGuest.visibility = View.VISIBLE
                }
            }


//            topBarSearch.apply {
//                appicon.visibility = View.GONE
//                editTextSearch.hint = "Search For Nearest Franchise"
//                editTextSearch.isFocusable = false
//            }

            topBarOthers.apply {
                ivSearch.singleClick {
                    findNavController().navigate(R.id.action_profile_to_search)
                }

                ivCart.singleClick {
                    findNavController().navigate(R.id.action_profile_to_cart)
                }
            }


            btProfileDetails.singleClick {
                findNavController().navigate(R.id.action_profile_to_profileDetails)
            }

            btOrders.singleClick {
                findNavController().navigate(R.id.action_profile_to_trackOrder)
            }

            btComplaints.singleClick {
                findNavController().navigate(R.id.action_profile_to_complaintFeedback)
            }

            btChangePassword.singleClick {
                findNavController().navigate(R.id.action_profile_to_ChangePassword)
            }

            switchNotifications.setOnCheckedChangeListener { _, isChecked ->
                Log.e("TAG", "onViewCreated: $isChecked")
            }

            btSearchNearFranchise.singleClick {
                findNavController().navigate(R.id.action_profile_to_nearetFranchise)
            }

            btSearchOrder.singleClick {
                findNavController().navigate(R.id.action_profile_to_searchOrder)
            }


            textLogout.singleClick {
               MaterialAlertDialogBuilder(requireContext(), R.style.LogoutDialogTheme)
                    .setTitle(resources.getString(R.string.app_name))
                    .setMessage(resources.getString(R.string.are_your_sure_want_to_logout))
                    .setPositiveButton(resources.getString(R.string.yes)) { dialog, _ ->
                        dialog.dismiss()
                        removeKey(LOGIN_DATA) {}
                        removeKey(CUSTOMER_TOKEN) {}
                        removeKey(WEBSITE_ID) {}
                        clearDataStore { }
                        findNavController().navigate(R.id.action_profile_to_loginOptions)
                    }
                    .setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setCancelable(false)
                    .show()
            }

        }
    }
}
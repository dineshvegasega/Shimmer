package com.klifora.franchise.ui.main.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.klifora.franchise.R
import com.klifora.franchise.databinding.ProfileBinding
import com.klifora.franchise.datastore.DataStoreKeys.LOGIN_DATA
import com.klifora.franchise.datastore.DataStoreKeys.CUSTOMER_TOKEN
import com.klifora.franchise.datastore.DataStoreUtil.clearDataStore
import com.klifora.franchise.datastore.DataStoreUtil.removeKey
import com.klifora.franchise.ui.mainActivity.MainActivity
import com.klifora.franchise.ui.mainActivity.MainActivity.Companion.hideValueOff
import com.klifora.franchise.ui.mainActivity.MainActivity.Companion.isBackStack
import com.klifora.franchise.ui.mainActivity.MainActivityVM.Companion.loginType
import com.klifora.franchise.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint
import com.klifora.franchise.datastore.DataStoreKeys.WEBSITE_ID
import com.klifora.franchise.ui.enums.LoginType
import com.klifora.franchise.ui.mainActivity.MainActivityVM.Companion.cartItemCount
import com.klifora.franchise.ui.mainActivity.MainActivityVM.Companion.cartItemLiveData


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
        MainActivity.mainActivity.get()!!.callCartApi()

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
                    textLogout.text = getString(R.string.logout)
                }
                LoginType.CUSTOMER ->  {
                    groupVendor.visibility = View.GONE
                    groupGuest.visibility = View.VISIBLE
                    textLogout.text = getString(R.string.login)
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


            cartItemLiveData.value = false
            cartItemLiveData.observe(viewLifecycleOwner) {
                topBarOthers.menuBadge.text = "$cartItemCount"
                topBarOthers.menuBadge.visibility = if (cartItemCount != 0) View.VISIBLE else View.GONE
            }

            val manager = requireContext().packageManager
            val info = manager?.getPackageInfo(requireContext().packageName, 0)
            val versionName = info?.versionName
            textAppVersionTxt.text = getString(R.string.app_version_1_0, versionName)


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
                findNavController().navigate(R.id.action_profile_to_nearestFranchise)
            }

            btSearchOrder.singleClick {
                findNavController().navigate(R.id.action_profile_to_searchOrder)
            }


            textLogout.singleClick {
                when(loginType){
                    LoginType.VENDOR ->  {
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
                    LoginType.CUSTOMER ->  {
                        removeKey(LOGIN_DATA) {}
                        removeKey(CUSTOMER_TOKEN) {}
                        removeKey(WEBSITE_ID) {}
                        clearDataStore { }
                        findNavController().navigate(R.id.action_profile_to_loginOptions)
                    }
                }

            }

        }
    }
}
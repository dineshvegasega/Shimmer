package com.shimmer.store.ui.onBoarding.loginOptions

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.shimmer.store.R
import com.shimmer.store.databinding.LoginOptionsBinding
import com.shimmer.store.datastore.DataStoreKeys.ADMIN_TOKEN
import com.shimmer.store.datastore.DataStoreKeys.WEBSITE_ID
import com.shimmer.store.datastore.DataStoreUtil.readData
import com.shimmer.store.datastore.DataStoreUtil.saveData
import com.shimmer.store.ui.enums.LoginType
import com.shimmer.store.ui.mainActivity.MainActivity
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.loginType
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.storeWebUrl
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class LoginOptions : Fragment() {
    private var _binding: LoginOptionsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginOptionsVM by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LoginOptionsBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MainActivity.mainActivity.get()!!.callBack(0)
        binding.apply {

            layoutFranchise.singleClick {
                findNavController().navigate(R.id.action_loginOptions_to_login)
            }

            layoutUser.singleClick {
                val obj: JSONObject = JSONObject().apply {
                    put("username", "admin")
                    put("password", "admin123")
                }
                viewModel.adminToken(obj) {
                    Log.e("TAG", "ADMIN_TOKENBBBB " + this)
                    saveData(ADMIN_TOKEN, this)
                    loginType = LoginType.CUSTOMER
                    readData(WEBSITE_ID) {
                        storeWebUrl = if (it != null) it.toString() else ""
                        findNavController().navigate(R.id.action_loginOptions_to_home)
                    }
                }
            }
        }
    }
}
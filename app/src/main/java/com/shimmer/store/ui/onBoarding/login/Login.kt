package com.shimmer.store.ui.onBoarding.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.shimmer.store.databinding.LoginBinding
import com.shimmer.store.datastore.DataStoreKeys.ADMIN_TOKEN
import com.shimmer.store.datastore.DataStoreKeys.LOGIN_DATA
import com.shimmer.store.datastore.DataStoreKeys.STORE_DETAIL
import com.shimmer.store.datastore.DataStoreUtil.readData
import com.shimmer.store.datastore.DataStoreUtil.saveData
import com.shimmer.store.datastore.DataStoreUtil.saveObject
import com.shimmer.store.ui.mainActivity.MainActivity
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class Login : Fragment() {
    private val viewModel: LoginVM by viewModels()
    private var _binding: LoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LoginBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MainActivity.mainActivity.get()!!.callBack(0)

        val obj: JSONObject = JSONObject().apply {
            put("username", "vegasega@gmail.com")
            put("password", "ZvdZIC0R")
        }


        binding.apply {
            textHeaderTxt4.singleClick {
                readData(ADMIN_TOKEN) {
                    viewModel.websiteUrl(it.toString(), obj, view){
                        Log.e("TAG", "itAAA "+this)
                    }
//
//                    viewModel.customerLoginToken(it.toString(), obj, view){
//                        Log.e("TAG", "itAAA "+this)
//                    }
                }
            }
        }

    }
}
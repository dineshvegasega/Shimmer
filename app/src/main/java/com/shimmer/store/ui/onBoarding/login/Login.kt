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
import com.shimmer.store.datastore.DataStoreKeys.LOGIN_DATA
import com.shimmer.store.datastore.DataStoreUtil.saveObject
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

        saveObject(LOGIN_DATA, "data")
//
//        var token = arguments?.getString("token")
//        val obj: JSONObject = JSONObject().apply {
//            put("username", "admin")
//            put("password", "admin123")
//        }

//        viewModel.adminToken(obj){
//            val token = this.replace("\"", "")
//            Log.e("TAG", "itAAA "+token)
//            handleSplashTime(token)
//        }


    }
}
package com.shimmer.store.ui.onBoarding.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.shimmer.store.R
import com.shimmer.store.databinding.LoginBinding
import com.shimmer.store.datastore.DataStoreKeys.ADMIN_TOKEN
import com.shimmer.store.datastore.DataStoreKeys.CUSTOMER_TOKEN
import com.shimmer.store.datastore.DataStoreKeys.LOGIN_DATA
import com.shimmer.store.datastore.DataStoreKeys.QUOTE_ID
import com.shimmer.store.datastore.DataStoreKeys.WEBSITE_ID
import com.shimmer.store.datastore.DataStoreUtil.readData
import com.shimmer.store.datastore.DataStoreUtil.saveData
import com.shimmer.store.datastore.DataStoreUtil.saveObject
import com.shimmer.store.models.ItemStore
import com.shimmer.store.ui.enums.LoginType
import com.shimmer.store.ui.mainActivity.MainActivity
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.loginType
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.storeWebUrl
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




        binding.apply {

            includeBackButton.apply {
                layoutBack.singleClick {
                    findNavController().navigateUp()
                }
            }


            val customerJSON: JSONObject = JSONObject().apply {
                put("username", editTextMobileNumber.text.toString())
                put("password", editTextPassword.text.toString())
            }
            btLogin.singleClick {
                val adminJSON: JSONObject = JSONObject().apply {
                    put("username", "admin")
                    put("password", "admin123")
                }
//                viewModel.adminToken(obj) {
//                    val adminToken = this
//                    Log.e("TAG", "ADMIN_TOKENAAAA: " + adminToken)
//                    saveData(ADMIN_TOKEN, adminToken)
//
//                    readData(ADMIN_TOKEN) {
//                        viewModel.websiteUrl(it.toString(), obj, view){
//                            Log.e("TAG", "itAAA "+this)
//                        }
////                    viewModel.customerLoginToken(it.toString(), obj, view){
////                        Log.e("TAG", "itAAA "+this)
////                    }
//                    }
//                }

                viewModel.adminToken(adminJSON) {
                    val adminToken = this
                    saveData(ADMIN_TOKEN, adminToken)
                    Log.e("TAG", "ADMIN_TOKENAAAA: " + adminToken)
                    readData(ADMIN_TOKEN) {
                        viewModel.websiteUrl(it.toString(), customerJSON){
                            Log.e("TAG", "itAAA "+this)
                            val website_id = JSONObject(this).getString("website_id")
                            saveData(WEBSITE_ID, website_id)
                            storeWebUrl = website_id
                            viewModel.customerLoginToken(it.toString(), customerJSON){
                                val storeToken = this
                                Log.e("TAG", "itBBB "+storeToken)
                                viewModel.customerDetail(storeToken){
                                    Log.e("TAG", "itCCC "+this)
                                    saveData(CUSTOMER_TOKEN, storeToken)
                                    saveObject(
                                        LOGIN_DATA,
                                        Gson().fromJson(
                                            this,
                                            ItemStore::class.java
                                        )
                                    )
                                    viewModel.getQuoteId(storeToken, JSONObject()) {
                                        val quoteId = this
                                        saveData(QUOTE_ID, quoteId)
                                        loginType = LoginType.VENDOR
                                        findNavController().navigate(R.id.action_login_to_home)
                                    }
                                }
                            }
//                        }
                        }
//                    viewModel.customerLoginToken(it.toString(), obj, view){
//                        Log.e("TAG", "itAAA "+this)
//                    }
                    }
                }

            }



            textTitleForgotPassword.singleClick {
                findNavController().navigate(R.id.action_login_to_forgotPassword)
            }
        }

    }
}
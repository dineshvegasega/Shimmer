package com.klifora.franchise.ui.onBoarding.login

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.klifora.franchise.R
import com.klifora.franchise.databinding.LoginBinding
import com.klifora.franchise.datastore.DataStoreKeys.ADMIN_TOKEN
import com.klifora.franchise.datastore.DataStoreKeys.CUSTOMER_TOKEN
import com.klifora.franchise.datastore.DataStoreKeys.LOGIN_DATA
import com.klifora.franchise.datastore.DataStoreKeys.MOBILE_NUMBER
import com.klifora.franchise.datastore.DataStoreKeys.QUOTE_ID
import com.klifora.franchise.datastore.DataStoreKeys.WEBSITE_ID
import com.klifora.franchise.datastore.DataStoreUtil.readData
import com.klifora.franchise.datastore.DataStoreUtil.saveData
import com.klifora.franchise.datastore.DataStoreUtil.saveObject
import com.klifora.franchise.models.user.ItemUserItem
import com.klifora.franchise.networking.Repository
import com.klifora.franchise.networking.password
import com.klifora.franchise.networking.username
import com.klifora.franchise.ui.enums.LoginType
import com.klifora.franchise.ui.mainActivity.MainActivity
import com.klifora.franchise.ui.mainActivity.MainActivityVM.Companion.loginType
import com.klifora.franchise.ui.mainActivity.MainActivityVM.Companion.storeWebUrl
import com.klifora.franchise.utils.isValidPassword
import com.klifora.franchise.utils.showSnackBar
import com.klifora.franchise.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import javax.inject.Inject

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



            var counter = 0
            var start: Int
            var end: Int
            imgCreatePassword.singleClick {
                if(counter == 0){
                    counter = 1
                    imgCreatePassword.setImageResource(R.drawable.ic_eye_open)
                    start=editTextPassword.getSelectionStart()
                    end=editTextPassword.getSelectionEnd()
                    editTextPassword.setTransformationMethod(null)
                    editTextPassword.setSelection(start,end)
                }else{
                    counter = 0
                    imgCreatePassword.setImageResource(R.drawable.ic_eye_closed)
                    start=editTextPassword.getSelectionStart()
                    end=editTextPassword.getSelectionEnd()
                    editTextPassword.setTransformationMethod(PasswordTransformationMethod())
                    editTextPassword.setSelection(start,end)
                }
            }



            editTextPassword.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                @SuppressLint("SuspiciousIndentation")
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (editTextMobileNumber.text.toString().length == 10 && editTextPassword.text.toString().length >= 8){
                        btLogin.isEnabled = true
                        btLogin.backgroundTintList =
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color._E2B476
                                )
                            )
                    } else {
                        btLogin.isEnabled = false
                        btLogin.backgroundTintList =
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color._C08D54
                                )
                            )
                    }
                }
            })


            btLogin.singleClick {
                if (editTextMobileNumber.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.EnterValidMobileNumber))
                } else if (editTextMobileNumber.text.toString().length != 10){
                    showSnackBar(getString(R.string.EnterValidMobileNumber))
                } else if (editTextMobileNumber.text.toString().startsWith("0")){
                    showSnackBar(getString(R.string.EnterValidMobileNumber))
                } else if (binding.editTextPassword.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.EnterPassword))
//                } else if(binding.editTextPassword.text.toString().length >= 0 && binding.editTextPassword.text.toString().length < 8){
//                    showSnackBar(getString(R.string.InvalidPassword))
//                } else if(!isValidPassword(editTextPassword.text.toString().trim())){
//                    showSnackBar(getString(R.string.InvalidPassword))
//                } else if(editTextPassword.text.toString().contains(" ")){
//                    showSnackBar(getString(R.string.InvalidPassword))
                } else {
                    Log.e("TAG", "XXXXXXXX")
                    val customerJSON: JSONObject = JSONObject().apply {
                        put("emailmobile", editTextMobileNumber.text.toString())
                        put("mobpassword", editTextPassword.text.toString())
                    }

                    val adminJSON: JSONObject = JSONObject().apply {
                        put("username", username)
                        put("password", password)
                    }

//                        mainThread {
                            viewModel.show()
//                        }
                        viewModel.adminToken(adminJSON) {
                            val adminToken = this
                            saveData(ADMIN_TOKEN, adminToken)
                            Log.e("TAG", "ADMIN_TOKENAAAA: " + adminToken)
                            readData(ADMIN_TOKEN) {
                                viewModel.websiteUrl(it.toString(), customerJSON){
                                    Log.e("TAG", "itAAA "+this)
//                                    val website_id = JSONObject(this).getString("website_id")
                                    saveData(WEBSITE_ID, this.website_id)
                                    storeWebUrl = this.website_id
                                    viewModel.customerLoginToken(it.toString(), customerJSON){
                                        val storeToken = this
                                        Log.e("TAG", "itBBB "+storeToken)
                                        viewModel.customerDetail(storeToken){
                                            Log.e("TAG", "itCCC "+this)
                                            saveData(CUSTOMER_TOKEN, storeToken)
                                            saveObject(
                                                LOGIN_DATA,
                                                Gson().fromJson(this,
                                                    ItemUserItem::class.java
                                                )
                                            )
                                            viewModel.getQuoteId(storeToken, JSONObject()) {
//                                                mainThread {
                                                    viewModel.hide()
//                                                }
                                                val quoteId = this
                                                saveData(QUOTE_ID, quoteId)
                                                loginType = LoginType.VENDOR
                                                saveData(MOBILE_NUMBER, editTextMobileNumber.text.toString())
                                                findNavController().navigate(R.id.action_login_to_home)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
//                }
            }



            textTitleForgotPassword.singleClick {
                findNavController().navigate(R.id.action_login_to_forgotPassword)
            }
        }

    }
}
package com.shimmer.store.ui.main.changePassword

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.shimmer.store.R
import com.shimmer.store.databinding.ChangePasswordBinding
import com.shimmer.store.datastore.db.CartModel
import com.shimmer.store.ui.main.faq.FaqVM
import com.shimmer.store.ui.mainActivity.MainActivity
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.db
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.hideValueOff
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.isBackStack
import com.shimmer.store.utils.isValidPassword
import com.shimmer.store.utils.mainThread
import com.shimmer.store.utils.showSnackBar
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class ChangePassword : Fragment() {
    private val viewModel: FaqVM by viewModels()
    private var _binding: ChangePasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ChangePasswordBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isBackStack = false
        hideValueOff = 2
        MainActivity.mainActivity.get()!!.callBack(2)

        binding.apply {
            topBarBack.includeBackButton.apply {
                layoutBack.singleClick {
                    findNavController().navigateUp()
                }
                topBarBack.ivCartLayout.visibility = View.GONE
                topBarBack.ivCart.singleClick {
                    findNavController().navigate(R.id.action_changePassword_to_cart)
                }
            }

//            topBar.apply {
//                textViewTitle.visibility = View.VISIBLE
//                ivSearch.visibility = View.VISIBLE
//                ivCart.visibility = View.VISIBLE
//                textViewTitle.text = "Change Password"
//
//                appicon.setImageDrawable(
//                    ContextCompat.getDrawable(
//                        MainActivity.context.get()!!,
//                        R.drawable.baseline_west_24
//                    )
//                )
//
//                appicon.singleClick {
//                    findNavController().navigateUp()
//                }
//
//
//                ivSearch.singleClick {
//                    findNavController().navigate(R.id.action_changePassword_to_search)
//                }
//
//                ivCart.singleClick {
//                    findNavController().navigate(R.id.action_changePassword_to_cart)
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

            editTextConfirmNewPassword.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                @SuppressLint("SuspiciousIndentation")
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if(!isValidPassword(editTextNewPassword.text.toString().trim())){
                        btLogin.isEnabled = false
                        btLogin.backgroundTintList =
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color._00b3b0
                                )
                            )
                    } else if(!isValidPassword(editTextConfirmNewPassword.text.toString().trim())){
                        btLogin.isEnabled = false
                        btLogin.backgroundTintList =
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color._00b3b0
                                )
                            )
                    } else if(editTextNewPassword.text.toString() == editTextConfirmNewPassword.text.toString()){
                        Log.e("TAG", "onTextChanged")
                        btLogin.isEnabled = true
                        btLogin.backgroundTintList =
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color._07FFFC
                                )
                            )
                    } else {
                        btLogin.isEnabled = false
                        btLogin.backgroundTintList =
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color._00b3b0
                                )
                            )
                    }
                }
            })


            btLogin.singleClick {
                if (binding.editTextOldPassword.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.EnterOldPassword))
                } else if(binding.editTextOldPassword.text.toString().length >= 0 && binding.editTextOldPassword.text.toString().length < 8){
                    showSnackBar(getString(R.string.InvalidPassword))
                } else if(!isValidPassword(editTextOldPassword.text.toString().trim())){
                    showSnackBar(getString(R.string.InvalidPassword))
                } else if (binding.editTextNewPassword.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.EnterNewPassword))
                } else if(binding.editTextNewPassword.text.toString().length >= 0 && binding.editTextNewPassword.text.toString().length < 8){
                    showSnackBar(getString(R.string.InvalidPassword))
                } else if(!isValidPassword(editTextNewPassword.text.toString().trim())){
                    showSnackBar(getString(R.string.InvalidPassword))
                } else if (binding.editTextConfirmNewPassword.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.EnterConfirmNewPassword))
                } else if(binding.editTextConfirmNewPassword.text.toString().length >= 0 && binding.editTextConfirmNewPassword.text.toString().length < 8){
                    showSnackBar(getString(R.string.InvalidPassword))
                } else if(!isValidPassword(editTextConfirmNewPassword.text.toString().trim())){
                    showSnackBar(getString(R.string.InvalidPassword))
                } else if(editTextNewPassword.text.toString() != editTextConfirmNewPassword.text.toString()){
                    showSnackBar(getString(R.string.CreatePasswordReEnterPasswordisnotsame))
                } else {
                    Log.e("TAG", "count: $this")
//                    val obj: JSONObject = JSONObject().apply {
//                        put(mobile_number, binding.editTextMobileNumber.text.toString())
//                        put(password, binding.editTextPassword.text.toString())
//                    }
//                    if(networkFailed) {
//                        viewModel.login(view = requireView(), obj)
//                    } else {
//                        requireContext().callNetworkDialog()
//                    }
                }
            }

        }
    }
}
package com.shimmer.store.ui.onBoarding.resetPassword

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
import com.shimmer.store.databinding.ResetPasswordBinding
import com.shimmer.store.ui.mainActivity.MainActivity
import com.shimmer.store.utils.isValidPassword
import com.shimmer.store.utils.showSnackBar
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetPassword : Fragment() {
    private val viewModel: ResetPasswordVM by viewModels()
    private var _binding: ResetPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ResetPasswordBinding.inflate(inflater)
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
                if (binding.editTextNewPassword.text.toString().isEmpty()){
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
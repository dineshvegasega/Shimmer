package com.shimmer.store.ui.onBoarding.forgotPassword

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
import com.shimmer.store.databinding.ForgotPasswordBinding
import com.shimmer.store.datastore.DataStoreKeys.LOGIN_DATA
import com.shimmer.store.datastore.DataStoreUtil.saveObject
import com.shimmer.store.ui.mainActivity.MainActivity
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPassword : Fragment() {
    private val viewModel: ForgotPasswordVM by viewModels()
    private var _binding: ForgotPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ForgotPasswordBinding.inflate(inflater)
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

            btResetPassword.singleClick {
                findNavController().navigate(R.id.action_forgotPassword_to_resetPassword)
            }




            editTextMobileNumber.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                @SuppressLint("SuspiciousIndentation")
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (editTextMobileNumber.text.toString().length == 10){
                        textTitleRequestOTP.isEnabled = true
                        textTitleRequestOTP.setTextColor(ContextCompat.getColor(binding.root.context, R.color._0098BD))
                    } else {
                        textTitleRequestOTP.isEnabled = false
                        textTitleRequestOTP.setTextColor(ContextCompat.getColor(binding.root.context, R.color._007a99))
                    }
                }
            })


            textTitleRequestOTP.singleClick {
                Log.e("TAG", "count: $this")
            }




            editTextPassword.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                @SuppressLint("SuspiciousIndentation")
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (editTextPassword.text.toString().length == 6){
                        btResetPassword.isEnabled = true
                        btResetPassword.backgroundTintList =
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color._07FFFC
                                )
                            )
                    } else {
                        btResetPassword.isEnabled = false
                        btResetPassword.backgroundTintList =
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color._00b3b0
                                )
                            )
                    }
                }
            })

        }

    }
}
package com.shimmer.store.ui.main.selectFranchise

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.shimmer.store.R
import com.shimmer.store.databinding.SelectFranchiseBinding
import com.shimmer.store.models.user.ItemUserItem
import com.shimmer.store.ui.enums.LoginType
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.loginType
import com.shimmer.store.utils.showSnackBar
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SelectFranchise : Fragment() {
    private val viewModel: SelectFranchiseVM by viewModels()
    private var _binding: SelectFranchiseBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SelectFranchiseBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            topBarBack.includeBackButton.apply {
                layoutBack.singleClick {
                    findNavController().navigateUp()
                }
            }

            topBarBack.ivCartLayout.visibility = View.GONE
            
//            topBarSearch.apply {
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
////                viewModel.searchValue.observe(viewLifecycleOwner) {
////                    binding.topBarSearch.editTextSearch.setText(it)
////                }
////
////                viewModel.searchDelete.observe(viewLifecycleOwner) {
////                    if (it) {
////                        openSearchHistory()
////                    }
////                }
////                editTextSearch.singleClick {
////                    openSearchHistory()
////                }
//
////                editTextSearch.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
////                    if (hasFocus) {
////                        openSearchHistory()
////                    }
////                })
//            }

//            val iconTypeSearch = when (resources.getInteger(R.integer.layout_value)){
//                1 -> R.drawable.baseline_search_24
//                2 -> R.drawable.baseline_search_32
//                3 -> R.drawable.baseline_search_50
//                else -> R.drawable.baseline_search_24
//            }
//            Log.e("TAG", "onViewCreated: "+resources.getInteger(R.integer.layout_value))
//            topBarSearch.editTextSearch.setCompoundDrawablesWithIntrinsicBounds(0, 0, iconTypeSearch, 0)

            ivEditSearch.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (ivEditSearch.text.toString().isEmpty()){
                        showSnackBar("Enter Franchise Code")
                    } else {
                        viewModel.customerDetail(ivEditSearch.text.toString()){
                            Log.e("TAG", "itCCC "+this)
                            if (this == "false"){
                                groupVendor.visibility = View.GONE
                                viewModel.selectedPosition = -1
                            } else {
                                val data = Gson().fromJson(this,
                                    ItemUserItem::class.java
                                )
                                textFNTxt.text = "Name : "+data.contact_person
                                textCompanyNameTxt.text = "Franchise Name : "+data.name
                                textMobileTxt.text = "Mobile No : "+data.mobile_number
                                textAdrressTxt.text = "Address : "+data.register_address
                                textCityTxt.text = "City : "+data.d_city
                                textStateTxt.text = "State : "+data.d_state
                                textPinCodeTxt.text = "Pincode : "+data.d_pincode
                                groupVendor.visibility = View.VISIBLE
                                viewModel.selectedPosition = 1
                            }
                        }
                    }
                }
                true
            }





//            viewModel.franchiseList(){
//                val fList = this
//                rvList.setHasFixedSize(true)
//                rvList.adapter = viewModel.franchiseListAdapter
//                viewModel.franchiseListAdapter.notifyDataSetChanged()
//                viewModel.franchiseListAdapter.submitList(fList)
//                rvList.visibility = View.VISIBLE
//
//                ivEditSearch.addTextChangedListener(object : TextWatcher {
//                    override fun afterTextChanged(s: Editable?) {
//                    }
//
//                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                    }
//
//                    @SuppressLint("SuspiciousIndentation")
//                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                        val filteredNot = fList.filter { it.name.lowercase().contains(ivEditSearch.text.toString().lowercase()) }
//                        Log.e("TAG", "filteredNot "+filteredNot.toString())
//                        viewModel.franchiseListAdapter.submitList(filteredNot)
//                        viewModel.franchiseListAdapter.notifyDataSetChanged()
//                    }
//                })
//
//
//            }


            layoutSort.singleClick {
                when(loginType){
                    LoginType.VENDOR ->  {
//                        findNavController().navigate(R.id.action_orderSummary_to_home)
                    }
                    LoginType.CUSTOMER ->  {
//                        if(viewModel.selectedPosition != -1){
                            findNavController().navigate(R.id.action_selectFranchise_to_thankyou)
//                        }
                    }
                }
            }

        }



    }
}
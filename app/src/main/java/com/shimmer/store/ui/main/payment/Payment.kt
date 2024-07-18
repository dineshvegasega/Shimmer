package com.shimmer.store.ui.main.payment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.shimmer.store.R
import com.shimmer.store.databinding.PaymentBinding
import com.shimmer.store.datastore.db.CartModel
import com.shimmer.store.ui.mainActivity.MainActivity
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.db
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.isBackStack
import com.shimmer.store.utils.mainThread
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Payment : Fragment() {
    private val viewModel: PaymentVM by viewModels()
    private var _binding: PaymentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PaymentBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged", "ClickableViewAccessibility", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isBackStack = true



        binding.apply {
            topBar.apply {
                textViewTitle.visibility = View.VISIBLE
//                cardSearch.visibility = View.GONE
                ivSearch.visibility = View.GONE
                ivCartLayout.visibility = View.GONE
                textViewTitle.text = "YOUR ORDER"

                appicon.setImageDrawable(
                    ContextCompat.getDrawable(
                        MainActivity.context.get()!!,
                        R.drawable.baseline_west_24
                    )
                )

                appicon.singleClick {
                    findNavController().navigateUp()
                }
            }


            mainThread {
                val userList: List<CartModel>? = db?.cartDao()?.getAll()
                binding.apply {
                    rvList.setHasFixedSize(true)
                    rvList.adapter = viewModel.ordersAdapter
                    viewModel.ordersAdapter.notifyDataSetChanged()
                    viewModel.ordersAdapter.submitList(userList)
                }

                userList?.forEach {
                    Log.e(
                        "TAG",
                        "onViewCreated: " + it.name + " it.currentTime " + it.currentTime
                    )
                }
            }

//            rvList.setHasFixedSize(true)
//            rvList.adapter = viewModel.ordersAdapter
//            viewModel.ordersAdapter.notifyDataSetChanged()
//            viewModel.ordersAdapter.submitList(viewModel.item1)

            layoutSort.singleClick {
                findNavController().navigate(R.id.action_payment_to_home)
            }
        }
    }
}
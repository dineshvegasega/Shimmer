package com.shimmer.store.ui.main.complaintFeedback

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.shimmer.store.databinding.ComplaintFeedbackBinding
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.isBackStack
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComplaintFeedback : Fragment() {
    private val viewModel: ComplaintFeedbackVM by viewModels()
    private var _binding: ComplaintFeedbackBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ComplaintFeedbackBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isBackStack = true
    }
}
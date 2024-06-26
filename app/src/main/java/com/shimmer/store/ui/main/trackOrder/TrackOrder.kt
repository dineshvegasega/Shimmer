package com.shimmer.store.ui.main.trackOrder

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.shimmer.store.databinding.TrackOrderBinding
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.isBackStack
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrackOrder : Fragment() {
    private val viewModel: TrackOrderVM by viewModels()
    private var _binding: TrackOrderBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TrackOrderBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isBackStack = true
    }
}
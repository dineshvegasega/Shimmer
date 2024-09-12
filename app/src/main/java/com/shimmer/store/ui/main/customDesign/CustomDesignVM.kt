package com.shimmer.store.ui.main.customDesign

import androidx.lifecycle.ViewModel
import com.shimmer.store.networking.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CustomDesignVM @Inject constructor(private val repository: Repository) : ViewModel() {
}
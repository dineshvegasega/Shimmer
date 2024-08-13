package com.shimmer.store.ui.onBoarding.resetPassword

import androidx.lifecycle.ViewModel
import com.shimmer.store.networking.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ResetPasswordVM @Inject constructor(private val repository: Repository) : ViewModel() {
}
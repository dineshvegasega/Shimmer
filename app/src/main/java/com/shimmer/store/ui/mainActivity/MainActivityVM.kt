package com.shimmer.store.ui.mainActivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MainActivityVM @Inject constructor() : ViewModel() {
    companion object {
        @JvmStatic
        var locale: Locale = Locale.getDefault()

//        var isHide: Boolean = false
//        var isHide = MutableLiveData<Boolean>()

        var badgeCount = MutableLiveData<Int>(6)

//        val filters = hashMapOf<String, Any?>()

        val arrayPrice : ArrayList<String> = ArrayList()
        val arrayCategory : ArrayList<String> = ArrayList()
        val arrayMaterial : ArrayList<String> = ArrayList()

    }



}
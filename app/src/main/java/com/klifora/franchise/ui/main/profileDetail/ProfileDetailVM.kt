package com.klifora.franchise.ui.main.profileDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klifora.franchise.datastore.db.CartModel
import com.klifora.franchise.ui.mainActivity.MainActivity.Companion.db
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileDetailVM @Inject constructor() : ViewModel() {

    fun getCartCount(callBack: Int.() -> Unit){
        viewModelScope.launch {
            val userList: List<CartModel> ?= db?.cartDao()?.getAll()
            var countBadge = 0
            userList?.forEach {
                countBadge += it.quantity
            }
            callBack(countBadge)
        }
    }

}
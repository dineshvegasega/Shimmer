package com.klifora.franchise.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ItemFaq(
    val que: String = "Item Name",
    val ans: String = "Item Name"
): Parcelable {
    override fun hashCode(): Int {
        val result = que.hashCode()
        return result
    }
}
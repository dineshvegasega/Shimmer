package com.klifora.franchise.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ItemFaq(
    val que: String = "Item Name",
    val ans: String = "Item Name"
): Parcelable
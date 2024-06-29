package com.shimmer.store.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Items(
    val name: String = "Item Name",
    var isSelected: Boolean = false,
    var quantity: Int = 0
): Parcelable
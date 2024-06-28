package com.shimmer.store.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ItemParcelable(
    val item: ArrayList<Items> = ArrayList(),
    var position: Int = -1
): Parcelable

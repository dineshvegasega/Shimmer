package com.shimmer.store.models.orderHistory
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Filter(
    val condition_type: String,
    val `field`: String,
    val value: String
): Parcelable
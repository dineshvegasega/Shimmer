package com.shimmer.store.models.orderHistory
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Shipping(
    val address: Address,
    val method: String,
    val total: Total
): Parcelable
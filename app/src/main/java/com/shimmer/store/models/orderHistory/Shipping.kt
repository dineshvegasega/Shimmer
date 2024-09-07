package com.shimmer.store.models.orderHistory
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Shipping(
    val address: @RawValue Address,
    val method: String,
    val total: @RawValue Total
): Parcelable
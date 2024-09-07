package com.shimmer.store.models.orderHistory
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class PaymentAdditionalInfo(
    val key: String,
    val value: String
): Parcelable
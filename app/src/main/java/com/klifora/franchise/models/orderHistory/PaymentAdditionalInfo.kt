package com.klifora.franchise.models.orderHistory
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class PaymentAdditionalInfo(
    val key: String,
    val value: String
): Parcelable
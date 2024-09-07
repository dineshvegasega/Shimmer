package com.shimmer.store.models.orderHistory
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class ShippingAssignment(
    val items: List<ItemXX> = ArrayList(),
    val shipping: Shipping
): Parcelable
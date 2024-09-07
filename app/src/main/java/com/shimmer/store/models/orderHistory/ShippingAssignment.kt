package com.shimmer.store.models.orderHistory
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class ShippingAssignment(
    val items: @RawValue List<ItemXX> = ArrayList(),
    val shipping: @RawValue Shipping
): Parcelable
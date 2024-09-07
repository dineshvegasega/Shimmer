package com.shimmer.store.models.orderHistory
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class FilterGroup(
    val filters: @RawValue List<Filter> = ArrayList()
): Parcelable
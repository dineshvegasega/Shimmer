package com.shimmer.store.models.orderHistory
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class SearchCriteria(
    val filter_groups: @RawValue List<FilterGroup> = ArrayList()
): Parcelable
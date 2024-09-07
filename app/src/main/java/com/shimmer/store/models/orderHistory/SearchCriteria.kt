package com.shimmer.store.models.orderHistory
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class SearchCriteria(
    val filter_groups: List<FilterGroup> = ArrayList()
): Parcelable
package com.shimmer.store.models.orderHistory
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class ItemOrderHistoryModel(
    val items: @RawValue List<Item> = ArrayList(),
    val search_criteria: @RawValue SearchCriteria,
    val total_count: Int
): Parcelable
package com.shimmer.store.models.orderHistory
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class ItemOrderHistoryModel(
    val items: List<Item> = ArrayList(),
    val search_criteria: SearchCriteria,
    val total_count: Int
): Parcelable
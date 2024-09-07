package com.shimmer.store.models.orderHistory

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StatusHistory(
    val comment: String,
    val created_at: String,
    val entity_id: Int ?= null,
    val entity_name: String,
    val is_customer_notified: Int ?= null,
    val is_visible_on_front: Int ?= null,
    val parent_id: Int ?= null,
    val status: String
): Parcelable{
    override fun hashCode(): Int {
        val result = entity_id.hashCode()
        return result
    }
}
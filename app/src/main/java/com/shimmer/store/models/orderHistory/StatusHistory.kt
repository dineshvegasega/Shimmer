package com.shimmer.store.models.orderHistory

data class StatusHistory(
    val comment: String,
    val created_at: String,
    val entity_id: Int,
    val entity_name: String,
    val is_customer_notified: Int,
    val is_visible_on_front: Int,
    val parent_id: Int,
    val status: String
)
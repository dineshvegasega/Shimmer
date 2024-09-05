package com.shimmer.store.models.orderHistory

data class Filter(
    val condition_type: String,
    val `field`: String,
    val value: String
)
package com.shimmer.store.models.products

data class Filter(
    val condition_type: String,
    val `field`: String,
    val value: String
)
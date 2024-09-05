package com.shimmer.store.models.orderHistory

data class Shipping(
    val address: Address,
    val method: String,
    val total: Total
)
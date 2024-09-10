package com.shimmer.store.models.myOrdersDetail

data class ShippingAssignment(
    val items: List<ItemX>,
    val shipping: Shipping
)
package com.klifora.franchise.models.orderDetailModel

data class ShippingAssignment(
    val items: List<ItemX>,
    val shipping: Shipping
)
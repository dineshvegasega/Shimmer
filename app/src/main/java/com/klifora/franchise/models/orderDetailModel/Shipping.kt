package com.klifora.franchise.models.orderDetailModel

data class Shipping(
    val address: Address,
    val method: String,
    val total: Total
)
package com.klifora.franchise.models.myOrdersDetail

data class Shipping(
    val address: Address,
    val method: String,
    val total: Total
)
package com.klifora.franchise.models.orderDetailModel

data class AppliedTaxe(
    val amount: Double,
    val base_amount: Double,
    val code: String,
    val percent: Int,
    val title: String
)
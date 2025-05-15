package com.klifora.franchise.models.orderDetailModel

data class ItemAppliedTaxe(
    val applied_taxes: List<AppliedTaxe>,
    val item_id: Int,
    val type: String
)
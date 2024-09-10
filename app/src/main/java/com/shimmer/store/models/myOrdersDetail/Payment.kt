package com.shimmer.store.models.myOrdersDetail

data class Payment(
    val account_status: Any,
    val additional_information: List<String>,
    val amount_ordered: Int,
    val base_amount_ordered: Int,
    val base_shipping_amount: Int,
    val cc_last4: Any,
    val entity_id: Int,
    val method: String,
    val parent_id: Int,
    val shipping_amount: Int
)
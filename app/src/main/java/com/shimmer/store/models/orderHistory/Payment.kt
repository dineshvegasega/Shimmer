package com.shimmer.store.models.orderHistory

data class Payment(
    val account_status: Any,
    val additional_information: List<String>,
    val amount_ordered: Double,
    val base_amount_ordered: Double,
    val base_shipping_amount: Int,
    val cc_exp_year: String,
    val cc_last4: Any,
    val cc_ss_start_month: String,
    val cc_ss_start_year: String,
    val entity_id: Int,
    val method: String,
    val parent_id: Int,
    val shipping_amount: Int
)
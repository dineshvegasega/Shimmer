package com.shimmer.store.models.orderHistory

data class ExtensionAttributes(
    val applied_taxes: List<Any>,
    val item_applied_taxes: List<Any>,
    val payment_additional_info: List<PaymentAdditionalInfo>,
    val shipping_assignments: List<ShippingAssignment>
)
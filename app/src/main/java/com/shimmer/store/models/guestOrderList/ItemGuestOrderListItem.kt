package com.shimmer.store.models.guestOrderList

data class ItemGuestOrderListItem(
    val CustomerName: String,
    val cartItem: String,
    val createdtime: String,
    val customerEmail: String,
    val customerMobile: String,
    val franchiseCode: String,
    val guestcustomeroder_id: String,
    val status: String,
    val updatedtime: String
)
package com.shimmer.store.models.orderHistory

data class ItemOrderHistoryModel(
    val items: List<Item> = ArrayList(),
    val search_criteria: SearchCriteria,
    val total_count: Int
)
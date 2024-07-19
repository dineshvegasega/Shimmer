package com.shimmer.store.models.products

data class ItemProductRoot(
    val items: ArrayList<ItemProduct>,
    val search_criteria: SearchCriteria,
    val total_count: Int
)
package com.shimmer.store.models.products

data class ItemProduct(
    val items: List<Item>,
    val search_criteria: SearchCriteria,
    val total_count: Int
)
package com.shimmer.store.models.orderHistory

data class Address(
    val address_type: String,
    val city: String,
    val company: String,
    val country_id: String,
    val customer_address_id: Int,
    val email: String,
    val entity_id: Int,
    val fax: String,
    val firstname: String,
    val lastname: String,
    val parent_id: Int,
    val postcode: String,
    val region: String,
    val region_code: String,
    val region_id: Int,
    val street: List<String>,
    val telephone: String
)
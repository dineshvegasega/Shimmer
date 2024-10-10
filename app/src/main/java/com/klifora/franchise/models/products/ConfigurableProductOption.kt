package com.klifora.franchise.models.products

data class ConfigurableProductOption(
    val attribute_id: String,
    val id: Int,
    val label: String,
    val position: Int,
    val product_id: Int,
    val values: ArrayList<Value> = ArrayList()
)
package com.klifora.franchise.models

data class ItemProductOptions(
    val gender: String,
    val metal_color: String,
    val metal_purity: String,
    val name: String,
    val price: String,
    val size: String,
    val sku: String,
    val weight: String,
    var isSelected : Boolean = false

)
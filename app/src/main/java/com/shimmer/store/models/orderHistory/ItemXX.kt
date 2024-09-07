package com.shimmer.store.models.orderHistory

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ItemXX(
    val amount_refunded: Int ?= null,
    val base_amount_refunded: Int ?= null,
    val base_discount_amount: Int?= null,
    val base_discount_invoiced: Int?= null,
    val base_discount_tax_compensation_amount: Int?= null,
    val base_original_price: Double,
    val base_price: Double,
    val base_price_incl_tax: Double,
    val base_row_invoiced: Int?= null,
    val base_row_total: Double,
    val base_row_total_incl_tax: Double,
    val base_tax_amount: Int?= null,
    val base_tax_invoiced: Int?= null,
    val created_at: String,
    val discount_amount: Int?= null,
    val discount_invoiced: Int?= null,
    val discount_percent: Int?= null,
    val discount_tax_compensation_amount: Int?= null,
    val free_shipping: Int?= null,
    val is_qty_decimal: Int?= null,
    val is_virtual: Int?= null,
    val item_id: Int?= null,
    val name: String,
    val no_discount: Int?= null,
    val order_id: Int?= null,
    val original_price: Double,
    val price: Double,
    val price_incl_tax: Double,
    val product_id: Int?= null,
    val product_type: String,
    val qty_canceled: Int?= null,
    val qty_invoiced: Int?= null,
    val qty_ordered: Int?= null,
    val qty_refunded: Int?= null,
    val qty_shipped: Int?= null,
    val quote_item_id: Int?= null,
    val row_invoiced: Int?= null,
    val row_total: Double,
    val row_total_incl_tax: Double,
    val row_weight: Double,
    val sku: String,
    val store_id: Int?= null,
    val tax_amount: Int?= null,
    val tax_invoiced: Int?= null,
    val tax_percent: Int?= null,
    val updated_at: String,
    val weight: Double
): Parcelable{
    override fun hashCode(): Int {
        val result = amount_refunded.hashCode()
        return result
    }
}
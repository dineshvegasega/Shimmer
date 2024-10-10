package com.klifora.franchise.models.orderHistory

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Item(
    val base_currency_code: String ,
    val base_discount_amount: Int ?= null,
    val base_discount_tax_compensation_amount: Int?= null,
    val base_grand_total: Double,
    val base_shipping_amount: Int?= null,
    val base_shipping_discount_amount: Int?= null,
    val base_shipping_discount_tax_compensation_amnt: Int?= null,
    val base_shipping_incl_tax: Int?= null,
    val base_shipping_tax_amount: Int?= null,
    val base_subtotal: Double,
    val base_subtotal_incl_tax: Double,
    val base_tax_amount: Int?= null,
    val base_to_global_rate: Int?= null,
    val base_to_order_rate: Int?= null,
    val base_total_due: Double,
    val billing_address: BillingAddress,
    val billing_address_id: Int?= null,
    val created_at: String,
    val customer_email: String,
    val customer_firstname: String,
    val customer_gender: Int?= null,
    val customer_group_id: Int?= null,
    val customer_id: Int?= null,
    val customer_is_guest: Int?= null,
    val customer_lastname: String,
    val customer_note_notify: Int?= null,
    val discount_amount: Int?= null,
    val discount_tax_compensation_amount: Int?= null,
    val entity_id: Int?= null,
    val extension_attributes: ExtensionAttributes,
    val global_currency_code: String,
    val grand_total: Double,
    val increment_id: String,
    val is_virtual: Int?= null,
    val items: List<ItemXX> = ArrayList(),
    val order_currency_code: String,
    val payment: Payment,
    val protect_code: String,
    val quote_id: Int?= null,
    val remote_ip: String,
    val shipping_amount: Int?= null,
    val shipping_description: String,
    val shipping_discount_amount: Int?= null,
    val shipping_discount_tax_compensation_amount: Int?= null,
    val shipping_incl_tax: Int?= null,
    val shipping_tax_amount: Int?= null,
    val state: String,
    val status: String,
    val status_histories: List<StatusHistory> = ArrayList(),
    val store_currency_code: String,
    val store_id: Int?= null,
    val store_name: String,
    val store_to_base_rate: Int?= null,
    val store_to_order_rate: Int?= null,
    val subtotal: Double,
    val subtotal_incl_tax: Double,
    val tax_amount: Int?= null,
    val total_due: Double,
    val total_item_count: Int?= null,
    val total_qty_ordered: Int?= null,
    val updated_at: String,
    val weight: Double
): Parcelable {
    override fun hashCode(): Int {
        val result = base_discount_amount.hashCode()
        return result
    }
}
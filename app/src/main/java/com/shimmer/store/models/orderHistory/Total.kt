package com.shimmer.store.models.orderHistory

import android.os.Parcelable
import kotlinx.android.parcel.RawValue
import kotlinx.parcelize.Parcelize

@Parcelize
data class Total(
    val base_shipping_amount: Int?= null,
    val base_shipping_discount_amount: Int?= null,
    val base_shipping_discount_tax_compensation_amnt: Int?= null,
    val base_shipping_incl_tax: Int?= null,
    val base_shipping_tax_amount: Int?= null,
    val shipping_amount: Int?= null,
    val shipping_discount_amount: Int?= null,
    val shipping_discount_tax_compensation_amount: Int?= null,
    val shipping_incl_tax: Int?= null,
    val shipping_tax_amount: Int?= null
): Parcelable{
    override fun hashCode(): Int {
        val result = base_shipping_amount.hashCode()
        return result
    }
}
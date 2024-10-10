package com.klifora.franchise.models.orderHistory

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Payment(
    val account_status: String,
    val additional_information: List<String> = ArrayList(),
    val amount_ordered: Double,
    val base_amount_ordered: Double,
    val base_shipping_amount: Int ?= null,
    val cc_exp_year: String,
    val cc_last4: String,
    val cc_ss_start_month: String,
    val cc_ss_start_year: String,
    val entity_id: Int ?= null,
    val method: String,
    val parent_id: Int ?= null,
    val shipping_amount: Int ?= null
): Parcelable {
    override fun hashCode(): Int {
        val result = entity_id.hashCode()
        return result
    }
}
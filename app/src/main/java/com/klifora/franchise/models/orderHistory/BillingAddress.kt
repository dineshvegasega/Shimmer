package com.klifora.franchise.models.orderHistory

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class BillingAddress(
    val address_type: String,
    val city: String,
    val country_id: String,
    val email: String,
    val entity_id: Int?= null,
    val firstname: String,
    val lastname: String,
    val parent_id: Int?= null,
    val postcode: String,
    val region: String,
    val region_code: String,
    val region_id: Int?= null,
    val street: List<String> = ArrayList(),
    val telephone: String
): Parcelable{
    override fun hashCode(): Int {
        val result = entity_id.hashCode()
        return result
    }
}
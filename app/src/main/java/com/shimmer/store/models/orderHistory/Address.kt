package com.shimmer.store.models.orderHistory
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
//import kotlinx.parcelize.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Address(
    val address_type: String,
    val city: String,
    val company: String,
    val country_id: String,
    val customer_address_id: Int?= null,
    val email: String,
    val entity_id: Int?= null,
    val fax: String,
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
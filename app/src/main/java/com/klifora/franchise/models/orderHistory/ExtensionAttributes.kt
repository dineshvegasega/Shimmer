package com.klifora.franchise.models.orderHistory

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class ExtensionAttributes(
    val applied_taxes:  List<String> = ArrayList(),
    val item_applied_taxes:  List<String> = ArrayList(),
    val payment_additional_info: List<PaymentAdditionalInfo> = ArrayList(),
    val shipping_assignments:  List<ShippingAssignment> = ArrayList()
): Parcelable
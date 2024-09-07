package com.shimmer.store.models.orderHistory

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class ExtensionAttributes(
    val applied_taxes: @RawValue List<String> = ArrayList(),
    val item_applied_taxes: @RawValue List<String> = ArrayList(),
    val payment_additional_info: @RawValue List<PaymentAdditionalInfo> = ArrayList(),
    val shipping_assignments: @RawValue List<ShippingAssignment> = ArrayList()
): Parcelable
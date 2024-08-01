package com.shimmer.store.models.products
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class CustomAttribute(
    val attribute_code: String,
    val value:  @RawValue Any
): Parcelable
package com.shimmer.store.models.products
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class ExtensionAttributes(
    val category_links:  @RawValue List<CategoryLink>,
    val website_ids:  @RawValue List<Int>
): Parcelable
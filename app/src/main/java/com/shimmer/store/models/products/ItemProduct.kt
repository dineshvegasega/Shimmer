package com.shimmer.store.models.products
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class ItemProduct(
    val attribute_set_id: Int,
    val created_at: String,
    val custom_attributes: @RawValue ArrayList<CustomAttribute>,
    val extension_attributes: @RawValue ExtensionAttributes,
    val id: Int,
    val media_gallery_entries: @RawValue ArrayList<MediaGalleryEntry> = ArrayList(),
    val name: String,
    val options: @RawValue List<Any>,
    val price: Double,
    val product_links: @RawValue List<Any>,
    val sku: String,
    val status: Int,
    val tier_prices: @RawValue List<Any>,
    val type_id: String,
    val updated_at: String,
    val visibility: Int,
    val weight: Double,
    var isSelected: Boolean = false,
    var isCollapse: Boolean = false,
    var isAll: Boolean = false,
    var quantity: Int = 0,
): Parcelable
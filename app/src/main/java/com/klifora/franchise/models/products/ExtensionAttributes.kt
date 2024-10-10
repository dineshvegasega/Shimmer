package com.klifora.franchise.models.products
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class ExtensionAttributes(
    val category_links:  @RawValue List<CategoryLink>,
    val configurable_product_links: @RawValue List<Int>,
    val configurable_product_options: @RawValue List<ConfigurableProductOption>,
    val stock_item: @RawValue StockItem,
    val website_ids: @RawValue List<Int>
): Parcelable
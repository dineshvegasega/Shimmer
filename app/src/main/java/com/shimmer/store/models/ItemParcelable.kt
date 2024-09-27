package com.shimmer.store.models

import android.os.Parcelable
import com.shimmer.store.models.products.MediaGalleryEntry
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
//import kotlinx.parcelize.Parcelize

@Parcelize
data class ItemParcelable(
    val item: @RawValue ArrayList<MediaGalleryEntry> = ArrayList(),
    var position: Int = -1
): Parcelable
package com.klifora.franchise.models.products

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class MediaGalleryEntry(
    val disabled: Boolean,
    val file: String,
    val id: Int,
//    val label: @RawValue Any,
    val media_type: String,
    val position: Int,
//    val types: @RawValue List<String>
): Parcelable
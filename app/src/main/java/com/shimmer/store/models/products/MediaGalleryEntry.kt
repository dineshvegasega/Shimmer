package com.shimmer.store.models.products

data class MediaGalleryEntry(
    val disabled: Boolean,
    val `file`: String,
    val id: Int,
    val label: Any,
    val media_type: String,
    val position: Int,
    val types: List<String>
)
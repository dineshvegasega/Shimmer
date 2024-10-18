package com.klifora.franchise.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ItemFeedbackItem(
    val created_at: String,
    val detail: String,
    val entity_pk_value: String,
    val nickname: String,
    val percent: String,
    val rating_id: String,
    val review_id: String,
    val title: String,
    val value: String
): Parcelable {
    override fun hashCode(): Int {
        val result = review_id.hashCode()
        return result
    }
}
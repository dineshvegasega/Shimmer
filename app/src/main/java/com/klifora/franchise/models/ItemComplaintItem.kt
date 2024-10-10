package com.klifora.franchise.models
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ItemComplaintItem(
    val admin_note: String,
    val bcc: String,
    val category_id: String,
    val cc: String,
    val channel: String,
    val channel_data: String,
    val code: String,
    val created_at: String,
    val creditmemo_id: String,
    val customer_email: String,
    val customer_id: String,
    val customer_name: String,
    val department_id: String,
    val description: String,
    val email_id: String,
    val first_reply_at: String,
    val first_solved_at: String,
    val folder: String,
    val fp_department_id: String,
    val fp_execute_at: String,
    val fp_is_remind: String,
    val fp_period_unit: String,
    val fp_period_value: String,
    val fp_priority_id: String,
    val fp_remind_email: String,
    val fp_status_id: String,
    val fp_user_id: String,
    val is_read: String,
    val last_reply_at: String,
    val last_reply_name: String,
    val merged_ticket_id: String,
    val order_id: String,
    val price: String,
    val priority_id: String,
    val product_id: String,
    val qty_requested: String,
    val qty_returned: String,
    val quote_address_id: String,
    val rating: String,
    val reply_cnt: String,
    val search_index: String,
    val status_id: String,
    val store_id: String,
    val subject: String,
    val third_party_email: String,
    val ticket_id: String,
    val updated_at: String,
    val user_id: String
): Parcelable{
    override fun hashCode(): Int {
        val result = category_id.hashCode()
        return result
    }
}
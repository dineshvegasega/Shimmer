package com.shimmer.store.datastore.db

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Entity(tableName = "cart", indices = [Index(value = ["product_id"], unique = true)])
@Parcelize
data class CartModel(
    @PrimaryKey(autoGenerate = true) val _id: Int = 0,
    @ColumnInfo(name = "product_id") val product_id: Int? = 0,
    @ColumnInfo(name = "name") val name: String? = "",
    @ColumnInfo(name = "price") val price: Double? = 0.0,
    @ColumnInfo(name = "isSelected") val isSelected: Boolean? = false,
    @ColumnInfo(name = "isCollapse") val isCollapse: Boolean? = false,
    @ColumnInfo(name = "isAll") val isAll: Boolean? = false,
    @ColumnInfo(name = "quantity") var quantity: Int = 0,
    @ColumnInfo(name = "current_time") val currentTime: Long? = 0,
) : Parcelable

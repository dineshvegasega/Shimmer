package com.klifora.franchise.models.products

data class StockItem(
    val backorders: Int,
    val enable_qty_increments: Boolean,
    val is_decimal_divided: Boolean,
    val is_in_stock: Boolean,
    val is_qty_decimal: Boolean,
    val item_id: Int,
    val low_stock_date: Any,
    val manage_stock: Boolean,
    val max_sale_qty: Int,
    val min_qty: Int,
    val min_sale_qty: Int,
    val notify_stock_qty: Int,
    val product_id: Int,
    val qty: Int,
    val qty_increments: Int,
    val show_default_notification_message: Boolean,
    val stock_id: Int,
    val stock_status_changed_auto: Int,
    val use_config_backorders: Boolean,
    val use_config_enable_qty_inc: Boolean,
    val use_config_manage_stock: Boolean,
    val use_config_max_sale_qty: Boolean,
    val use_config_min_qty: Boolean,
    val use_config_min_sale_qty: Int,
    val use_config_notify_stock_qty: Boolean,
    val use_config_qty_increments: Boolean
)
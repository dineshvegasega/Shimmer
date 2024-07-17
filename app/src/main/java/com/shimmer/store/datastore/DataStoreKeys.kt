package com.shimmer.store.datastore

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

object DataStoreKeys {
    const val DATA_STORE_NAME = "ApplicationTemplate"
    val LOGIN_DATA by lazy { stringPreferencesKey("LOGIN_DATA") }
    val ADMIN_TOKEN by lazy { stringPreferencesKey("ADMIN_TOKEN") }
    val STORE_TOKEN by lazy { stringPreferencesKey("STORE_TOKEN") }
    val WEBSITE_ID by lazy { stringPreferencesKey("WEBSITE_ID") }
    val AUTH by lazy { stringPreferencesKey("AUTH") }
    val LIVE_SCHEME_DATA by lazy { stringPreferencesKey("LIVE_SCHEME_DATA") }
    val LIVE_NOTICE_DATA by lazy { stringPreferencesKey("LIVE_NOTICE_DATA") }
    val LIVE_TRAINING_DATA by lazy { stringPreferencesKey("LIVE_TRAINING_DATA") }
    val Complaint_Feedback_DATA by lazy { stringPreferencesKey("Complaint_Feedback_DATA") }
    val Information_Center_DATA by lazy { stringPreferencesKey("Information_Center_DATA") }

    val Context.dataStore by preferencesDataStore(DATA_STORE_NAME)
}

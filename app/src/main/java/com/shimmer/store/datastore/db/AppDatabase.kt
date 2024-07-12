package com.shimmer.store.datastore.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SearchModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun searchDao(): SearchDao
}
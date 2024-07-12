package com.shimmer.store.datastore.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SearchDao {

    @Query("SELECT * FROM searchmodel")
    suspend fun getAll(): List<SearchModel>

    @Query("SELECT * FROM searchmodel WHERE _id IN (:ids)")
    suspend fun loadAllByIds(ids: IntArray): List<SearchModel>

    @Query("SELECT * FROM searchmodel WHERE search_name LIKE :search_name")
    suspend fun findBySearchName(search_name: String): SearchModel

    @Insert
    suspend fun insertAll(users: SearchModel)

    @Delete
    suspend fun delete(user: SearchModel)


}
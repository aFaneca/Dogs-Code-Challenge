package com.afaneca.dogscodechallenge.data.local.db.breed

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BreedDao {

    @Query("SELECT * FROM breeds WHERE search_query = :searchQuery AND page = :page")
    suspend fun getAllQueryResultsInPage(searchQuery: String, page: Int): List<BreedEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(dataset: List<BreedEntity>): LongArray
}
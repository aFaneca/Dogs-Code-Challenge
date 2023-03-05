package com.afaneca.dogscodechallenge.data.local.db.dog

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DogDao {

    @Query("SELECT * FROM dogs WHERE order1 = :order AND page = :page")
    suspend fun getAllResultsInPage(page: Int, order: String): List<DogEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(dataset: List<DogEntity>): LongArray
}
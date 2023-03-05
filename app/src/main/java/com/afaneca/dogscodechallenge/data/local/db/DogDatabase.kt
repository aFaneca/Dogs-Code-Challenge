package com.afaneca.dogscodechallenge.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.afaneca.dogscodechallenge.data.local.db.breed.BreedDao
import com.afaneca.dogscodechallenge.data.local.db.breed.BreedEntity
import com.afaneca.dogscodechallenge.data.local.db.dog.DogDao
import com.afaneca.dogscodechallenge.data.local.db.dog.DogEntity

@Database(entities = [DogEntity::class, BreedEntity::class], version = 1)
abstract class DogDatabase : RoomDatabase() {
    abstract fun breedDao(): BreedDao
    abstract fun dogDao(): DogDao
}
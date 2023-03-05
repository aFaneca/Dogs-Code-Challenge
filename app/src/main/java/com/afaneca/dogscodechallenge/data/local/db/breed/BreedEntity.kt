package com.afaneca.dogscodechallenge.data.local.db.breed

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.afaneca.dogscodechallenge.domain.model.Breed

@Entity(tableName = "breeds")
class BreedEntity(
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "group")
    val group: String,
    @ColumnInfo(name = "origin")
    val origin: String,
    @ColumnInfo(name = "temperament")
    val temperament: String,
    @ColumnInfo(name = "page")
    val page: Int,
    @ColumnInfo(name = "search_query")
    val search_query: String?,
) {
    companion object {
        fun mapFromDomain(domainModel: Breed, query: String, page: Int) = BreedEntity(
            id = domainModel.id,
            name = domainModel.name,
            group = domainModel.breedGroup ?: "",
            origin = domainModel.origin ?: "",
            temperament = domainModel.temperament ?: "",
            page = page,
            search_query = query
        )
    }
}

fun BreedEntity.mapToDomain() = Breed(
    id = id,
    name = name,
    breedGroup = group,
    origin = origin,
    temperament = temperament
)
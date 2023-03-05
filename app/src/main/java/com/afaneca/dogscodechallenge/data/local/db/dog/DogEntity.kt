package com.afaneca.dogscodechallenge.data.local.db.dog

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.afaneca.dogscodechallenge.domain.model.Breed
import com.afaneca.dogscodechallenge.domain.model.DogItem

@Entity(tableName = "dogs")
class DogEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "url")
    val url: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "group")
    val group: String,
    @ColumnInfo(name = "origin")
    val origin: String,
    @ColumnInfo(name = "temperament")
    val temperament: String,
    @ColumnInfo(name = "order1")
    val order: String,
    @ColumnInfo(name = "page")
    val page: Int,
) {
    companion object {
        fun mapFromDomain(domainModel: DogItem, page: Int, order: String) = DogEntity(
            id = domainModel.id,
            url = domainModel.url,
            // TODO - make null safe
            name = domainModel.breeds.first().name ?: "",
            group = domainModel.breeds.first().breedGroup ?: "",
            origin = domainModel.breeds.first().origin ?: "",
            temperament = domainModel.breeds.first().temperament ?: "",
            page = page,
            order = order,
        )
    }
}

fun DogEntity.mapToDomain() = DogItem(
    breeds = listOf(
        Breed(
            id = id,
            name = name,
            breedGroup = group,
            origin = origin,
            temperament = temperament
        )
    ),
    id = id,
    url = url,
)

package com.fakhrirasyids.technicalnestedlist.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "jokes")
data class JokeEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String,
    val joke: String
)

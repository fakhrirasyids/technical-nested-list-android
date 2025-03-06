package com.fakhrirasyids.technicalnestedlist.core.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fakhrirasyids.technicalnestedlist.core.data.local.dao.JokeDao
import com.fakhrirasyids.technicalnestedlist.core.data.local.entity.JokeEntity

@Database(entities = [JokeEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun jokeDao(): JokeDao
}

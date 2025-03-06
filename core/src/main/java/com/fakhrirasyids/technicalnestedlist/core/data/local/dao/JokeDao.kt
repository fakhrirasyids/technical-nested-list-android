package com.fakhrirasyids.technicalnestedlist.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fakhrirasyids.technicalnestedlist.core.data.local.entity.JokeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JokeDao {
    @Query("SELECT * FROM jokes WHERE category = :category")
    fun getJokesByCategory(category: String): Flow<List<JokeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJokes(jokes: List<JokeEntity>)

    @Query("DELETE FROM jokes WHERE category = :category")
    suspend fun clearJokesByCategory(category: String)

    @Query("DELETE FROM jokes")
    suspend fun clearJokes()
}

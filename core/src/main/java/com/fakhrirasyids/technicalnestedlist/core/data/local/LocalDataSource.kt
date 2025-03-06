package com.fakhrirasyids.technicalnestedlist.core.data.local

import com.fakhrirasyids.technicalnestedlist.core.data.local.dao.JokeDao
import com.fakhrirasyids.technicalnestedlist.core.data.local.entity.JokeEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val jokeDao: JokeDao) {

    fun getJokesByCategory(category: String): Flow<List<String>> =
        jokeDao.getJokesByCategory(category).map { jokes -> jokes.map { it.joke } }

    suspend fun insertJokes(category: String, jokes: List<String>, shouldFetch: Boolean) {
        if (!shouldFetch) {
            jokeDao.clearJokesByCategory(category)
        }

        val jokeEntities = jokes.map { JokeEntity(category = category, joke = it) }
        jokeDao.insertJokes(jokeEntities)
    }

    suspend fun clearJokes() = jokeDao.clearJokes()
}

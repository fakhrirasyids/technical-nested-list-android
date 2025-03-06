package com.fakhrirasyids.technicalnestedlist.core.data.repository

import com.fakhrirasyids.technicalnestedlist.core.data.local.LocalDataSource
import com.fakhrirasyids.technicalnestedlist.core.data.mapper.CategoriesMapper.toDomain
import com.fakhrirasyids.technicalnestedlist.core.data.remote.RemoteDataSource
import com.fakhrirasyids.technicalnestedlist.core.domain.repo.MainRepository
import com.fakhrirasyids.technicalnestedlist.core.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : MainRepository {

    override fun getCategories() = flow {
        runCatching {
            localDataSource.clearJokes()

            val categoriesResponse = remoteDataSource.getCategories()
            categoriesResponse.toDomain()
        }.onSuccess { categories ->
            emit(Resource.Success(categories))
        }.onFailure { exception ->
            emit(Resource.Error(exception.message.toString()))
        }
    }.onStart { emit(Resource.Loading) }
        .flowOn(Dispatchers.IO)

    override fun addRandomJokes(category: String, shouldFetch: Boolean) =
        object : NetworkBoundResource<List<String>, List<String>>() {

            override fun loadFromDb(): Flow<List<String>> =
                localDataSource.getJokesByCategory(category)

            override suspend fun fetchFromNetwork(): List<String> =
                remoteDataSource.getChildCategories(category).toDomain()

            override suspend fun fetchFromNetworkWithoutParam(): List<String> =
                remoteDataSource.getChildCategories().toDomain()

            override suspend fun saveNetworkResult(data: List<String>) {
                localDataSource.insertJokes(category, data, shouldFetch)
            }

            override fun shouldFetch(data: List<String>?): Boolean =
                shouldFetch || data.isNullOrEmpty()
        }.asFlow(Dispatchers.IO)
}

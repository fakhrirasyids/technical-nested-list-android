package com.fakhrirasyids.technicalnestedlist.core.data.repository

import com.fakhrirasyids.technicalnestedlist.core.data.mapper.CategoriesMapper.toDomain
import com.fakhrirasyids.technicalnestedlist.core.data.remote.RemoteDataSource
import com.fakhrirasyids.technicalnestedlist.core.domain.repo.MainRepository
import com.fakhrirasyids.technicalnestedlist.core.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : MainRepository {

    override fun getCategories() = flow {
        emit(Resource.Loading)

        try {
            val categoriesResponse = remoteDataSource.getCategories()
            val categories = categoriesResponse.toDomain()

            val categoriesWithJokes = supervisorScope {
                categories.map { category ->
                    async {
                        try {
                            val childResponse =
                                remoteDataSource.getChildCategories(category.categoryName)
                            category.copy(jokes = childResponse.toDomain().toMutableList())
                        } catch (e: Exception) {
                            throw e
                        }
                    }
                }.awaitAll()
            }

            emit(Resource.Success(categoriesWithJokes))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    override fun addRandomJokes(category: String) = flow {
        emit(Resource.Loading)

        try {
            val jokesResponse = remoteDataSource.getChildCategories(category)
            val newJokes = jokesResponse.jokes?.mapNotNull { it?.joke }?.take(2) ?: emptyList()

            emit(Resource.Success(newJokes))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)
}

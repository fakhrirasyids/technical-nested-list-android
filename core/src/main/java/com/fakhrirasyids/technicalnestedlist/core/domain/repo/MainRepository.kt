package com.fakhrirasyids.technicalnestedlist.core.domain.repo

import com.fakhrirasyids.technicalnestedlist.core.domain.model.Categories
import com.fakhrirasyids.technicalnestedlist.core.utils.Resource
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    fun getCategories(): Flow<Resource<List<Categories>>>
    fun addRandomJokes(category: String): Flow<Resource<List<String>>>
}
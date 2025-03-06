package com.fakhrirasyids.technicalnestedlist.core.domain.usecase.getcategories

import com.fakhrirasyids.technicalnestedlist.core.domain.model.Categories
import com.fakhrirasyids.technicalnestedlist.core.utils.Resource
import kotlinx.coroutines.flow.Flow

interface GetCategoriesUseCase {
    operator fun invoke(): Flow<Resource<List<Categories>>>
}
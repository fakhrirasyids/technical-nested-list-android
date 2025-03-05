package com.fakhrirasyids.technicalnestedlist.core.domain.usecase.getcategories

import com.fakhrirasyids.technicalnestedlist.core.domain.model.Categories
import kotlinx.coroutines.flow.Flow

interface GetCategoriesUseCase {
    operator fun invoke(): Flow<List<Categories>>
}
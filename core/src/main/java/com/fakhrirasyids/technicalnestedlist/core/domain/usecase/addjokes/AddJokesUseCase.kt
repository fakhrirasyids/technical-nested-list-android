package com.fakhrirasyids.technicalnestedlist.core.domain.usecase.addjokes

import com.fakhrirasyids.technicalnestedlist.core.utils.Resource
import kotlinx.coroutines.flow.Flow

interface AddJokesUseCase {
    operator fun invoke(category: String, shouldFetch: Boolean = false): Flow<Resource<List<String>>>
}
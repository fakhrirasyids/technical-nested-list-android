package com.fakhrirasyids.technicalnestedlist.core.domain.usecase.addjokes

import kotlinx.coroutines.flow.Flow

interface AddJokesUseCase {
    operator fun invoke(category: String): Flow<List<String>>
}
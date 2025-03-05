package com.fakhrirasyids.technicalnestedlist.core.domain.usecase.addjokes

import com.fakhrirasyids.technicalnestedlist.core.domain.repo.MainRepository
import com.fakhrirasyids.technicalnestedlist.core.utils.Resource
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AddJokesInteractor @Inject constructor(
    private val repository: MainRepository
) : AddJokesUseCase {

    override operator fun invoke(category: String) = repository.addRandomJokes(category)
        .filterIsInstance<Resource.Success<List<String>>>()
        .map { it.data }
}

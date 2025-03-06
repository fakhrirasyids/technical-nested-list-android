package com.fakhrirasyids.technicalnestedlist.core.domain.usecase.addjokes

import com.fakhrirasyids.technicalnestedlist.core.domain.repo.MainRepository
import javax.inject.Inject

class AddJokesInteractor @Inject constructor(
    private val repository: MainRepository
) : AddJokesUseCase {

    override operator fun invoke(category: String, shouldFetch: Boolean) =
        repository.addRandomJokes(category, shouldFetch)
}

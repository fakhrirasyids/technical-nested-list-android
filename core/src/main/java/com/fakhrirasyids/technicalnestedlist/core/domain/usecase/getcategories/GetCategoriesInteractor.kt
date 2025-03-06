package com.fakhrirasyids.technicalnestedlist.core.domain.usecase.getcategories

import com.fakhrirasyids.technicalnestedlist.core.domain.repo.MainRepository
import javax.inject.Inject

class GetCategoriesInteractor @Inject constructor(
    private val repository: MainRepository
) : GetCategoriesUseCase {

    override operator fun invoke() = repository.getCategories()
}

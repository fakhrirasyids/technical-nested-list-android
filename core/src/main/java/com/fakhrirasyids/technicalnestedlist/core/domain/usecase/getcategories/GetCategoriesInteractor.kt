package com.fakhrirasyids.technicalnestedlist.core.domain.usecase.getcategories

import com.fakhrirasyids.technicalnestedlist.core.domain.model.Categories
import com.fakhrirasyids.technicalnestedlist.core.domain.repo.MainRepository
import com.fakhrirasyids.technicalnestedlist.core.utils.Resource
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCategoriesInteractor @Inject constructor(
    private val repository: MainRepository
) : GetCategoriesUseCase {

    override operator fun invoke() = repository.getCategories()
        .filterIsInstance<Resource.Success<List<Categories>>>()
        .map { it.data }
}

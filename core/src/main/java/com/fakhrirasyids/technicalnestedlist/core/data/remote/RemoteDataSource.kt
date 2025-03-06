package com.fakhrirasyids.technicalnestedlist.core.data.remote

import com.fakhrirasyids.technicalnestedlist.core.data.remote.services.CategoriesApiServices
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val categoriesApiServices: CategoriesApiServices
) {
    suspend fun getCategories() =
        categoriesApiServices.getCategories()

    suspend fun getChildCategories(category: String = "any") =
        categoriesApiServices.getChildCategories(category)
}
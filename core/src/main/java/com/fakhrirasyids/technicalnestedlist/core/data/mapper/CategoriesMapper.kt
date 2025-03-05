package com.fakhrirasyids.technicalnestedlist.core.data.mapper

import com.fakhrirasyids.technicalnestedlist.core.data.remote.response.CategoriesResponse
import com.fakhrirasyids.technicalnestedlist.core.data.remote.response.ChildCategoriesResponse
import com.fakhrirasyids.technicalnestedlist.core.domain.model.Categories

object CategoriesMapper {
    fun CategoriesResponse.toDomain(): List<Categories> {
        return categories?.mapNotNull { category ->
            category?.let { Categories(categoryName = it) }
        } ?: emptyList()
    }

    fun ChildCategoriesResponse.toDomain(): List<String> {
        return jokes?.mapNotNull { it?.joke } ?: emptyList()
    }
}
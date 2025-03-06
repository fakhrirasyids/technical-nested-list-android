package com.fakhrirasyids.technicalnestedlist.core.data.mapper

import com.fakhrirasyids.technicalnestedlist.core.data.remote.response.CategoriesResponse
import com.fakhrirasyids.technicalnestedlist.core.data.remote.response.ChildCategoriesResponse
import com.fakhrirasyids.technicalnestedlist.core.domain.model.Categories

fun CategoriesResponse.toDomain(): List<Categories> =
    categories?.mapIndexedNotNull { index, category ->
        category?.let { Categories(index = index, categoryName = it) }
    } ?: emptyList()

fun ChildCategoriesResponse.toDomain(): List<String> =
    jokes?.mapNotNull { it?.joke } ?: emptyList()
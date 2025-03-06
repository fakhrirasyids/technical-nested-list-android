package com.fakhrirasyids.technicalnestedlist.core.domain.model

data class Categories(
    val index: Int = 0,
    val categoryName: String = "",
    val jokes: List<String> = emptyList(),
    var isExpanded: Boolean = false
)

package com.fakhrirasyids.technicalnestedlist.core.domain.model

data class Categories(
    val categoryName: String = "",
    val jokes: MutableList<String> = mutableListOf(),
    var isExpanded: Boolean = false
)

package com.fakhrirasyids.technicalnestedlist.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class CategoriesResponse(

	@field:SerializedName("categories")
	val categories: List<String?>? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("timestamp")
	val timestamp: Long? = null
)
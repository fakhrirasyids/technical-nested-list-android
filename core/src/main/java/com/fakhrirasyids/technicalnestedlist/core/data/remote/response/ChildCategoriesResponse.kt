package com.fakhrirasyids.technicalnestedlist.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class ChildCategoriesResponse(

	@field:SerializedName("amount")
	val amount: Int? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("jokes")
	val jokes: List<JokesItemResponse?>? = null
)
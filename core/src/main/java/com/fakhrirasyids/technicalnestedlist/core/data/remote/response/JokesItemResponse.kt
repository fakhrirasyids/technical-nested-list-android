package com.fakhrirasyids.technicalnestedlist.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class JokesItemResponse(

	@field:SerializedName("flagsResponse")
	val flagsResponse: FlagsResponse? = null,

	@field:SerializedName("safe")
	val safe: Boolean? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("category")
	val category: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("lang")
	val lang: String? = null,

	@field:SerializedName("joke")
	val joke: String? = null
)
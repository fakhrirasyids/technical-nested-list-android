package com.fakhrirasyids.technicalnestedlist.core.data.remote.services

import com.fakhrirasyids.technicalnestedlist.core.data.remote.response.CategoriesResponse
import com.fakhrirasyids.technicalnestedlist.core.data.remote.response.ChildCategoriesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CategoriesApiServices {
    @GET(CATEGORIES_ENDPOINT)
    suspend fun getCategories(): CategoriesResponse

    @GET(CHILD_CATEGORIES_ENDPOINT)
    suspend fun getChildCategories(
        @Path("category") category: String,
        @Query("type") type: String = "single",
        @Query("amount") amount: Int = 2
    ): ChildCategoriesResponse

    companion object {

        // Endpoints
        private const val CATEGORIES_ENDPOINT = "categories"
        private const val CHILD_CATEGORIES_ENDPOINT = "jokes/{category}"
    }
}
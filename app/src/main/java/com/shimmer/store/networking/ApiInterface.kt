package com.shimmer.store.networking

import com.google.gson.JsonElement
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @POST(ADMIN_TOKEN)
    suspend fun adminToken(
        @Body requestBody: RequestBody
    ): Response<JsonElement>


    @GET(WEBSITE_URL)
    suspend fun websiteUrl(
        @Query("email") email: String
    ): Response<JsonElement>


    @POST("{id}"+CUSTOMER_LOGIN_TOKEN)
    suspend fun customerLoginToken(
        @Header("Authorization") adminToken : String,
        @Path("id") id: String,
        @Body requestBody: RequestBody
    ): Response<JsonElement>


    @GET("{id}"+CUSTOMER_DETAIL)
    suspend fun customerDetail(
        @Header("Authorization") authHeader : String,
        @Path("id") id: String,
    ): Response<JsonElement>

    @GET("{id}"+PRODUCTS)
    suspend fun prodcuts(
        @Header("Authorization") authHeader : String,
        @Path("id") id: String,
        @Query("") field: ArrayList<String>,
//        @FieldMap parms : Map<String, String>,
//        @Query("searchCriteria[filter_groups][0][filters]") field: ArrayList<String>,
//        @Query("searchCriteria[filter_groups][0][filters]") value: ArrayList<String>,

//        @Query("searchCriteria[filter_groups][0][filters][0][field]") field: ArrayList<String>,
//        @Query("searchCriteria[filter_groups][0][filters][0][value]") value: ArrayList<String>
    ): Response<JsonElement>

    @GET("{id}"+PRODUCTS)
    suspend fun list(
        @Header("Authorization") authHeader : String,
                      @Path("id") id: String,
                      @Query("searchCriteria[filter_groups][0][filters]") vararg categories: String?):
            Response<JsonElement>
}
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
import retrofit2.http.QueryMap

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
    suspend fun products(
        @Header("Authorization") authHeader: String,
        @Path("id") id: String,
        @QueryMap parms: Map<String, String>,
    ): Response<JsonElement>

    @GET(PRODUCTS_ID)
    suspend fun productsID(
        @Header("Authorization") authHeader: String,
        @QueryMap parms: Map<String, String>,
    ): Response<JsonElement>


    @GET("{id}"+PRODUCTS_DETAIL+"{ids}")
    suspend fun productsDetail(
        @Header("Authorization") authHeader : String,
        @Path("id") id: String,
        @Path("ids") ids: String,
    ): Response<JsonElement>


    @GET(PRODUCTS_DETAIL_ID+"{ids}")
    suspend fun productsDetailID(
        @Header("Authorization") authHeader : String,
        @Path("ids") ids: String,
    ): Response<JsonElement>


    @GET(PRODUCT_OPTIONS)
    suspend fun productsOptions(
        @Query("con_id") con_id: String
    ): Response<JsonElement>


    @GET(ALL_PRODUCTS+"{id}/children")
    suspend fun allProducts(
        @Header("Authorization") authHeader : String,
        @Path("id") id: String,
    ): Response<JsonElement>


}




//https://shop.vegasega.com/rest/all/V1/configurable-products/SRI0002G/children
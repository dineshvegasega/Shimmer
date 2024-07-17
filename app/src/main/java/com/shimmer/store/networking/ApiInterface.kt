package com.shimmer.store.networking

import com.google.gson.JsonElement
import com.shimmer.store.ItemSS
import com.shimmer.store.models.BaseResponseDC
import com.shimmer.store.models.ItemChat
import com.shimmer.store.models.Items
import com.shimmer.store.models.ItemComplaintType
import com.shimmer.store.models.ItemDistrict
import com.shimmer.store.models.ItemMarketplace
import com.shimmer.store.models.ItemOrganization
import com.shimmer.store.models.ItemPanchayat
import com.shimmer.store.models.ItemPincode
import com.shimmer.store.models.ItemSizes
import com.shimmer.store.models.ItemVending
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.storeWebUrl
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

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


}
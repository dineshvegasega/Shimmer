package com.shimmer.store.networking

import com.google.gson.JsonElement
import com.shimmer.store.models.ItemBanner
import com.shimmer.store.models.ItemFranchiseArray
import com.shimmer.store.models.cart.ItemCart
import com.shimmer.store.models.cart.ItemCartModel
import com.shimmer.store.models.guestOrderList.ItemGuestOrderList
import com.shimmer.store.models.myOrdersDetail.ItemOrderDetail
import com.shimmer.store.models.myOrdersList.ItemOrders
import com.shimmer.store.models.orderHistory.ItemOrderHistoryModel
import com.shimmer.store.models.user.ItemUser
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
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


    @POST("{id}"+CUSTOMER_RESET_PASSWORD)
    suspend fun customerResetPassword(
        @Header("Authorization") adminToken : String,
        @Path("id") id: String,
        @Body requestBody: RequestBody
    ): Response<JsonElement>


    @GET("{id}"+CUSTOMER_DETAIL)
    suspend fun customerDetail(
        @Header("Authorization") authHeader : String,
        @Path("id") id: String,
    ): Response<JsonElement>


    @GET(USER_DETAIL)
    suspend fun userDetail(
        @Query("franchise") franchise: String,
    ): Response<ItemUser>

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


    @POST("{id}"+QUOTE_ID)
    suspend fun getQuoteId(
        @Header("Authorization") authHeader : String,
        @Path("id") id: String,
        @Body requestBody: RequestBody
    ): Response<JsonElement>


    @GET("{id}"+QUOTE_ID)
    suspend fun getCart(
        @Header("Authorization") authHeader : String,
        @Path("id") id: String,
    ): Response<ItemCart>


    @POST("{id}"+ADD_CART)
    suspend fun addCart(
        @Header("Authorization") authHeader : String,
        @Path("id") id: String,
        @Body requestBody: RequestBody
    ): Response<ItemCartModel>


    @PUT("{id}"+ADD_CART+"/{ids}")
    suspend fun updateCart(
        @Header("Authorization") authHeader : String,
        @Path("id") id: String,
        @Path("ids") ids: Int,
        @Body requestBody: RequestBody
    ): Response<ItemCartModel>


    @DELETE("{id}"+ADD_CART+"/{ids}")
    suspend fun deleteCart(
        @Header("Authorization") authHeader : String,
        @Path("id") id: String,
        @Path("ids") ids: Int,
    ): Response<Boolean>


    @GET(BANNER)
    suspend fun banner(): Response<ItemBanner>

    @GET(FRANCHISE_LIST)
    suspend fun franchiseList(): Response<ItemFranchiseArray>

    @GET(ORDERS)
    suspend fun getOrderHistory(
        @Header("Authorization") authHeader: String,
        @QueryMap parms: Map<String, String>,
    ): Response<ItemOrderHistoryModel>


    @POST(PLACE_ORDER_GUEST)
    suspend fun placeOrderGuest(
        @Body requestBody: RequestBody
    ): Response<JsonElement>


    @GET(GUEST_ORDER_LIST)
    suspend fun guestOrderList(
        @Query("franchise") franchise: String,
    ): Response<ItemGuestOrderList>


    @POST(UPDATE_STATUS)
    suspend fun updateStatus(
        @Body requestBody: RequestBody
    ): Response<JsonElement>


    @POST("{id}"+UPDATE_SHIPPING)
    suspend fun updateShipping(
        @Header("Authorization") authHeader : String,
        @Path("id") id: String,
        @Body requestBody: RequestBody
    ): Response<JsonElement>


    @POST("{id}"+UPDATE_ORDER_PAYMENT)
    suspend fun createOrder(
        @Header("Authorization") authHeader : String,
        @Path("id") id: String,
        @Body requestBody: RequestBody
    ): Response<JsonElement>


    @PUT("{id}"+POST_CUSTOM_DETAILS)
    suspend fun postCustomDetails(
        @Header("Authorization") authHeader : String,
        @Path("id") id: String,
        @Body requestBody: RequestBody
    ): Response<JsonElement>


    @GET(ORDER_HISTORY_LIST)
    suspend fun orderHistoryList(
        @Query("customerID") franchise: String,
    ): Response<ItemOrders>


    @GET(ORDER_HISTORY_LIST_DETAIL+"/{id}")
    suspend fun orderHistoryListDetail(
        @Header("Authorization") authHeader : String,
        @Path("id") id: String
    ): Response<ItemOrderDetail>


}




//https://shop.vegasega.com/rest/all/V1/configurable-products/SRI0002G/children
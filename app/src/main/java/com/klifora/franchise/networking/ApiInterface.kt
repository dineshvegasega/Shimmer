package com.klifora.franchise.networking

import com.google.gson.JsonElement
import com.klifora.franchise.models.ItemBanner
import com.klifora.franchise.models.ItemComplaint
import com.klifora.franchise.models.ItemComplaintItem
import com.klifora.franchise.models.ItemFeedback
import com.klifora.franchise.models.ItemFranchiseArray
import com.klifora.franchise.models.ItemMessageHistory
import com.klifora.franchise.models.ItemRelatedProducts
import com.klifora.franchise.models.ItemWebsite
import com.klifora.franchise.models.cart.ItemCart
import com.klifora.franchise.models.cart.ItemCartModel
import com.klifora.franchise.models.guestOrderList.ItemGuestOrderList
import com.klifora.franchise.models.myOrdersDetail.ItemOrderDetail
import com.klifora.franchise.models.myOrdersList.ItemOrders
import com.klifora.franchise.models.orderHistory.ItemOrderHistoryModel
import com.klifora.franchise.models.user.ItemUser
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
    ): Response<ItemWebsite>


    @POST("{id}"+SEND_OTP)
    suspend fun sendOTP(
        @Path("id") id: String,
        @Body requestBody: RequestBody
    ): Response<JsonElement>


    @POST("{id}"+VERIFY_OTP)
    suspend fun verifyOTP(
        @Path("id") id: String,
        @Body requestBody: RequestBody
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
        @Query("customermobile") customermobile: String,
        @Query("customername") customername: String,
        @Query("ordernumber") ordernumber: String = "",
        @Query("pagenumber") pagenumber: Int,
        @Query("pagesize") pagesize: Int = 10,
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


//    @PUT("{id}"+POST_CUSTOM_DETAILS)
//    suspend fun postCustomDetails(
//        @Header("Authorization") authHeader : String,
//        @Path("id") id: String,
//        @Body requestBody: RequestBody
//    ): Response<JsonElement>

    @GET(POST_CUSTOM_DETAILS)
    suspend fun postCustomDetails(
//        @Header("Authorization") authHeader : String,
//        @Body requestBody: RequestBody
        @Query("cartId") cartId: String,
        @Query("checkout_buyer_name") checkout_buyer_name: String,
        @Query("checkout_buyer_email") checkout_buyer_email: String,
        @Query("checkout_purchase_order_no") pagenumber: String,
        @Query("checkout_goods_mark") pagesize: String= ""
    ): Response<String>




    @GET(ORDER_HISTORY_LIST)
    suspend fun orderHistoryList(
        @Query("franchise") franchise: String,
        @Query("customermobile") customermobile: String,
        @Query("customername") customername: String,
        @Query("pagenumber") pagenumber: Int,
        @Query("pagesize") pagesize: Int,
    ): Response<ItemOrders>


    @GET(ORDER_HISTORY_LIST_DETAIL+"/{id}")
    suspend fun orderHistoryListDetail(
        @Header("Authorization") authHeader : String,
        @Path("id") id: String
    ): Response<ItemOrderDetail>


    @GET(ORDER_HISTORY_DETAIL)
    suspend fun orderHistoryDetail(
        @Query("orderid") orderid: String,
    ): Response<JsonElement>


    @POST(CUSTOM_PRODUCT)
    suspend fun customProduct(
        @Body requestBody: RequestBody
    ): Response<JsonElement>


    @GET(RELATED_PRODUCTS)
    suspend fun relatedProducts(
        @Query("productid") productid: Int,
    ): Response<ItemRelatedProducts>


    @GET("{id}"+COMPLAINT_LIST)
    suspend fun complaintList(
//        @Header("Authorization") authHeader : String,
        @Path("id") webId: String,
        @Query("customerids") ids: String,
    ): Response<ItemComplaint>


    @GET("{id}"+FEEDBACK_LIST)
    suspend fun feedbackList(
//        @Header("Authorization") authHeader : String,
        @Path("id") webId: String,
        @Query("customerids") ids: String,
    ): Response<ItemFeedback>


    @GET("{id}"+COMPLAINT_DETAIL)
    suspend fun complaintDetail(
        @Path("id") webId: String,
        @Query("ticket_id") ticket_id: String,
    ): Response<ItemComplaint>


    @POST("{id}"+CREATE_TICKET)
    suspend fun createTicket(
        @Path("id") id: String,
        @Body requestBody: RequestBody
    ): Response<JsonElement>


    @POST("{id}"+CREATE_FEEDBACK)
    suspend fun createFeedback(
        @Path("id") id: String,
        @Body requestBody: RequestBody
    ): Response<JsonElement>


    @GET("{id}"+ MESSAGE_HISTORY)
    suspend fun messageHistory(
        @Path("id") id: String,
        @Query("ticketid") ticketid: String,
        @Query("customerid") customerid: String,
    ): Response<ItemMessageHistory>


    @POST("{id}"+SEND_MESSAGE)
    suspend fun sendMessage(
        @Path("id") id: String,
        @Body requestBody: RequestBody
    ): Response<JsonElement>


//    @GET("{id}"+ MESSAGE_HISTORY)
//    suspend fun messageHistory(
//        @Path("id") id: String,
//        @Query("ticketid") ticketid: String,
//        @Query("customerid") customerid: String,
//    ): Response<JsonElement>
}




//https://shop.vegasega.com/rest/all/V1/configurable-products/SRI0002G/children
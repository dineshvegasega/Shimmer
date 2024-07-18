package com.shimmer.store.ui.main.products

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonElement
import com.shimmer.store.datastore.db.CartModel
import com.shimmer.store.models.Items
import com.shimmer.store.networking.ApiInterface
import com.shimmer.store.networking.CallHandler
import com.shimmer.store.networking.Repository
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.db
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.storeWebUrl
import com.shimmer.store.utils.showSnackBar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class ProductsVM @Inject constructor(private val repository: Repository) : ViewModel() {

    var item1 : ArrayList<Items> = ArrayList()
    var item2 : ArrayList<String> = ArrayList()
    var item3 : ArrayList<String> = ArrayList()


    init {
        item1.add(Items())
        item1.add(Items())
        item1.add(Items())
        item1.add(Items())
        item1.add(Items())
        item1.add(Items())
        item1.add(Items())
        item1.add(Items())
        item1.add(Items())
        item1.add(Items())
        item1.add(Items())
        item1.add(Items())
        item1.add(Items())
        item1.add(Items())
        item1.add(Items())
        item1.add(Items())

        item2.add("1")
        item2.add("2")

        item3.add("1")
        item3.add("2")
        item3.add("3")
        item3.add("4")
    }



    fun getCartCount(callBack: Int.() -> Unit){
        viewModelScope.launch {
            val userList: List<CartModel> ?= db?.cartDao()?.getAll()
            var countBadge = 0
            userList?.forEach {
                countBadge += it.quantity
            }
            callBack(countBadge)
        }
    }






    fun getProducts(adminToken: String, view: View, arrayField: ArrayList<String>, arrayValue: ArrayList<String>, callBack: Int.() -> Unit) =
        viewModelScope.launch {
//            val parms: HashMap<String, String> = HashMap()
//            parms.put("searchCriteria[filter_groups][0][filters][0][field][category_id]", "searchCriteria[filter_groups][0][filters][0][field][value]15");
////            parms.put("searchCriteria[filter_groups][0][filters][0][value][]", "15");

            var arrayField : ArrayList<String> = ArrayList()
            arrayField.add("searchCriteria[filter_groups][0][filters][0][field]=10")
//            arrayField.add("[1][field]")

            var arrayValue: ArrayList<String> = ArrayList()
            arrayValue.add("[0][value]")
            arrayValue.add("[1][value]")

            repository.callApi(
                callHandler = object : CallHandler<Response<JsonElement>> {
                    override suspend fun sendRequest(apiInterface: ApiInterface) =
                        apiInterface.list("Bearer " +adminToken, storeWebUrl, arrayField.toString())
                    override fun success(response: Response<JsonElement>) {
                        if (response.isSuccessful) {
//                            try {
//                                val token = response.body().toString()
//                                    .substring(1, response.body().toString().length - 1)
                                  Log.e("TAG", "successAA: ${response.body().toString()}")
//                                //callBack(token)
//                                storeToken = token
//                                //customerDetail(token, view, callBack)
//                            } catch (e: Exception) {
//                            }
                        }
                    }

                    override fun error(message: String) {
//                        Log.e("TAG", "successAA: ${message}")
//                        super.error(message)
                        showSnackBar(message)
//                        callBack(message.toString())
                    }

                    override fun loading() {
                        super.loading()
                    }
                }
            )
        }


}
package com.shimmer.store.ui.main.category

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.shimmer.store.R
import com.shimmer.store.databinding.ItemChildBinding
//import com.shimmer.store.databinding.ItemCategoryRoundBinding
import com.shimmer.store.databinding.ItemFaqBinding
import com.shimmer.store.databinding.ItemHomeCategoryBinding
import com.shimmer.store.databinding.LoaderBinding
import com.shimmer.store.datastore.db.CartModel
import com.shimmer.store.genericAdapter.GenericAdapter
import com.shimmer.store.models.Items
import com.shimmer.store.models.cart.ItemCart
import com.shimmer.store.networking.ApiInterface
import com.shimmer.store.networking.CallHandler
import com.shimmer.store.networking.Repository
import com.shimmer.store.ui.main.category.CategoryChildTab.Companion.mainSelect
import com.shimmer.store.ui.mainActivity.MainActivity
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.db
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.mainCategory
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.mainMaterial
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.mainPrice
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.mainShopFor
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.storeWebUrl
import com.shimmer.store.utils.sessionExpired
import com.shimmer.store.utils.showSnackBar
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CategoryVM @Inject constructor(private val repository: Repository) : ViewModel() {
//    var item1: ArrayList<Items> = ArrayList()
    var item2: ArrayList<String> = ArrayList()
    var item3: ArrayList<String> = ArrayList()

    var positionSelect = 0

    var alertDialog: AlertDialog? = null


    fun show() {
        viewModelScope.launch {
            if (alertDialog != null) {
                alertDialog?.dismiss()
                alertDialog?.show()
            }
        }
    }

    fun hide() {
        viewModelScope.launch {
            if (alertDialog != null) {
                alertDialog?.dismiss()
            }
        }
    }


    init {
        val alert = AlertDialog.Builder(MainActivity.activity.get())
        val binding =
            LoaderBinding.inflate(LayoutInflater.from(MainActivity.activity.get()), null, false)
        alert.setView(binding.root)
        alert.setCancelable(false)
        alertDialog = alert.create()
        alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

//        item1.add(Items())
//        item1.add(Items())
//        item1.add(Items())
//        item1.add(Items())
//        item1.add(Items())
//        item1.add(Items())
//        item1.add(Items())
//        item1.add(Items())
//        item1.add(Items())
//        item1.add(Items())
//        item1.add(Items())
//        item1.add(Items())
//        item1.add(Items())
//        item1.add(Items())
//        item1.add(Items())
//        item1.add(Items())

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



    fun getCart(customerToken: String, callBack: ItemCart.() -> Unit) =
        viewModelScope.launch {
            repository.callApi(
                callHandler = object : CallHandler<Response<ItemCart>> {
                    override suspend fun sendRequest(apiInterface: ApiInterface) =
//                        if (loginType == "vendor") {
                        apiInterface.getCart("Bearer " +customerToken, storeWebUrl)
                    //                        } else if (loginType == "guest") {
//                        apiInterface.getQuoteId("Bearer " +adminToken, emptyMap)
                    //                        } else {
//                            apiInterface.products("Bearer " +adminToken, storeWebUrl, emptyMap)
//                        }
                    @SuppressLint("SuspiciousIndentation")
                    override fun success(response: Response<ItemCart>) {
                        if (response.isSuccessful) {
                            try {
                                Log.e("TAG", "successAAXX: ${response.body().toString()}")
                                callBack(response.body()!!)
                            } catch (_: Exception) {
                            }
                        }
                    }

                    override fun error(message: String) {
                        if(message.contains("fieldName")){
                            showSnackBar("Something went wrong!")
                        } else {
                            sessionExpired()
                        }
                    }

                    override fun loading() {
                        super.loading()
                    }
                }
            )
        }

    val subCategoryAdapter1 = object : GenericAdapter<ItemChildBinding, Items>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemChildBinding.inflate(inflater, parent, false)

        override fun onBindHolder(
            binding: ItemChildBinding,
            dataClass: Items,
            position: Int
        ) {
            binding.apply {
                if (dataClass.name.contains(" ")){
                    val txt = dataClass.name.split(" ")[0]+
                    "\n"+dataClass.name.split(" ")[1]
                    textName.text = txt
                } else {
                    textName.text = dataClass.name
                }

                layoutChild.singleClick {
                    mainCategory.forEach {
                        it.isSelected = false
                        it.subCategory.forEach {
                            it.isSelected = false
                        }
                    }

                    currentList.forEach {
                        it.isSelected = false
                    }
                    dataClass.isSelected = true

                    currentList.forEach {
                        if (it.isSelected && it.isAll) {
//                            Log.e("TAG", "IFisSelected: " + it.isSelected + " :: "+it.isAll + " :: "+mainSelect)
                            when (mainSelect) {
                                1 -> mainCategory[0].isSelected = true
                                2 -> mainCategory[1].isSelected = true
                                3 -> mainCategory[2].isSelected = true
                                4 -> mainCategory[3].isSelected = true
                                5 -> mainCategory[4].isSelected = true
                                6 -> mainCategory[5].isSelected = true
                                7 -> mainCategory[6].isSelected = true
                                8 -> mainCategory[7].isSelected = true
                                9 -> mainCategory[8].isSelected = true
                                10 -> mainCategory[9].isSelected = true
                            }
                        } else {
//                            Log.e("TAG", "ELSEisSelected: " + it.isSelected + " :: "+it.isAll)
                        }
                    }



                    mainCategory.forEach {
//                        Log.e("TAG", "onBindHolder: " + it.isSelected)
                        if (it.isSelected) {
                            it.subCategory.forEach { sub ->
                                sub.isSelected = true
                            }
                        }
                    }


//                    currentList.forEach {
//                        Log.e("TAG", "currentList: " + it.isSelected)
//                    }

                    mainPrice.forEach {
                        it.isSelected = false
//                        it.isChildSelect = false
                    }
                    mainMaterial.forEach {
                        it.isSelected = false
//                        it.isChildSelect = false
                    }


                    root.findNavController().navigate(R.id.action_category_to_products)


                }
            }
        }
    }


    val subCategoryAdapter2 = object : GenericAdapter<ItemChildBinding, Items>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemChildBinding.inflate(inflater, parent, false)

        override fun onBindHolder(
            binding: ItemChildBinding,
            dataClass: Items,
            position: Int
        ) {
            binding.apply {
                if (dataClass.name.contains(" ")){
                    val txt = dataClass.name.split(" ")[0]+
                            "\n"+dataClass.name.split(" ")[1]
                    textName.text = txt
                } else {
                    textName.text = dataClass.name
                }
                layoutChild.singleClick {
                    mainCategory.forEach {
                        it.isSelected = false
                        it.subCategory.forEach {
                            it.isSelected = false
                        }
                    }

                    currentList.forEach {
                        it.isSelected = false
                    }
                    dataClass.isSelected = true

                    currentList.forEach {
                        if (it.isSelected && it.isAll) {
//                            Log.e("TAG", "IFisSelected: " + it.isSelected + " :: "+it.isAll + " :: "+mainSelect)
                            when (mainSelect) {
                                1 -> mainCategory[0].isSelected = true
                                2 -> mainCategory[1].isSelected = true
                                3 -> mainCategory[2].isSelected = true
                                4 -> mainCategory[3].isSelected = true
                                5 -> mainCategory[4].isSelected = true
                                6 -> mainCategory[5].isSelected = true
                                7 -> mainCategory[6].isSelected = true
                                8 -> mainCategory[7].isSelected = true
                                9 -> mainCategory[8].isSelected = true
                                10 -> mainCategory[9].isSelected = true
                            }
                        } else {
//                            Log.e("TAG", "ELSEisSelected: " + it.isSelected + " :: "+it.isAll)
                        }
                    }



                    mainCategory.forEach {
//                        Log.e("TAG", "onBindHolder: " + it.isSelected)
                        if (it.isSelected) {
                            it.subCategory.forEach { sub ->
                                sub.isSelected = true
                            }
                        }
                    }


//                    currentList.forEach {
//                        Log.e("TAG", "currentList: " + it.isSelected)
//                    }

                    mainPrice.forEach {
                        it.isSelected = false
//                        it.isChildSelect = false
                    }
                    mainMaterial.forEach {
                        it.isSelected = false
//                        it.isChildSelect = false
                    }


                    root.findNavController().navigate(R.id.action_category_to_products)


                }
            }
        }
    }


    val subCategoryAdapter3 = object : GenericAdapter<ItemChildBinding, Items>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemChildBinding.inflate(inflater, parent, false)

        override fun onBindHolder(
            binding: ItemChildBinding,
            dataClass: Items,
            position: Int
        ) {
            binding.apply {
                if (dataClass.name.contains(" ")){
                    val txt = dataClass.name.split(" ")[0]+
                            "\n"+dataClass.name.split(" ")[1]
                    textName.text = txt
                } else {
                    textName.text = dataClass.name
                }
                layoutChild.singleClick {
                    mainCategory.forEach {
                        it.isSelected = false
                        it.subCategory.forEach {
                            it.isSelected = false
                        }
                    }

                    currentList.forEach {
                        it.isSelected = false
                    }
                    dataClass.isSelected = true

                    currentList.forEach {
                        if (it.isSelected && it.isAll) {
//                            Log.e("TAG", "IFisSelected: " + it.isSelected + " :: "+it.isAll + " :: "+mainSelect)
                            when (mainSelect) {
                                1 -> mainCategory[0].isSelected = true
                                2 -> mainCategory[1].isSelected = true
                                3 -> mainCategory[2].isSelected = true
                                4 -> mainCategory[3].isSelected = true
                                5 -> mainCategory[4].isSelected = true
                                6 -> mainCategory[5].isSelected = true
                                7 -> mainCategory[6].isSelected = true
                                8 -> mainCategory[7].isSelected = true
                                9 -> mainCategory[8].isSelected = true
                                10 -> mainCategory[9].isSelected = true
                            }
                        } else {
//                            Log.e("TAG", "ELSEisSelected: " + it.isSelected + " :: "+it.isAll)
                        }
                    }



                    mainCategory.forEach {
//                        Log.e("TAG", "onBindHolder: " + it.isSelected)
                        if (it.isSelected) {
                            it.subCategory.forEach { sub ->
                                sub.isSelected = true
                            }
                        }
                    }


//                    currentList.forEach {
//                        Log.e("TAG", "currentList: " + it.isSelected)
//                    }

                    mainPrice.forEach {
                        it.isSelected = false
//                        it.isChildSelect = false
                    }
                    mainMaterial.forEach {
                        it.isSelected = false
//                        it.isChildSelect = false
                    }


                    root.findNavController().navigate(R.id.action_category_to_products)


                }
            }
        }
    }


    val subCategoryAdapter4 = object : GenericAdapter<ItemChildBinding, Items>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemChildBinding.inflate(inflater, parent, false)

        override fun onBindHolder(
            binding: ItemChildBinding,
            dataClass: Items,
            position: Int
        ) {
            binding.apply {
                if (dataClass.name.contains(" ")){
                    val txt = dataClass.name.split(" ")[0]+
                            "\n"+dataClass.name.split(" ")[1]
                    textName.text = txt
                } else {
                    textName.text = dataClass.name
                }
                layoutChild.singleClick {
                    mainCategory.forEach {
                        it.isSelected = false
                        it.subCategory.forEach {
                            it.isSelected = false
                        }
                    }

                    currentList.forEach {
                        it.isSelected = false
                    }
                    dataClass.isSelected = true

                    currentList.forEach {
                        if (it.isSelected && it.isAll) {
//                            Log.e("TAG", "IFisSelected: " + it.isSelected + " :: "+it.isAll + " :: "+mainSelect)
                            when (mainSelect) {
                                1 -> mainCategory[0].isSelected = true
                                2 -> mainCategory[1].isSelected = true
                                3 -> mainCategory[2].isSelected = true
                                4 -> mainCategory[3].isSelected = true
                                5 -> mainCategory[4].isSelected = true
                                6 -> mainCategory[5].isSelected = true
                                7 -> mainCategory[6].isSelected = true
                                8 -> mainCategory[7].isSelected = true
                                9 -> mainCategory[8].isSelected = true
                                10 -> mainCategory[9].isSelected = true
                            }
                        } else {
//                            Log.e("TAG", "ELSEisSelected: " + it.isSelected + " :: "+it.isAll)
                        }
                    }



                    mainCategory.forEach {
//                        Log.e("TAG", "onBindHolder: " + it.isSelected)
                        if (it.isSelected) {
                            it.subCategory.forEach { sub ->
                                sub.isSelected = true
                            }
                        }
                    }


//                    currentList.forEach {
//                        Log.e("TAG", "currentList: " + it.isSelected)
//                    }

                    mainPrice.forEach {
                        it.isSelected = false
//                        it.isChildSelect = false
                    }
                    mainMaterial.forEach {
                        it.isSelected = false
//                        it.isChildSelect = false
                    }


                    root.findNavController().navigate(R.id.action_category_to_products)


                }
            }
        }
    }


    val subCategoryAdapter5 = object : GenericAdapter<ItemChildBinding, Items>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemChildBinding.inflate(inflater, parent, false)

        override fun onBindHolder(
            binding: ItemChildBinding,
            dataClass: Items,
            position: Int
        ) {
            binding.apply {
                if (dataClass.name.contains(" ")){
                    val txt = dataClass.name.split(" ")[0]+
                            "\n"+dataClass.name.split(" ")[1]
                    textName.text = txt
                } else {
                    textName.text = dataClass.name
                }
                layoutChild.singleClick {
                    mainCategory.forEach {
                        it.isSelected = false
                        it.subCategory.forEach {
                            it.isSelected = false
                        }
                    }

                    currentList.forEach {
                        it.isSelected = false
                    }
                    dataClass.isSelected = true

                    currentList.forEach {
                        if (it.isSelected && it.isAll) {
//                            Log.e("TAG", "IFisSelected: " + it.isSelected + " :: "+it.isAll + " :: "+mainSelect)
                            when (mainSelect) {
                                1 -> mainCategory[0].isSelected = true
                                2 -> mainCategory[1].isSelected = true
                                3 -> mainCategory[2].isSelected = true
                                4 -> mainCategory[3].isSelected = true
                                5 -> mainCategory[4].isSelected = true
                                6 -> mainCategory[5].isSelected = true
                                7 -> mainCategory[6].isSelected = true
                                8 -> mainCategory[7].isSelected = true
                                9 -> mainCategory[8].isSelected = true
                                10 -> mainCategory[9].isSelected = true
                            }
                        } else {
//                            Log.e("TAG", "ELSEisSelected: " + it.isSelected + " :: "+it.isAll)
                        }
                    }



                    mainCategory.forEach {
//                        Log.e("TAG", "onBindHolder: " + it.isSelected)
                        if (it.isSelected) {
                            it.subCategory.forEach { sub ->
                                sub.isSelected = true
                            }
                        }
                    }


//                    currentList.forEach {
//                        Log.e("TAG", "currentList: " + it.isSelected)
//                    }

                    mainPrice.forEach {
                        it.isSelected = false
//                        it.isChildSelect = false
                    }
                    mainMaterial.forEach {
                        it.isSelected = false
//                        it.isChildSelect = false
                    }


                    root.findNavController().navigate(R.id.action_category_to_products)


                }
            }
        }
    }


    val subCategoryAdapter6 = object : GenericAdapter<ItemChildBinding, Items>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemChildBinding.inflate(inflater, parent, false)

        override fun onBindHolder(
            binding: ItemChildBinding,
            dataClass: Items,
            position: Int
        ) {
            binding.apply {
                if (dataClass.name.contains(" ")){
                    val txt = dataClass.name.split(" ")[0]+
                            "\n"+dataClass.name.split(" ")[1]
                    textName.text = txt
                } else {
                    textName.text = dataClass.name
                }
                layoutChild.singleClick {
                    mainCategory.forEach {
                        it.isSelected = false
                        it.subCategory.forEach {
                            it.isSelected = false
                        }
                    }

                    currentList.forEach {
                        it.isSelected = false
                    }
                    dataClass.isSelected = true

                    currentList.forEach {
                        if (it.isSelected && it.isAll) {
//                            Log.e("TAG", "IFisSelected: " + it.isSelected + " :: "+it.isAll + " :: "+mainSelect)
                            when (mainSelect) {
                                1 -> mainCategory[0].isSelected = true
                                2 -> mainCategory[1].isSelected = true
                                3 -> mainCategory[2].isSelected = true
                                4 -> mainCategory[3].isSelected = true
                                5 -> mainCategory[4].isSelected = true
                                6 -> mainCategory[5].isSelected = true
                                7 -> mainCategory[6].isSelected = true
                                8 -> mainCategory[7].isSelected = true
                                9 -> mainCategory[8].isSelected = true
                                10 -> mainCategory[9].isSelected = true
                            }
                        } else {
//                            Log.e("TAG", "ELSEisSelected: " + it.isSelected + " :: "+it.isAll)
                        }
                    }



                    mainCategory.forEach {
//                        Log.e("TAG", "onBindHolder: " + it.isSelected)
                        if (it.isSelected) {
                            it.subCategory.forEach { sub ->
                                sub.isSelected = true
                            }
                        }
                    }


//                    currentList.forEach {
//                        Log.e("TAG", "currentList: " + it.isSelected)
//                    }

                    mainPrice.forEach {
                        it.isSelected = false
//                        it.isChildSelect = false
                    }
                    mainMaterial.forEach {
                        it.isSelected = false
//                        it.isChildSelect = false
                    }


                    root.findNavController().navigate(R.id.action_category_to_products)


                }
            }
        }
    }


    val subCategoryAdapter7 = object : GenericAdapter<ItemChildBinding, Items>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemChildBinding.inflate(inflater, parent, false)

        override fun onBindHolder(
            binding: ItemChildBinding,
            dataClass: Items,
            position: Int
        ) {
            binding.apply {
                if (dataClass.name.contains(" ")){
                    val txt = dataClass.name.split(" ")[0]+
                            "\n"+dataClass.name.split(" ")[1]
                    textName.text = txt
                } else {
                    textName.text = dataClass.name
                }
                layoutChild.singleClick {
                    mainCategory.forEach {
                        it.isSelected = false
                        it.subCategory.forEach {
                            it.isSelected = false
                        }
                    }

                    currentList.forEach {
                        it.isSelected = false
                    }
                    dataClass.isSelected = true

                    currentList.forEach {
                        if (it.isSelected && it.isAll) {
//                            Log.e("TAG", "IFisSelected: " + it.isSelected + " :: "+it.isAll + " :: "+mainSelect)
                            when (mainSelect) {
                                1 -> mainCategory[0].isSelected = true
                                2 -> mainCategory[1].isSelected = true
                                3 -> mainCategory[2].isSelected = true
                                4 -> mainCategory[3].isSelected = true
                                5 -> mainCategory[4].isSelected = true
                                6 -> mainCategory[5].isSelected = true
                                7 -> mainCategory[6].isSelected = true
                                8 -> mainCategory[7].isSelected = true
                                9 -> mainCategory[8].isSelected = true
                                10 -> mainCategory[9].isSelected = true
                            }
                        } else {
//                            Log.e("TAG", "ELSEisSelected: " + it.isSelected + " :: "+it.isAll)
                        }
                    }



                    mainCategory.forEach {
//                        Log.e("TAG", "onBindHolder: " + it.isSelected)
                        if (it.isSelected) {
                            it.subCategory.forEach { sub ->
                                sub.isSelected = true
                            }
                        }
                    }


//                    currentList.forEach {
//                        Log.e("TAG", "currentList: " + it.isSelected)
//                    }

                    mainPrice.forEach {
                        it.isSelected = false
//                        it.isChildSelect = false
                    }
                    mainMaterial.forEach {
                        it.isSelected = false
//                        it.isChildSelect = false
                    }


                    root.findNavController().navigate(R.id.action_category_to_products)


                }
            }
        }
    }


    val subCategoryAdapter8 = object : GenericAdapter<ItemChildBinding, Items>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemChildBinding.inflate(inflater, parent, false)

        override fun onBindHolder(
            binding: ItemChildBinding,
            dataClass: Items,
            position: Int
        ) {
            binding.apply {
                if (dataClass.name.contains(" ")){
                    val txt = dataClass.name.split(" ")[0]+
                            "\n"+dataClass.name.split(" ")[1]
                    textName.text = txt
                } else {
                    textName.text = dataClass.name
                }
                layoutChild.singleClick {
                    mainCategory.forEach {
                        it.isSelected = false
                        it.subCategory.forEach {
                            it.isSelected = false
                        }
                    }

                    currentList.forEach {
                        it.isSelected = false
                    }
                    dataClass.isSelected = true

                    currentList.forEach {
                        if (it.isSelected && it.isAll) {
//                            Log.e("TAG", "IFisSelected: " + it.isSelected + " :: "+it.isAll + " :: "+mainSelect)
                            when (mainSelect) {
                                1 -> mainCategory[0].isSelected = true
                                2 -> mainCategory[1].isSelected = true
                                3 -> mainCategory[2].isSelected = true
                                4 -> mainCategory[3].isSelected = true
                                5 -> mainCategory[4].isSelected = true
                                6 -> mainCategory[5].isSelected = true
                                7 -> mainCategory[6].isSelected = true
                                8 -> mainCategory[7].isSelected = true
                                9 -> mainCategory[8].isSelected = true
                                10 -> mainCategory[9].isSelected = true
                            }
                        } else {
//                            Log.e("TAG", "ELSEisSelected: " + it.isSelected + " :: "+it.isAll)
                        }
                    }



                    mainCategory.forEach {
//                        Log.e("TAG", "onBindHolder: " + it.isSelected)
                        if (it.isSelected) {
                            it.subCategory.forEach { sub ->
                                sub.isSelected = true
                            }
                        }
                    }


//                    currentList.forEach {
//                        Log.e("TAG", "currentList: " + it.isSelected)
//                    }

                    mainPrice.forEach {
                        it.isSelected = false
//                        it.isChildSelect = false
                    }
                    mainMaterial.forEach {
                        it.isSelected = false
//                        it.isChildSelect = false
                    }


                    root.findNavController().navigate(R.id.action_category_to_products)


                }
            }
        }
    }


    val subCategoryAdapter9 = object : GenericAdapter<ItemChildBinding, Items>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemChildBinding.inflate(inflater, parent, false)

        override fun onBindHolder(
            binding: ItemChildBinding,
            dataClass: Items,
            position: Int
        ) {
            binding.apply {
                if (dataClass.name.contains(" ")){
                    val txt = dataClass.name.split(" ")[0]+
                            "\n"+dataClass.name.split(" ")[1]
                    textName.text = txt
                } else {
                    textName.text = dataClass.name
                }
                layoutChild.singleClick {
                    mainCategory.forEach {
                        it.isSelected = false
                        it.subCategory.forEach {
                            it.isSelected = false
                        }
                    }

                    currentList.forEach {
                        it.isSelected = false
                    }
                    dataClass.isSelected = true

                    currentList.forEach {
                        if (it.isSelected && it.isAll) {
//                            Log.e("TAG", "IFisSelected: " + it.isSelected + " :: "+it.isAll + " :: "+mainSelect)
                            when (mainSelect) {
                                1 -> mainCategory[0].isSelected = true
                                2 -> mainCategory[1].isSelected = true
                                3 -> mainCategory[2].isSelected = true
                                4 -> mainCategory[3].isSelected = true
                                5 -> mainCategory[4].isSelected = true
                                6 -> mainCategory[5].isSelected = true
                                7 -> mainCategory[6].isSelected = true
                                8 -> mainCategory[7].isSelected = true
                                9 -> mainCategory[8].isSelected = true
                                10 -> mainCategory[9].isSelected = true
                            }
                        } else {
//                            Log.e("TAG", "ELSEisSelected: " + it.isSelected + " :: "+it.isAll)
                        }
                    }



                    mainCategory.forEach {
//                        Log.e("TAG", "onBindHolder: " + it.isSelected)
                        if (it.isSelected) {
                            it.subCategory.forEach { sub ->
                                sub.isSelected = true
                            }
                        }
                    }


//                    currentList.forEach {
//                        Log.e("TAG", "currentList: " + it.isSelected)
//                    }

                    mainPrice.forEach {
                        it.isSelected = false
//                        it.isChildSelect = false
                    }
                    mainMaterial.forEach {
                        it.isSelected = false
//                        it.isChildSelect = false
                    }


                    root.findNavController().navigate(R.id.action_category_to_products)


                }
            }
        }
    }


    val subCategoryAdapter10 = object : GenericAdapter<ItemChildBinding, Items>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemChildBinding.inflate(inflater, parent, false)

        override fun onBindHolder(
            binding: ItemChildBinding,
            dataClass: Items,
            position: Int
        ) {
            binding.apply {
                if (dataClass.name.contains(" ")){
                    val txt = dataClass.name.split(" ")[0]+
                            "\n"+dataClass.name.split(" ")[1]
                    textName.text = txt
                } else {
                    textName.text = dataClass.name
                }
                layoutChild.singleClick {
                    mainCategory.forEach {
                        it.isSelected = false
                        it.subCategory.forEach {
                            it.isSelected = false
                        }
                    }

                    currentList.forEach {
                        it.isSelected = false
                    }
                    dataClass.isSelected = true

                    currentList.forEach {
                        if (it.isSelected && it.isAll) {
//                            Log.e("TAG", "IFisSelected: " + it.isSelected + " :: "+it.isAll + " :: "+mainSelect)
                            when (mainSelect) {
                                1 -> mainCategory[0].isSelected = true
                                2 -> mainCategory[1].isSelected = true
                                3 -> mainCategory[2].isSelected = true
                                4 -> mainCategory[3].isSelected = true
                                5 -> mainCategory[4].isSelected = true
                                6 -> mainCategory[5].isSelected = true
                                7 -> mainCategory[6].isSelected = true
                                8 -> mainCategory[7].isSelected = true
                                9 -> mainCategory[8].isSelected = true
                                10 -> mainCategory[9].isSelected = true
                            }
                        } else {
//                            Log.e("TAG", "ELSEisSelected: " + it.isSelected + " :: "+it.isAll)
                        }
                    }



                    mainCategory.forEach {
//                        Log.e("TAG", "onBindHolder: " + it.isSelected)
                        if (it.isSelected) {
                            it.subCategory.forEach { sub ->
                                sub.isSelected = true
                            }
                        }
                    }


//                    currentList.forEach {
//                        Log.e("TAG", "currentList: " + it.isSelected)
//                    }

                    mainPrice.forEach {
                        it.isSelected = false
//                        it.isChildSelect = false
                    }
                    mainMaterial.forEach {
                        it.isSelected = false
//                        it.isChildSelect = false
                    }


                    root.findNavController().navigate(R.id.action_category_to_products)


                }
            }
        }
    }
}
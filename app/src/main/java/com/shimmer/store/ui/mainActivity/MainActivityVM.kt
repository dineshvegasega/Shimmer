package com.shimmer.store.ui.mainActivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shimmer.store.models.Items
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MainActivityVM @Inject constructor() : ViewModel() {
    companion object {

        @JvmStatic
        var storeWebUrl: String = ""

        @JvmStatic
        var locale: Locale = Locale.getDefault()

//        var isHide: Boolean = false
//        var isHide = MutableLiveData<Boolean>()

        var badgeCount = MutableLiveData<Int>(6)

//        val filters = hashMapOf<String, Any?>()

//        val arrayPrice : ArrayList<String> = ArrayList()
//        val arrayCategory : ArrayList<String> = ArrayList()
//        val arrayMaterial : ArrayList<String> = ArrayList()
//        val arrayShopFor : ArrayList<String> = ArrayList()




        var isFilterFrom: Boolean = false

        var mainPrice: ArrayList<Items> = ArrayList()
        val mainCategory : ArrayList<Items> = ArrayList()
        var mainMaterial: ArrayList<Items> = ArrayList()
        var mainShopFor: ArrayList<Items> = ArrayList()

        init {
            mainPrice.add(Items(name = "₹1000 - ₹10000"))
            mainPrice.add(Items(name = "₹10000 - ₹15000"))
            mainPrice.add(Items(name = "₹15000 - ₹20000"))
            mainPrice.add(Items(name = "₹20000 - ₹30000"))
            mainPrice.add(Items(name = "₹30000 - ₹50000"))
            mainPrice.add(Items(name = "₹50000 - ₹100000"))
            mainPrice.add(Items(name = "₹100000 - ₹1000000"))
            mainPrice.add(Items(name = "₹1000000 - Above"))



            mainCategory.add(Items(id = 19, parent_id = 18, name = "RINGS",
                subCategory = arrayListOf(
                    Items(id = -1, parent_id = 19, name = "All Rings", isAll = true),
                    Items(id = 13, parent_id = 19, name = "Solitaire Rings"),
                    Items(id = 14, parent_id = 19, name = "Everyday Wear"),
                    Items(id = 15, parent_id = 19, name = "Cocktail Rings"),
                    Items(id = 16, parent_id = 19, name = "Engagement Rings"),
                    Items(id = 17, parent_id = 19, name = "Bands"))
            ))
            mainCategory.add(Items(id = 4, parent_id = 18, name = "EARRINGS",
                subCategory = arrayListOf(
                    Items(name = "All Earrings", isAll = true),
                    Items(name = "Long Necklace"),
                    Items(name = "Short Necklace"))
            ))
            mainCategory.add(Items(id = 5, parent_id = 18, name = "PENDANTS",
                subCategory = arrayListOf(
                    Items(name = "All Pendants", isAll = true),
                    Items(name = "Alphabets"),
                    Items(name = "Zodiac"),
                    Items(name = "Casual"),
                    Items(name = "Everyday"))
            ))
            mainCategory.add(Items(id = 6, parent_id = 18, name = "BRACELETS",
                subCategory = arrayListOf(
                    Items(name = "All Bracelets", isAll = true),
                    Items(name = "Casual"),
                    Items(name = "Bangles"),
                    Items(name = "Occasion"),
                    Items(name = "Everyday"))
            ))
            mainCategory.add(Items(id = 7, parent_id = 18, name = "MANGALSUTRA",
                subCategory = arrayListOf(
                    Items(name = "All Mangalsutras", isAll = true),
                    Items(name = "Casual"),
                    Items(name = "Bangles"),
                    Items(name = "Everyday"))
            ))
            mainCategory.add(Items(id = 8, parent_id = 18, name = "NOSEPINS",
                subCategory = arrayListOf(
                    Items(name = "All Nosepins", isAll = true),
                    Items(name = "Balis"),
                    Items(name = "Studs"),
                    Items(name = "Drops"),
                    Items(name = "Hoops"))
            ))
            mainCategory.add(Items(id = 9, parent_id = 18, name = "SOLITAIRE",
                subCategory = arrayListOf(
                    Items(name = "All Solitaire", isAll = true),
                    Items(name = "Balis"),
                    Items(name = "Studs"),
                    Items(name = "Drops"),
                    Items(name = "Hoops"))
            ))
            mainCategory.add(Items(id = 10, parent_id = 18, name = "ACCESSORIES",
                subCategory = arrayListOf(
                    Items(name = "All Accessories", isAll = true),
                    Items(name = "Balis"),
                    Items(name = "Studs"),
                    Items(name = "Drops"),
                    Items(name = "Hoops"))
            ))
            mainCategory.add(Items(id = 11, parent_id = 18, name = "BANGLES",
                subCategory = arrayListOf(
                    Items(name = "All Bangles", isAll = true),
                    Items(name = "Balis"),
                    Items(name = "Studs"),
                    Items(name = "Drops"),
                    Items(name = "Hoops"))
            ))
            mainCategory.add(Items(id = 12, parent_id = 18, name = "WATCHES",
                subCategory = arrayListOf(
                    Items(name = "All Watches", isAll = true),
                    Items(name = "Balis"),
                    Items(name = "Studs"),
                    Items(name = "Drops"),
                    Items(name = "Hoops"))
            ))



            mainMaterial.add(Items(name = "Gold 14 K"))
            mainMaterial.add(Items(name = "Gold 18 K"))
            mainMaterial.add(Items(name = "Platinum"))



            mainShopFor.add(Items(name = "Men"))
            mainShopFor.add(Items(name = "Women"))
            mainShopFor.add(Items(name = "Kids"))
        }
    }








}
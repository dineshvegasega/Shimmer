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
            mainPrice.add(Items("₹1000 - ₹10000"))
            mainPrice.add(Items("₹10000 - ₹15000"))
            mainPrice.add(Items("₹15000 - ₹20000"))
            mainPrice.add(Items("₹20000 - ₹30000"))
            mainPrice.add(Items("₹30000 - ₹50000"))
            mainPrice.add(Items("₹50000 - ₹100000"))
            mainPrice.add(Items("₹100000 - ₹1000000"))
            mainPrice.add(Items("₹1000000 - Above"))



            mainCategory.add(Items("RINGS",
                subCategory = arrayListOf(
                    Items("Band"),
                    Items("Casual"),
                    Items("Cocktail"),
                    Items("Engagement"),
                    Items("Solitaire"))
            ))
            mainCategory.add(Items("NECKLACE",
                subCategory = arrayListOf(
                    Items("Long Necklace"),
                    Items("Short Necklace"))
            ))
            mainCategory.add(Items("PENDANTS",
                subCategory = arrayListOf(
                    Items("Alphabets"),
                    Items("Zodiac"),
                    Items("Casual"),
                    Items("Everyday"))
            ))
            mainCategory.add(Items("BRACELETS",
                subCategory = arrayListOf(
                    Items("Casual"),
                    Items("Bangles"),
                    Items("Occasion"),
                    Items("Everyday"))
            ))
            mainCategory.add(Items("MANGALSUTRA",
                subCategory = arrayListOf(
                    Items("Casual"),
                    Items("Bangles"),
                    Items("Everyday"))
            ))
            mainCategory.add(Items("EARRINGS",
                subCategory = arrayListOf(
                    Items("Balis"),
                    Items("Studs"),
                    Items("Drops"),
                    Items("Hoops"))
            ))



            mainMaterial.add(Items("Gold 14 K"))
            mainMaterial.add(Items("Gold 18 K"))
            mainMaterial.add(Items("Platinum"))



            mainShopFor.add(Items("Men"))
            mainShopFor.add(Items("Women"))
            mainShopFor.add(Items("Kids"))
        }
    }








}
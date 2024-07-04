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

        val arrayPrice : ArrayList<String> = ArrayList()
        val arrayCategory : ArrayList<String> = ArrayList()
        val arrayMaterial : ArrayList<String> = ArrayList()
        val arrayShopFor : ArrayList<String> = ArrayList()

        val mainCategory : ArrayList<Items> = ArrayList()

        init {
            mainCategory.add(Items("RINGS",
                subCategory = arrayListOf(Items("All Rings"),
                    Items("Men's"),
                    Items("Band"),
                    Items("Casual"),
                    Items("Cocktail"),
                    Items("Engagement"),
                    Items("Solitaire"))
            ))
            mainCategory.add(Items("NECKLACE",
                subCategory = arrayListOf(Items("All Necklace"),
                    Items("Men's"),
                    Items("Long Necklace"),
                    Items("Short Necklace"),
                    Items("Short Necklace"))
            ))
            mainCategory.add(Items("PENDANTS",
                subCategory = arrayListOf(Items("All Pendants"),
                    Items("Alphabates"),
                    Items("Zodiac"),
                    Items("Casual"),
                    Items("Everyday"))
            ))
            mainCategory.add(Items("BRACELETS",
                subCategory = arrayListOf(Items("All Bracelets"),
                    Items("Casual"),
                    Items("Bangles"),
                    Items("Occasion"),
                    Items("Everyday"))
            ))
            mainCategory.add(Items("MANGALSUTRA",
                subCategory = arrayListOf(Items("All Mangalsutra"),
                    Items("Casual"),
                    Items("Bangles"),
                    Items("Everyday"))
            ))
            mainCategory.add(Items("EARRINGS",
                subCategory = arrayListOf(Items("All Earrings"),
                    Items("Balis"),
                    Items("Studs"),
                    Items("Drops"),
                    Items("Hoops"))
            ))
        }
    }








}
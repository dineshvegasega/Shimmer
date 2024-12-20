package com.klifora.franchise.ui.main.faq

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klifora.franchise.R
import com.klifora.franchise.databinding.ItemFaqBinding
import com.klifora.franchise.datastore.db.CartModel
import com.klifora.franchise.genericAdapter.GenericAdapter
import com.klifora.franchise.models.ItemFaq
import com.klifora.franchise.models.Items
import com.klifora.franchise.networking.Repository
import com.klifora.franchise.ui.mainActivity.MainActivity.Companion.db
import com.klifora.franchise.utils.singleClick
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FaqVM @Inject constructor(private val repository: Repository) : ViewModel() {

    var item1: ArrayList<ItemFaq> = ArrayList()


    init {
        item1.add(
            ItemFaq(
                que = "What types of Jewellery do you offer?",
                ans = "We offer a wide range of Jewellery including necklaces, rings, bracelets, earrings, and customized pieces made from high-quality materials such as gold, silver, and platinum, often adorned with diamonds, pearls, and other gemstones."
            )
        )
        item1.add(
            ItemFaq(
                que = "Do you offer custom Jewellery design?",
                ans = "Yes, we specialize in custom Jewellery design. Our expert designers will work with you to create a unique piece tailored to your preferences. Please contact us to schedule a consultation."
            )
        )
        item1.add(
            ItemFaq(
                que = "What is your return policy?",
                ans = "We accept returns within 30 days of purchase for most items, provided they are in their original condition and packaging. Custom or engraved Jewellery is non-returnable. Please refer to our full return policy for more details."
            )
        )
        item1.add(
            ItemFaq(
                que = "How do I take care of my Jewellery?",
                ans = "To maintain your Jewellery's brilliance, we recommend cleaning it with a soft cloth and mild soap, avoiding harsh chemicals, and storing it in a safe place when not in use. Our staff can provide specific care instructions based on the type of material."
            )
        )
        item1.add(
            ItemFaq(
                que = "Do you offer warranties on your Jewellery?",
                ans = "Yes, we offer warranties on our Jewellery to cover any manufacturing defects. Warranty periods vary by product. Please contact our customer service team for more details on warranties for specific items."
            )
        )


    }


    fun getCartCount(callBack: Int.() -> Unit) {
        viewModelScope.launch {
            val userList: List<CartModel>? = db?.cartDao()?.getAll()
            var countBadge = 0
            userList?.forEach {
                countBadge += it.quantity
            }
            callBack(countBadge)
        }
    }


    var selectedPosition = -1
    val faqAdapter = object : GenericAdapter<ItemFaqBinding, ItemFaq>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int,
        ) = ItemFaqBinding.inflate(inflater, parent, false)

        @SuppressLint("NotifyDataSetChanged")
        override fun onBindHolder(
            binding: ItemFaqBinding,
            dataClass: ItemFaq,
            position: Int,
        ) {
            binding.apply {
//                textDesc.visibility =
//                    if (dataClass.isCollapse == true) View.VISIBLE else View.GONE
//                ivHideShow.setImageDrawable(
//                    ContextCompat.getDrawable(
//                        root.context,
//                        if (dataClass.isCollapse == true) R.drawable.arrow_down else R.drawable.arrow_right
//                    )
//                )

                textDesc.visibility =
                    if (selectedPosition == position) View.VISIBLE else View.GONE
                ivHideShow.setImageDrawable(
                    ContextCompat.getDrawable(
                        root.context,
                        if (selectedPosition == position) R.drawable.arrow_down else R.drawable.arrow_right
                    )
                )

                textItem.text = dataClass.que
                textDesc.text = dataClass.ans

                root.singleClick {
                    if (selectedPosition == position) {
                        if (textDesc.isVisible == true) {
                            selectedPosition = -1
                        }
                        if (textDesc.isVisible == false) {
                            selectedPosition = position
                        }
                    } else {
                        selectedPosition = position
                    }
                    notifyDataSetChanged()
                }
            }
        }
    }


}
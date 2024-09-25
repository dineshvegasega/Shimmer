package com.shimmer.store.ui.main.products

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.shimmer.store.R
import com.shimmer.store.databinding.ItemLoadingBinding
import com.shimmer.store.BR
import com.shimmer.store.databinding.ItemHome2Binding
import com.shimmer.store.databinding.ItemProductBinding
import com.shimmer.store.datastore.db.CartModel
import com.shimmer.store.datastore.db.SearchModel
import com.shimmer.store.models.Items
import com.shimmer.store.models.products.ItemProduct
import com.shimmer.store.networking.IMAGE_URL
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.db
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.cartItemLiveData
import com.shimmer.store.utils.getPatternFormat
import com.shimmer.store.utils.glideImage
import com.shimmer.store.utils.glidePhotoView
import com.shimmer.store.utils.ioThread
import com.shimmer.store.utils.mainThread

class ProductsAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var counter = 0

    var itemModels: MutableList<ItemProduct> = ArrayList()


    lateinit var itemRowBinding2: ItemProductBinding


    private val item: Int = 0
    private val loading: Int = 1

    private var isLoadingAdded: Boolean = false
    private var retryPageLoad: Boolean = false

    private var errorMsg: String? = ""


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return  if(viewType == item){
            val binding: ItemProductBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_product, parent, false)
            itemRowBinding2 = binding
            TopMoviesVH(binding)
        }else{
            val binding: ItemLoadingBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_loading, parent, false)
            LoadingVH(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = itemModels[position]
        if(getItemViewType(position) == item){

            val myOrderVH: TopMoviesVH = holder as TopMoviesVH
//            myOrderVH.itemRowBinding.movieProgress.visibility = View.VISIBLE
            myOrderVH.bind(model, position)
        }else{
            val loadingVH: LoadingVH = holder as LoadingVH
            if (retryPageLoad) {
                loadingVH.itemRowBinding.loadmoreProgress.visibility = View.GONE
            } else {
                loadingVH.itemRowBinding.loadmoreProgress.visibility = View.VISIBLE
            }
        }
    }

    override fun getItemCount(): Int {
        return if (itemModels.size > 0) itemModels.size else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if(position == 0){
            item
        }else {
            if (position == itemModels.size - 1 && isLoadingAdded) {
                loading
            } else {
                item
            }
        }
    }


    inner class TopMoviesVH(binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        var itemRowBinding: ItemProductBinding = binding

        @SuppressLint("NotifyDataSetChanged", "SetTextI18n", "ClickableViewAccessibility")
        fun bind(obj: Any?, position: Int) {
            itemRowBinding.setVariable(BR._all, obj)
            itemRowBinding.executePendingBindings()
            val model = obj as ItemProduct

            itemRowBinding.textTitle.text = model.name
            itemRowBinding.textPrice.text = "₹ "+ getPatternFormat("1", model.price)

            (IMAGE_URL + if(model.media_gallery_entries.size > 0) model.media_gallery_entries[0].file else "").glideImage(itemRowBinding.ivIcon.context, itemRowBinding.ivIcon)

            itemRowBinding.ivIcon.setOnClickListener {
//                it.findNavController().navigate(R.id.action_products_to_productsDetail, Bundle().apply {
//                    putParcelable("model", model)
//                })
                it.findNavController().navigate(R.id.action_products_to_productsDetail, Bundle().apply {
                    putString("baseSku", model.sku)
                    putString("sku", "")
                })
            }

            itemRowBinding.ivAddCart.imageTintList = if(model.isSelected == true) ContextCompat.getColorStateList(itemRowBinding.root.context,R.color.app_color) else ContextCompat.getColorStateList(itemRowBinding.root.context,R.color._9A9A9A)


            itemRowBinding.ivAddCart.setOnClickListener {
                model.isSelected = !model.isSelected
                mainThread {
                    val newUser = CartModel(product_id = model.id, name = model.name, price = model.price, quantity = 1, sku = model.sku, currentTime = System.currentTimeMillis())
                    model.custom_attributes.forEach {
                        if (it.attribute_code == "size"){
                            newUser.apply {
                                this.size = ""+it.value
                            }
                        }


                        if (it.attribute_code == "metal_color"){
                            newUser.apply {
                                this.color = ""+it.value
                            }
                        }

                        if (it.attribute_code == "metal_type"){
                            Log.e("TAG", "metal_typeAAA " + it.value)
                            if (it.value == "12"){
                                newUser.apply {
                                    this.material_type = ""+it.value
                                }
                                model.custom_attributes.forEach { againAttributes ->
                                    if (againAttributes.attribute_code == "metal_purity"){
                                        Log.e("TAG", "metal_typeBBB " + againAttributes.value)
                                        newUser.apply {
                                            this.purity = ""+againAttributes.value
                                        }
                                    }
//                                    Log.e("TAG", "metal_typeBBB " + againAttributes.toString())
                                }
                            }
                        } else {
//                            Log.e("TAG", "metal_typeBBB " + it.value)
//                            model.custom_attributes.forEach { againAttributes ->
//                                if (againAttributes.attribute_code == "metal_purity"){
//                                    newUser.apply {
//                                        this.purity = ""+it.value
//                                    }
//                                }
//                            }
                        }
                    }
                    if(model.isSelected == true){
                        db?.cartDao()?.insertAll(newUser)
                    } else {
                        db?.cartDao()?.deleteById(newUser.product_id!!)
                    }
                    cartItemLiveData.value = true
                    notifyItemChanged(position)
                }
            }


            itemRowBinding.btAddCart.setOnClickListener {
                mainThread {
                    val newUser = CartModel(product_id = model.id, name = model.name, price = model.price, quantity = 1, currentTime = System.currentTimeMillis())
                    db?.cartDao()?.insertAll(newUser)
                    cartItemLiveData.value = true
                    it.findNavController().navigate(R.id.action_products_to_cart)
                }
            }



//            itemRowBinding.ivAddCart.visibility = if (model.type_id == "configurable") View.GONE else View.VISIBLE
//            itemRowBinding.btAddCart.visibility = if (model.type_id == "configurable") View.GONE else View.VISIBLE

        }
    }

    inner class LoadingVH(binding: ItemLoadingBinding) : RecyclerView.ViewHolder(binding.root) {
        var itemRowBinding: ItemLoadingBinding = binding
    }

    fun showRetry(show: Boolean, errorMsg: String) {
        retryPageLoad = show
        notifyItemChanged(itemModels.size - 1)
        this.errorMsg = errorMsg
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addAllSearch(movies: MutableList<ItemProduct>) {
        itemModels.clear()
        itemModels.addAll(movies)
//        for(movie in movies){
//            add(movie)
//        }
        notifyDataSetChanged()
    }

    fun addAll(movies: MutableList<ItemProduct>) {
        for(movie in movies){
            add(movie)
        }
    }

    fun add(moive: ItemProduct) {
        itemModels.add(moive)
        notifyItemInserted(itemModels.size - 1)
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
//        add(ItemLiveScheme())
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false

//        val position: Int =itemModels.size -1
//        val movie: ItemLiveScheme = itemModels[position]
//
//        if(movie != null){
//            itemModels.removeAt(position)
//            notifyItemRemoved(position)
//        }
    }


    fun submitData(itemMainArray: ArrayList<ItemProduct>) {
        itemModels = itemMainArray
    }


    @SuppressLint("NotifyDataSetChanged")
    fun updatePosition(position: Int) {
        counter = position
        Log.e("TAG", "updatePosition " + position)
//        itemRowBinding2.apply {
//            if (isHide) {
//                baseButtons.visibility = View.GONE
//                group.visibility = View.VISIBLE
//            } else {
//                baseButtons.visibility = View.VISIBLE
//                group.visibility = View.GONE
//            }
//        }
//        notifyItemChanged(position)

    }



}
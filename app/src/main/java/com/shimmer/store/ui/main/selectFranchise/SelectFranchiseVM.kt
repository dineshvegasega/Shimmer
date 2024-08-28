package com.shimmer.store.ui.main.selectFranchise

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.shimmer.store.R
import com.shimmer.store.databinding.ItemSelectFranchiseBinding
import com.shimmer.store.datastore.db.SearchModel
import com.shimmer.store.genericAdapter.GenericAdapter
import com.shimmer.store.models.ItemFranchise
import com.shimmer.store.utils.mainThread
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class SelectFranchiseVM @Inject constructor() : ViewModel() {

    var selectedPosition = -1
    val franchiseListAdapter = object : GenericAdapter<ItemSelectFranchiseBinding, ItemFranchise>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemSelectFranchiseBinding.inflate(inflater, parent, false)

        override fun onBindHolder(
            binding: ItemSelectFranchiseBinding,
            dataClass: ItemFranchise,
            position: Int
        ) {
            binding.apply {
                root.singleClick {
                    selectedPosition = position
                    notifyDataSetChanged()
//                    dataClass.isSelected = !dataClass.isSelected

//                    mainThread {
//                        currentList.forEach {
//                            binding.layoutTop.setBackgroundResource(R.drawable.bg_all_round_franchise)
//                        }
//
//                        binding.layoutTop.backgroundTintList =
//                            ColorStateList.valueOf(
//                                ContextCompat.getColor(
//                                    binding.root.context,
//                                    R.color._B9F2FF
//                                )
//                            )
//                    }
                }

                if (selectedPosition == position) {
                    binding.layoutTop.backgroundTintList =
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                binding.root.context,
                                R.color._B9F2FF
                            )
                        )
                } else {
//                    binding.layoutTop.setBackgroundResource(R.drawable.all_round)
                    binding.layoutTop.setBackgroundResource(R.drawable.bg_all_round_franchise)
                    binding.layoutTop.backgroundTintList = null
                }

                //textItem.text = dataClass.search_name
//                textDesc.visibility =
//                    if (dataClass.isCollapse == true) View.VISIBLE else View.GONE
//                ivHideShow.setImageDrawable(
//                    ContextCompat.getDrawable(
//                        root.context,
//                        if (dataClass.isCollapse == true) R.drawable.baseline_remove_24 else R.drawable.baseline_add_24
//                    )
//                )

//                textItem.singleClick {
//                    searchValue.value = dataClass.search_name
//                }


//
//                ivCross.singleClick {
//                    mainThread {
//                        db?.searchDao()?.delete(dataClass)
//                        searchDelete.value = true
//                    }
//                }
            }
        }
    }



}
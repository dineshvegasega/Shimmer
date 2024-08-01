package com.shimmer.store.ui.main.franchiseList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import com.shimmer.store.databinding.ItemSearchHistoryBinding
import com.shimmer.store.datastore.db.SearchModel
import com.shimmer.store.genericAdapter.GenericAdapter
import com.shimmer.store.ui.mainActivity.MainActivity.Companion.db
import com.shimmer.store.utils.mainThread
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FranchiseListVM @Inject constructor() : ViewModel() {

    val franchiseListAdapter = object : GenericAdapter<ItemSearchHistoryBinding, SearchModel>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemSearchHistoryBinding.inflate(inflater, parent, false)

        override fun onBindHolder(
            binding: ItemSearchHistoryBinding,
            dataClass: SearchModel,
            position: Int
        ) {
            binding.apply {
                textItem.text = dataClass.search_name
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
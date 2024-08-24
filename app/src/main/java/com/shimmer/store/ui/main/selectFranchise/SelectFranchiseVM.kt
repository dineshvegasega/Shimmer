package com.shimmer.store.ui.main.selectFranchise

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import com.shimmer.store.databinding.ItemSelectFranchiseBinding
import com.shimmer.store.datastore.db.SearchModel
import com.shimmer.store.genericAdapter.GenericAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SelectFranchiseVM @Inject constructor() : ViewModel() {

    val franchiseListAdapter = object : GenericAdapter<ItemSelectFranchiseBinding, SearchModel>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemSelectFranchiseBinding.inflate(inflater, parent, false)

        override fun onBindHolder(
            binding: ItemSelectFranchiseBinding,
            dataClass: SearchModel,
            position: Int
        ) {
            binding.apply {
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
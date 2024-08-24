package com.shimmer.store.ui.main.searchOrder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.shimmer.store.R
import com.shimmer.store.databinding.ItemSearchOrderBinding
import com.shimmer.store.datastore.db.SearchModel
import com.shimmer.store.genericAdapter.GenericAdapter
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchOrderVM @Inject constructor() : ViewModel() {

    val searchOrderAdapter = object : GenericAdapter<ItemSearchOrderBinding, SearchModel>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemSearchOrderBinding.inflate(inflater, parent, false)

        override fun onBindHolder(
            binding: ItemSearchOrderBinding,
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

                root.singleClick {
                    root.findNavController().navigate(R.id.action_searchOrder_to_orderDetail)
                }


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
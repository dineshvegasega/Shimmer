package com.klifora.franchise.ui.main.complaintsFeedback.history

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.klifora.franchise.R
import com.klifora.franchise.databinding.ItemHistoryBinding
import com.klifora.franchise.genericAdapter.GenericAdapter
import com.klifora.franchise.models.ItemComplaint
import com.klifora.franchise.models.ItemComplaintItem
import com.klifora.franchise.models.ItemHistory
import com.klifora.franchise.models.products.ItemProductRoot
import com.klifora.franchise.networking.ApiInterface
import com.klifora.franchise.networking.CallHandler
import com.klifora.franchise.networking.Repository
import com.klifora.franchise.ui.mainActivity.MainActivityVM.Companion.storeWebUrl
import com.klifora.franchise.utils.changeDateFormat
import com.klifora.franchise.utils.sessionExpired
import com.klifora.franchise.utils.showSnackBar
import com.klifora.franchise.utils.singleClick
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HistoryVM @Inject constructor(private val repository: Repository) : ViewModel() {
    var itemModels: MutableList<ItemHistory> = ArrayList()

//    val adapter by lazy { HistoryAdapter(this) }

    var isSelectedButton : Int = 1

    val complaintAdapter = object : GenericAdapter<ItemHistoryBinding, ItemComplaintItem>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemHistoryBinding.inflate(inflater, parent, false)

        @SuppressLint("SetTextI18n")
        override fun onBindHolder(
            binding: ItemHistoryBinding,
            dataClass: ItemComplaintItem,
            position: Int
        ) {
            binding.apply {
//                val title = when(dataClass.category_id) {
//                    "1" -> {
//                        "Complaint"
//                    }
//                    "2" -> {
//                        "Feedback"
//                    }
//                    else -> {
//                        "Complaint"
//                    }
//                }
                textTitle.text = "Complaint"
                textDesc.text = dataClass.subject
                textTrackValue.text = dataClass.ticket_id
                val date =
                    dataClass.updated_at.changeDateFormat("yyyy-MM-dd HH:mm:ss", "dd-MMM-yyyy")
                textValidDateValue.text = date

                if (dataClass.status_id == "0") {
                    btStatus.text = "Resolved"
                    btStatus.backgroundTintList =
                        ContextCompat.getColorStateList(binding.root.context, R.color._138808)
                } else if (dataClass.status_id == "1") {
                    btStatus.text = "In Progress"
                    btStatus.backgroundTintList =
                        ContextCompat.getColorStateList(binding.root.context, R.color._E87103)
                } else {
                    btStatus.text = "Resolved"
                    btStatus.backgroundTintList =
                        ContextCompat.getColorStateList(binding.root.context, R.color._138808)
                }


                root.singleClick {
                    root.findNavController()
                        .navigate(R.id.action_history_to_historyDetail, Bundle().apply {
                            putParcelable("key", dataClass)
                        })
                }
            }
        }
    }

    private var itemComplaintResult = MutableLiveData<ItemComplaint>()
    val itemComplaint: LiveData<ItemComplaint> get() = itemComplaintResult

    fun getComplaint(_id: String) =
        viewModelScope.launch {
            repository.callApi(
                callHandler = object : CallHandler<Response<ItemComplaint>> {
                    override suspend fun sendRequest(apiInterface: ApiInterface) =
                        apiInterface.complaintList(storeWebUrl, _id)

                    @SuppressLint("SuspiciousIndentation")
                    override fun success(response: Response<ItemComplaint>) {
                        Log.e("TAG", "success222:")
                        if (response.isSuccessful) {
                            try {
                                Log.e("TAG", "successAA: ${response.body().toString()}")
//                                val mMineUserEntity =
//                                    Gson().fromJson(response.body(), ItemProductRoot::class.java)
                                itemComplaintResult.value = response.body()
                            } catch (_: Exception) {
                            }
                        }
                    }

                    override fun error(message: String) {
                        Log.e("TAG", "success333:")
//                        Log.e("TAG", "successAA: ${message}")
//                        super.error(message)
                        showSnackBar(message)
//                        callBack(message.toString())

                        if (message.contains("fieldName")) {
                            showSnackBar("Something went wrong!")
                        } else {
//                            sessionExpired()
                        }

                    }

                    override fun loading() {
                        super.loading()
                    }
                }
            )
        }





    val feedbackAdapter = object : GenericAdapter<ItemHistoryBinding, ItemComplaintItem>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemHistoryBinding.inflate(inflater, parent, false)

        @SuppressLint("SetTextI18n")
        override fun onBindHolder(
            binding: ItemHistoryBinding,
            dataClass: ItemComplaintItem,
            position: Int
        ) {
            binding.apply {
//                val title = when(dataClass.category_id) {
//                    "1" -> {
//                        "Complaint"
//                    }
//                    "2" -> {
//                        "Feedback"
//                    }
//                    else -> {
//                        "Complaint"
//                    }
//                }
                textTitle.text = "Feedback"
                textDesc.text = dataClass.subject
                textTrackValue.text = dataClass.ticket_id
                val date =
                    dataClass.updated_at.changeDateFormat("yyyy-MM-dd HH:mm:ss", "dd-MMM-yyyy")
                textValidDateValue.text = date

                if (dataClass.status_id == "0") {
                    btStatus.text = "Resolved"
                    btStatus.backgroundTintList =
                        ContextCompat.getColorStateList(binding.root.context, R.color._138808)
                } else if (dataClass.status_id == "1") {
                    btStatus.text = "In Progress"
                    btStatus.backgroundTintList =
                        ContextCompat.getColorStateList(binding.root.context, R.color._E87103)
                } else {
                    btStatus.text = "Resolved"
                    btStatus.backgroundTintList =
                        ContextCompat.getColorStateList(binding.root.context, R.color._138808)
                }


                root.singleClick {
                    root.findNavController()
                        .navigate(R.id.action_history_to_historyDetail, Bundle().apply {
                            putParcelable("key", dataClass)
                        })
                }
            }
        }
    }

    private var itemFeedbackResult = MutableLiveData<ItemComplaint>()
    val itemFeedback: LiveData<ItemComplaint> get() = itemFeedbackResult

    fun getFeedback(_id: String) =
        viewModelScope.launch {
            repository.callApi(
                callHandler = object : CallHandler<Response<ItemComplaint>> {
                    override suspend fun sendRequest(apiInterface: ApiInterface) =
                        apiInterface.complaintList(storeWebUrl, _id)

                    @SuppressLint("SuspiciousIndentation")
                    override fun success(response: Response<ItemComplaint>) {
                        Log.e("TAG", "success222:")
                        if (response.isSuccessful) {
                            try {
                                Log.e("TAG", "successAA: ${response.body().toString()}")
//                                val mMineUserEntity =
//                                    Gson().fromJson(response.body(), ItemProductRoot::class.java)
                                itemFeedbackResult.value = response.body()
                            } catch (_: Exception) {
                            }
                        }
                    }

                    override fun error(message: String) {
                        Log.e("TAG", "success333:")
//                        Log.e("TAG", "successAA: ${message}")
//                        super.error(message)
                        showSnackBar(message)
//                        callBack(message.toString())

                        if (message.contains("fieldName")) {
                            showSnackBar("Something went wrong!")
                        } else {
//                            sessionExpired()
                        }

                    }

                    override fun loading() {
                        super.loading()
                    }
                }
            )
        }

}
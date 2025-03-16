package com.filangladminlpuveggi.lpuveggii.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.filangladminlpuveggi.lpuveggii.Orders.Order

import com.filangladminlpuveggi.lpuveggii.modelClass.mainPageScrollImagesModelClass
import com.filangladminlpuveggi.lpuveggii.modelClass.products
import com.filangladminlpuveggi.lpuveggii.utils.Resourse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class OrderViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _allOrder = MutableStateFlow<Resourse<List<Order>>>(Resourse.UnSpecified())
    val allOrder = _allOrder.asStateFlow()

    init {
        getAllOrders()
    }

    fun getAllOrders(){
        viewModelScope.launch{
            _allOrder.emit(Resourse.Loading())
        }

             firestore.collection("user/${auth.uid!!}/orders")
                .get().addOnSuccessListener {
                    var data = it.toObjects(Order::class.java)
                    viewModelScope.launch {
                        _allOrder.emit(Resourse.Success(data))
                    }

                }.addOnFailureListener {
                    viewModelScope.launch {
                        _allOrder.emit(Resourse.Error(it.toString()))
                    }
                }




    }


}
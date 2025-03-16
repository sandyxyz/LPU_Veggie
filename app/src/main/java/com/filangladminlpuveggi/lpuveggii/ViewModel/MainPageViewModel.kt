package com.filangladminlpuveggi.lpuveggii.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class MainPageViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
) : ViewModel() {
    private val _specialProduct = MutableStateFlow<Resourse<List<products>>>(Resourse.UnSpecified())
    val specialProducts : StateFlow<Resourse<List<products>>>  = _specialProduct

    private val _bestProducts = MutableStateFlow<Resourse<List<products>>>(Resourse.UnSpecified())
    val bestProducts : StateFlow<Resourse<List<products>>>  = _bestProducts

    private val _scrollImages = MutableStateFlow<Resourse<List<mainPageScrollImagesModelClass>>>(Resourse.UnSpecified())
    val scrollImages : StateFlow<Resourse<List<mainPageScrollImagesModelClass>>>  = _scrollImages


    private val paging =  PaginInfo()

    init {
        fetchspecialProducts()
        fetchbestProducts()
        getscrollimages()

    }

    private fun getscrollimages() {
        viewModelScope.launch {
            _scrollImages.emit(Resourse.Loading())
        }
        firestore.collection("Main Page Scroller")
            .get()
            .addOnSuccessListener {result ->
                val specialProductslist = result.toObjects(mainPageScrollImagesModelClass::class.java)
                viewModelScope.launch {
                    _scrollImages.emit(Resourse.Success(specialProductslist))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _scrollImages.emit(Resourse.Error("scroll"))
                }
            }
    }


    fun fetchspecialProducts(){
        viewModelScope.launch {
            _specialProduct.emit(Resourse.Loading())
        }
        firestore.collection("Products")
           .get()
            .addOnSuccessListener {result ->
                val specialProductslist = result.toObjects(products::class.java)
                viewModelScope.launch {
                    _specialProduct.emit(Resourse.Success(specialProductslist))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _specialProduct.emit(Resourse.Error("specialp"))
                }
            }
    }
     fun fetchbestProducts() {
         if (!paging.isPagingEnd) {
             viewModelScope.launch {
                 _bestProducts.emit(Resourse.Loading())
             }
             firestore.collection("Products")
                 .limit(paging.baseProductPage * 10).orderBy(
                     "id",
                     Query.Direction.ASCENDING
                 ).get()
                 .addOnSuccessListener { result ->

                     val specialProductslist = result.toObjects(products::class.java)
                     paging.isPagingEnd = specialProductslist == paging.oldBestProducts
                     paging.oldBestProducts = specialProductslist

                     viewModelScope.launch {
                         _bestProducts.emit(Resourse.Success(specialProductslist))
                     }
                     paging.baseProductPage++
                 }
                 .addOnFailureListener {
                     viewModelScope.launch {
                         _bestProducts.emit(Resourse.Error("bestPr"))
                     }
                 }
         }
     }
}

internal data class PaginInfo(
    var baseProductPage : Long = 1,
    var oldBestProducts : List<products> =  emptyList(),
    var isPagingEnd :  Boolean = false

)

//you can also make category of product by using .OrderBy( "id" , Query.Direction.ASCENDING)
//but first create index in firestore
package com.filangladminlpuveggi.lpuveggii.modelClass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class products(
    val id: String,
    val name2 : String,
    val name: String,
    val category: String,
    val price: String,
    val size: String,
    val offerPercentage: Float? = null,
    val description: String? = null,
    val sizes: List<String>? = null,
    val images: List<String>
): Parcelable {
    constructor(): this("0","","","","","", images = emptyList())
}
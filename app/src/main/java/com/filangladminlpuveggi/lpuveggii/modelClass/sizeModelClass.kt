package com.filangladminlpuveggi.lpuveggii.modelClass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class sizeModelClass(

    val size: String,
    val price: String,
) : Parcelable {
    constructor(): this("" , "")
}
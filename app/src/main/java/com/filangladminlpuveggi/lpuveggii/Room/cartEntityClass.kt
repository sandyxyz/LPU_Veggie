package com.filangladminlpuveggi.lpuveggii.Room

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity
@Parcelize
data class cartEntityClass(

    @PrimaryKey(autoGenerate = true)
    var id : Int = 0,

    @ColumnInfo("name")var name : String,
    @ColumnInfo("price") var price : String,
    @ColumnInfo("quantity")var quantity :  String,
    @ColumnInfo("image")var image :  String,
    @ColumnInfo("unit")var unit :  String,

) :  Parcelable{
    constructor() : this(0,"","","","","")
}

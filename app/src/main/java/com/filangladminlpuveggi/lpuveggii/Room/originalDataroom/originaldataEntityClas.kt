package com.filangladminlpuveggi.lpuveggii.Room.originalDataroom

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class originaldataEntityClas(

    @PrimaryKey(autoGenerate = true)
    var id : Int = 0,

    @ColumnInfo("name")var name : String,
    @ColumnInfo("price") var price : String,
    @ColumnInfo("quantity")var quantity :  String,
    @ColumnInfo("image")var image :  String,
    @ColumnInfo("unit")var unit :  String,

    )

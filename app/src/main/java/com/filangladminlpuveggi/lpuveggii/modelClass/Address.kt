package com.filangladminlpuveggi.lpuveggii.modelClass

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity
data class Address(
    @PrimaryKey
    var id : Int = 1,

    @ColumnInfo("addressTitle")var addressTitle : String,
    @ColumnInfo("Name") var Name : String,
    @ColumnInfo("street")var street : String,
    @ColumnInfo("phone") var phone : String,
    @ColumnInfo("city")var city : String,
    @ColumnInfo("state")var state : String,
)

package com.filangladminlpuveggi.lpuveggii.Room.PhoneNumber

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class phnoModel(
    @PrimaryKey(autoGenerate = true)
    var id : Int  = 0,

    @ColumnInfo("number")
    var number: String,
)
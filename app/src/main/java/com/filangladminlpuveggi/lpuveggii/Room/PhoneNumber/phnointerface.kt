package com.filangladminlpuveggi.lpuveggii.Room.PhoneNumber

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface phnointerface {
    @Insert
    fun insert(ins :  phnoModel)


    @Query("Select * from phnoModel")
    fun getNumber() : List<phnoModel>

}
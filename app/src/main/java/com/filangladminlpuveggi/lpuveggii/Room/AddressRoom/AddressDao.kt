package com.filangladminlpuveggi.lpuveggii.Room.AddressRoom

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.filangladminlpuveggi.lpuveggii.modelClass.Address

@Dao
interface AddressDao  {

    @Insert
    fun insertAddress(add : Address)

    @Query("Select * from Address")
    fun getAllAddress() : List<Address>

    @Update
    fun update(add : Address)

}
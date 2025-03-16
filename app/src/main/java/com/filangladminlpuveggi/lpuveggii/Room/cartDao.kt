package com.filangladminlpuveggi.lpuveggii.Room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import java.nio.channels.SelectableChannel


@Dao
interface cartDao {
   @Query("Select * from cartEntityClass")
   fun getdata() : List<cartEntityClass>

   @Query("Delete from cartEntityClass where name = :item")
   fun deleteItem(item : String)

    @Query("Update cartEntityClass Set price = :price ,  quantity = :size , unit = :unit where name = :name")
    fun update(name : String , size : String , price : String , unit : String)

    @Query("Select quantity from cartEntityClass where name = :name")
    fun getSizeQuantity(name : String ): String

    @Query("Select unit from cartEntityClass where name = :name")
    fun getQuantity(name : String ): String

    @Update
    fun updateData(cartEntityClass: cartEntityClass)

    @Query("Select name from cartEntityClass ")
    fun returnname() : List<String>


    @Query("Select price from cartEntityClass ")
    fun returnprice() : List<String>


    @Insert
    fun insert(cartEntityClass: cartEntityClass)

    @Query("Delete from cartEntityClass ")
    fun delete()

}
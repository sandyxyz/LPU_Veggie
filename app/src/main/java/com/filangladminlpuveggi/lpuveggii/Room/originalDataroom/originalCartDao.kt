package com.filangladminlpuveggi.lpuveggii.Room.originalDataroom

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.filangladminlpuveggi.lpuveggii.Room.cartEntityClass

@Dao
interface originalCartDao {

        @Query("Select * from originaldataEntityClas")
        fun getdatafromOrininalData() : List<originaldataEntityClas>

        @Query("Delete from originaldataEntityClas where name = :item")
        fun deleteItemfromOriginalData(item : String)


        @Update
        fun updateDataFromOriginalData(cartEntityClass: originaldataEntityClas)

        @Query("Select name from originaldataEntityClas ")
        fun returnnamefromOriginalData() : List<String>


        @Query("Select price from originaldataEntityClas ")
        fun returnpricefromOriginalData() : List<String>

        @Query("Select quantity from originaldataEntityClas  where name = :name")
        fun returnSize(name : String) : String

        @Insert
        fun insertInOriginalData(cartEntityClass: originaldataEntityClas)

        @Query("Delete from originaldataEntityClas ")
        fun delete()

}
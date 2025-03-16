package com.filangladminlpuveggi.lpuveggii.Room.AddressRoom

import androidx.room.Database
import androidx.room.RoomDatabase
import com.filangladminlpuveggi.lpuveggii.modelClass.Address


@Database(entities = [Address::class] , version = 2)
abstract class addressDatabase : RoomDatabase()  {
    abstract fun getAllAddress() : AddressDao
}
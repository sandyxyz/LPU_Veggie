package com.filangladminlpuveggi.lpuveggii.Room

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [cartEntityClass::class] ,version = 3 )
abstract class cartDatabase : RoomDatabase() {
     abstract fun getDao() : cartDao
}
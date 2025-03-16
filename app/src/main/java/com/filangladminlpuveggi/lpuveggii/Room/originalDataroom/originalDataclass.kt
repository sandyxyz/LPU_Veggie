package com.filangladminlpuveggi.lpuveggii.Room.originalDataroom

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [originaldataEntityClas::class] , version = 1)
abstract class originalDataclass  : RoomDatabase(){
    abstract fun getDao() : originalCartDao
}
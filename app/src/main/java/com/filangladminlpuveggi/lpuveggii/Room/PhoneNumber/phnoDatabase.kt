package com.filangladminlpuveggi.lpuveggii.Room.PhoneNumber

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [phnoModel::class] , version = 1)
abstract class phnoDatabase : RoomDatabase() {
    abstract fun getAllPhone() : phnointerface
}
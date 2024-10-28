package com.schedulev2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.schedulev2.database.converters.ScheduleTypeConverter
import com.schedulev2.database.dao.ScheduleDao
import com.schedulev2.database.entities.ScheduleDbo

@Database(entities = [ScheduleDbo::class], version = 3, exportSchema = false)
@TypeConverters(value = [ScheduleTypeConverter::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun scheduleDao(): ScheduleDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }


        private const val DATABASE_NAME = "ScheduleApp_db"
    }
}
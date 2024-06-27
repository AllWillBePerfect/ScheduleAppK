package com.example.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.database.dao.ScheduleDao
import com.example.database.entities.ScheduleDbo
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class Database_CasesTest {

    private lateinit var scheduleDao: ScheduleDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        scheduleDao = db.scheduleDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList() {
        scheduleDao.insertSchedule(ScheduleDbo.createEmpty())
        val allResult = scheduleDao.getAllSchedulesSingle().blockingGet()
        val result = scheduleDao.findCompareWeekSingle("").blockingGet()

        Assert.assertEquals(allResult[0], result)

    }

    @Test
    @Throws(Exception::class)
    fun removeSchedule() {
        scheduleDao.insertSchedule(ScheduleDbo.createEmpty(name = "КТбо4-11"))
        scheduleDao.insertSchedule(ScheduleDbo.createEmpty(name = "КТбо4-12"))
        scheduleDao.insertSchedule(ScheduleDbo.createEmpty(name = "КТбо4-1"))
        scheduleDao.deleteScheduleByName("КТбо4-1")
        val allResult = scheduleDao.getAllSchedulesSingle().blockingGet()
        val res1 = scheduleDao.getScheduleByGroupSingle("КТбо4-11").blockingGet()
        val res2 = scheduleDao.getScheduleByGroupSingle("КТбо4-12").blockingGet()
        Assert.assertEquals(allResult, listOf(res1, res2))

    }
}
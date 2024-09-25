package com.schedule.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.schedule.database.entities.ScheduleDbo
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface ScheduleDao {

    @Query("SELECT * FROM ${ScheduleDbo.TABLE_NAME}")
    fun getAllSchedulesSingle(): Single<List<ScheduleDbo>>

    @Query("SELECT * FROM ${ScheduleDbo.TABLE_NAME}")
    fun getAllSchedulesObservable(): Observable<List<ScheduleDbo>>

    @Query("SELECT * FROM ${ScheduleDbo.TABLE_NAME}")
    fun getAllSchedulesFlowable(): Flowable<List<ScheduleDbo>>

    @Query("SELECT * FROM ${ScheduleDbo.TABLE_NAME} WHERE name = :group ")
    fun getScheduleByGroupSingle(group: String): Single<ScheduleDbo>

    @Query("SELECT * FROM ${ScheduleDbo.TABLE_NAME} WHERE name = :group")
    fun getScheduleByGroupObservable(group: String): Observable<ScheduleDbo>

    @Query("SELECT * FROM ${ScheduleDbo.TABLE_NAME} WHERE name = :group")
    fun getScheduleByGroupFlowable(group: String): Flowable<ScheduleDbo>

    @Query("SELECT * FROM ${ScheduleDbo.TABLE_NAME} WHERE name = :group and week = :week")
    fun getScheduleByWeekSingle(group: String, week: Int): Single<ScheduleDbo>

    @Query("SELECT * FROM ${ScheduleDbo.TABLE_NAME} WHERE name = :group and week = :week")
    fun getScheduleByWeekObservable(group: String, week: Int): Observable<ScheduleDbo>

    @Query("SELECT * FROM ${ScheduleDbo.TABLE_NAME} WHERE name = :name and isCompareTable = 1")
    fun findCompareWeekSingle(name: String): Single<ScheduleDbo>

    @Query("SELECT * FROM ${ScheduleDbo.TABLE_NAME} WHERE name = :name and isCompareTable = 1")
    fun findCompareWeekObservable(name: String): Observable<ScheduleDbo>

    @Query("SELECT * FROM ${ScheduleDbo.TABLE_NAME} WHERE name = :name and isCompareTable = 1")
    fun findCompareWeekFlowable(name: String): Flowable<ScheduleDbo>




    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSchedule(entity: ScheduleDbo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertScheduleSingle(entity: ScheduleDbo): Single<Long>

    @Query("DELETE FROM ${ScheduleDbo.TABLE_NAME}")
    fun nukeTable()

    @Query("DELETE FROM ${ScheduleDbo.TABLE_NAME} where name = :name")
    fun deleteScheduleByName(name: String)

    @Query("DELETE FROM ${ScheduleDbo.TABLE_NAME} WHERE name = :name and isCompareTable = 1")
    fun deleteCompareTableTransactionPart(name: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCompareTableTransactionPart(scheduleDbo: ScheduleDbo): Long

    @Transaction
    fun insertOrUpdateCompareTable(scheduleDbo: ScheduleDbo) {
        deleteCompareTableTransactionPart(scheduleDbo.name)
        insertCompareTableTransactionPart(scheduleDbo)
    }


}
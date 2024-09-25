package com.schedule.database.entities

import androidx.room.Entity
import com.schedule.database.entities.ScheduleDbo.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME, primaryKeys = ["name", "week", "isCompareTable"])
data class ScheduleDbo(
    val type: String,
    val name: String,
    val week: Int,
    val group: String,
    val table: List<List<String>>,
    val link: String,
    val weeks: List<Int>,
    val cacheTime: String,
    val cacheDate: String,
    val isCompareTable: Boolean = false
) {


    companion object {
       const val TABLE_NAME = "schedule_dbo"

        fun createEmpty(name: String = "") = ScheduleDbo(
            "",
            name,
            1,
            "",
            emptyList(),
            "",
            emptyList(),
            "",
            "",
            true
        )
    }
}

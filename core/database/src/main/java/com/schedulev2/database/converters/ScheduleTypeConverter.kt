package com.schedulev2.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class ScheduleTypeConverter {

    @TypeConverter
    fun fromStringToTable(value: String): List<List<String>> = Gson().fromJson<List<List<String>>>(
        value,
        object : TypeToken<List<List<String>>>() {}.type
    )

    @TypeConverter
    fun fromTableList(value: List<List<String>>): String = Gson().toJson(value)

    @TypeConverter
    fun fromStringToWeeks(value: String): List<Int> = Gson().fromJson<List<Int>>(
        value,
        object : TypeToken<List<Int>>() {}.type
    )

    @TypeConverter
    fun fromWeeksList(value: List<Int>): String = Gson().toJson(value)

}
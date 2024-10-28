package com.schedulev2.utils.sources

import com.schedulev2.utils.DayOfWeek
import java.text.SimpleDateFormat
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale
import java.util.TimeZone

object DateUtils {

    var locale = Locale("ru", "RU")
    var timeZone = TimeZone.getTimeZone("Europe/Moscow")

    fun getCurrentDate(): String {
        val simpleDateFormat = SimpleDateFormat("d MMMM", locale)
        simpleDateFormat.timeZone = timeZone
        return simpleDateFormat.format(Date()).lowercase(Locale.getDefault())
    }

    fun getCurrentTime(): String {
        val simpleDateFormat = SimpleDateFormat("HH:mm", locale)
        simpleDateFormat.timeZone = timeZone
        return simpleDateFormat.format(Date()).lowercase(Locale.getDefault())
    }

    fun getCurrentDateAndTime(): String {
        val simpleDateFormat = SimpleDateFormat("d MMMM HH:mm", locale)
        simpleDateFormat.timeZone = timeZone
        return simpleDateFormat.format(Date()).lowercase(Locale.getDefault())
    }

    fun getCurrentDayOfWeek(): Int {
        val simpleDateFormat = SimpleDateFormat("u", locale)
        simpleDateFormat.calendar = GregorianCalendar()
        return simpleDateFormat.format(Date()).toInt()
    }

    fun getDayOfWeekName(string: String): String {
        val nameAbbreviation = string.subSequence(0, 3)
        return when (nameAbbreviation.toString().lowercase(Locale.getDefault())) {
            "пнд" -> DayOfWeek.MONDAY.titleRus
            "втр" -> DayOfWeek.TUESDAY.titleRus
            "срд" -> DayOfWeek.WEDNESDAY.titleRus
            "чтв" -> DayOfWeek.THURSDAY.titleRus
            "птн" -> DayOfWeek.FRIDAY.titleRus
            "сбт" -> DayOfWeek.SATURDAY.titleRus
            else -> throw IllegalStateException(
                "Unknown day of week name: " + nameAbbreviation.toString().lowercase(
                    Locale.getDefault()
                )
            )
        }
    }

    fun getCurrentDayOfWeekInYear(): Int {
        val simpleDateFormat = SimpleDateFormat("w", locale)
        simpleDateFormat.calendar = GregorianCalendar()
        return simpleDateFormat.format(Date()).toInt()
    }
}
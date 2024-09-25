package com.schedule.utils.sources

import com.schedule.utils.DayOfWeek
import java.text.SimpleDateFormat
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale
import java.util.TimeZone

object CurrentDayOfWeekUtils {

    fun getCurrentDayOfWeek(): Int {
        val locale = Locale("ru", "RU")
        val simpleDateFormat = SimpleDateFormat("EEEE", locale)
        simpleDateFormat.timeZone = TimeZone.getTimeZone("Europe/Moscow")
        simpleDateFormat.calendar = GregorianCalendar()
        return when (simpleDateFormat.format(Date()).uppercase()) {
            DayOfWeek.MONDAY.titleRus.uppercase() -> 1
            DayOfWeek.TUESDAY.titleRus.uppercase() -> 2
            DayOfWeek.WEDNESDAY.titleRus.uppercase() -> 3
            DayOfWeek.THURSDAY.titleRus.uppercase() -> 4
            DayOfWeek.FRIDAY.titleRus.uppercase() -> 5
            DayOfWeek.SATURDAY.titleRus.uppercase() -> 6
            DayOfWeek.SUNDAY.titleRus.uppercase() -> 7
            else -> throw IllegalStateException("This day of week is not handled")
        }
    }
}
package com.schedulev2.utils


enum class DayOfWeek(val number: Int, val titleRus: String, val titleRusShort: String) {
    MONDAY(1, "Понедельник", "ПН"),
    TUESDAY(2, "Вторник", "ВТ"),
    WEDNESDAY(3, "Среда", "СР"),
    THURSDAY(4, "Четверг", "ЧТ"),
    FRIDAY(5, "Пятница", "ПТ"),
    SATURDAY(6, "Суббота", "СБ"),
    SUNDAY(7, "Воскресенье", "ВС");
}

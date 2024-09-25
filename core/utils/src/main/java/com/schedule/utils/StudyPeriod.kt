package com.schedule.utils


enum class StudyPeriod(
    val fullTime: String,
    val startTime: String,
    val endTime: String,
    val periodCode: Int,
    val totalMinutes: Int
) {
    FIRST_LESSON("08:00-09:35", "08:00:00", "09:34:59", 1, 95),
    SECOND_LESSON("09:50-11:25", "09:50:00", "11:24:59", 2, 95),
    THIRD_LESSON("11:55-13:30", "11:55:00", "13:29:59", 3, 95),
    FOURTH_LESSON("13:45-15:20", "13:45:00", "15:19:59", 4, 95),
    FIFTH_LESSON("15:50-17:25", "15:50:00", "17:24:59", 5, 95),
    SIXTH_LESSON("17:35-19:10", "17:35:00", "19:09:59", 6, 95),
    SEVENTH_LESSON("19:15-20:50", "19:15:00", "20:49:59", 7, 95),

    FIRST_LESSON_BREAK("09:35-09:50", "09:35:00", "09:49:59", 11, 15),
    SECOND_LESSON_BREAK("11:25-11:55", "11:25:00", "11:54:59", 12, 30),
    THIRD_LESSON_BREAK("13:30-13:45", "13:30:00", "13:44:59", 13, 15),
    FOURTH_LESSON_BREAK("15:20-15:50", "15:20:00", "15:49:59", 14, 30),
    FIFTH_LESSON_BREAK("17:25-17:35", "17:25:00", "17:34:59", 15, 10),
    SIXTH_LESSON_BREAK("19:10-19:15", "19:10:00", "19:14:59", 16, 5),

    BEFORE_LESSONS("00:00-08:00", "00:00:00", "07:59:59", 21, 480),
    AFTER_LESSONS("20:50-00:00", "20:50:00", "23:59:59", 22, 190);

}

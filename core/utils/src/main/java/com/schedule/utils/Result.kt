package com.schedule.utils

sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val exception: Throwable) : Result<Nothing>
    data object Loading : Result<Nothing>
}

sealed interface ScheduleResult<out T> {
    data class SuccessFromDB<T>(val data: T) : ScheduleResult<T>
    data class SuccessFromNetwork<T>(val data: T) : ScheduleResult<T>
    data class Error(val exception: Throwable) : ScheduleResult<Nothing>
    data object Loading : ScheduleResult<Nothing>
}
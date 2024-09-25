package com.schedule.utils

sealed class CustomExceptions(message: String, cause: Throwable? = null): Exception(message, cause) {
    class NetworkException(message: String) : CustomExceptions(message)
}
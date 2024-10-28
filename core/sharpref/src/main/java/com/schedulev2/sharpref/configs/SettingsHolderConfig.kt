package com.schedulev2.sharpref.configs

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.squareup.moshi.JsonAdapter

abstract class SettingsHolderConfig<T, R>(context: Context) {
    val sharedPreferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context.applicationContext);

    abstract fun getSPValueName(): String
    abstract fun getDefaultValue(): R
    abstract fun getAdapter(): JsonAdapter<R>
    abstract fun mapToValue(sspContainer: R): T
    abstract fun mapFromValue(value: T): R

    fun getValue(): T {
        val token: String = sharedPreferences.getString(getSPValueName(), getAdapter().toJson(getDefaultValue()))!!
        val sspContainer = getAdapter().fromJson(token)!!
        return mapToValue(sspContainer)
    }

    fun setValue(value: T) {
        val token = getAdapter().toJson(mapFromValue(value))
        val edit = sharedPreferences.edit()
        edit.putString(getSPValueName(), token).apply()
    };
}
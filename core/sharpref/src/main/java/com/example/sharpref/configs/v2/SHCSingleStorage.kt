package com.example.sharpref.configs.v2

import android.content.Context
import com.example.models.sharpref.v2.SingleStorage
import com.example.sharpref.configs.SettingsHolderConfig
import com.example.sharpref.models.containers.v1.SingleStorageSPContainer
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SHCSingleStorage @Inject constructor(@ApplicationContext context: Context) :
    SettingsHolderConfig<SingleStorage, SingleStorageSPContainer>(context) {
    override fun getSPValueName(): String = "single_storage_sp"

    override fun getDefaultValue(): SingleStorageSPContainer = SingleStorageSPContainer(
        SingleStorage(cachedList = emptyList())
    )

    override fun getAdapter(): JsonAdapter<SingleStorageSPContainer> =
        Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build().adapter(SingleStorageSPContainer::class.java)


    override fun mapFromValue(value: SingleStorage): SingleStorageSPContainer = SingleStorageSPContainer(value)

    override fun mapToValue(sspContainer: SingleStorageSPContainer): SingleStorage = sspContainer.singleStorage
}
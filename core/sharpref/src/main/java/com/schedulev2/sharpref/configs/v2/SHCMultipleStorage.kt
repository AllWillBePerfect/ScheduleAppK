package com.schedulev2.sharpref.configs.v2

import android.content.Context
import com.schedulev2.models.sharpref.v2.MultipleStorage
import com.schedulev2.sharpref.configs.SettingsHolderConfig
import com.schedulev2.sharpref.models.containers.v1.MultipleStorageSPContainer
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SHCMultipleStorage @Inject constructor(@ApplicationContext context: Context) :
    SettingsHolderConfig<MultipleStorage, MultipleStorageSPContainer>(context) {
    override fun getSPValueName(): String = "multiple_storage_sp"

    override fun getDefaultValue(): MultipleStorageSPContainer = MultipleStorageSPContainer(
        MultipleStorage(cachedList = emptyList())
    )

    override fun getAdapter(): JsonAdapter<MultipleStorageSPContainer> = Moshi.Builder().addLast(
        KotlinJsonAdapterFactory()
    ).build().adapter(MultipleStorageSPContainer::class.java)

    override fun mapFromValue(value: MultipleStorage): MultipleStorageSPContainer = MultipleStorageSPContainer(value)

    override fun mapToValue(sspContainer: MultipleStorageSPContainer): MultipleStorage = sspContainer.multipleStorage
}
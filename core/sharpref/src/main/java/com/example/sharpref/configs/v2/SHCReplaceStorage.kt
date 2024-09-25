package com.example.sharpref.configs.v2

import android.content.Context
import com.example.models.sharpref.v2.ReplaceStorage
import com.example.sharpref.configs.SettingsHolderConfig
import com.example.sharpref.models.containers.v1.ReplaceStorageSPContainer
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SHCReplaceStorage @Inject constructor(@ApplicationContext context: Context) :
    SettingsHolderConfig<ReplaceStorage, ReplaceStorageSPContainer>(context) {
    override fun getSPValueName(): String = "replace_storage_sp"

    override fun getDefaultValue(): ReplaceStorageSPContainer = ReplaceStorageSPContainer(
        ReplaceStorage(
            isGlobal = true,
            cachedGlobalReplacedDays = listOf(1, 5),
            cachedList = emptyList()
        )
    )

    override fun getAdapter(): JsonAdapter<ReplaceStorageSPContainer> = Moshi.Builder().addLast(
        KotlinJsonAdapterFactory()
    ).build().adapter(ReplaceStorageSPContainer::class.java)


    override fun mapFromValue(value: ReplaceStorage): ReplaceStorageSPContainer =
        ReplaceStorageSPContainer(value)

    override fun mapToValue(sspContainer: ReplaceStorageSPContainer): ReplaceStorage =
        sspContainer.replaceStorage
}
package com.schedulev2.sharpref.configs

import android.content.Context
import android.os.Build
import com.schedulev2.models.sharpref.DynamicColors
import com.schedulev2.sharpref.models.containers.DynamicColorsSPContainer
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SHCDynamicColors @Inject constructor(@ApplicationContext context: Context) :
    SettingsHolderConfig<DynamicColors, DynamicColorsSPContainer>(context) {
    override fun getSPValueName(): String = "dynamic_colors_sp"

    override fun getDefaultValue(): DynamicColorsSPContainer = DynamicColorsSPContainer(
        dynamicColors = DynamicColors(
            isEnabled = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S,
        )
    )

    override fun getAdapter(): JsonAdapter<DynamicColorsSPContainer> = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory()).build().adapter(DynamicColorsSPContainer::class.java)

    override fun mapFromValue(value: DynamicColors): DynamicColorsSPContainer = DynamicColorsSPContainer(value)

    override fun mapToValue(sspContainer: DynamicColorsSPContainer): DynamicColors = sspContainer.dynamicColors
}
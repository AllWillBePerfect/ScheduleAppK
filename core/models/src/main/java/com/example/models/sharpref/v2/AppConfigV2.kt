package com.example.models.sharpref.v2

import com.squareup.moshi.JsonClass

data class AppConfigV2(
    val currentState: AppStateV2,
//    val cachedSingle: AppStateV2.Single?,
//    val cachedReplace: AppStateV2.Replace?,
//    val cachedMultiple: AppStateV2.Multiple?
)
@JsonClass(generateAdapter = true, generator = "sealed:type")
sealed class AppStateV2() {
    @JsonClass(generateAdapter = true)
    data class Single(val groupName: String) : AppStateV2()
    @JsonClass(generateAdapter = true)
    data class Replace(val groupName: String, val vpkName: String, val replaceDays: List<Int>) :
        AppStateV2()
    @JsonClass(generateAdapter = true)
    data class Multiple(val groupNames: List<String>) : AppStateV2()
    @JsonClass(generateAdapter = true)
    data class Unselected(val qwerty: String) : AppStateV2()
}

data class SingleStorage(
    val cachedList: List<String>
)

data class ReplaceStorage(
    val isGlobal: Boolean,
    val cachedGlobalReplacedDays: List<Int>,
    val cachedList: List<ReplaceItem>
) {
    data class ReplaceItem(
        val groupName: String,
        val vpkName: String,
        val replaceDays: List<Int>
    )
}

data class MultipleStorage(
    val cachedList: List<String>
)
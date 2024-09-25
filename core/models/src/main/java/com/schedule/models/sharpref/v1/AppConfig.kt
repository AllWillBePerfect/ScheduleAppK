package com.schedule.models.sharpref.v1

data class AppConfig(
    val appState: AppState,
    val singleGroupConfig: SingleGroupConfig,
    val multipleGroupConfig: MultipleGroupConfig
)

enum class AppState {
    SINGLE, MULTIPLE, UNSELECT
}

data class SingleGroup(
    val groupName: String
) {

}

data class MultipleGroup(
    val groupNameFirstPosition: String,
    val groupNameSecondPosition: String,
    val daysToReplace: List<Int>
)

data class SingleGroupConfig(
    val currentSingleGroup: SingleGroup,
    val list: List<SingleGroup>

)

data class MultipleGroupConfig(
    val currentMultipleGroup: MultipleGroup,
    val list: List<MultipleGroup>,
    val daysToReplaceShared: List<Int>

)
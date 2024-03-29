package com.example.models.sharpref

data class AppConfig(
    val appState: AppState,
    val singleGroup: SingleGroup,
    val multipleGroup: MultipleGroup
) {

}

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
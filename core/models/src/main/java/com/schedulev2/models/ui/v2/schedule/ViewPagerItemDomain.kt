package com.schedulev2.models.ui.v2.schedule

sealed class ViewPagerItemDomain {
    data class RecyclerViewDay(val lessons: List<TimetableItemDomain>) : ViewPagerItemDomain()
    data class RecyclerViewCurrentDay(val lessons: List<TimetableItemDomain>) : ViewPagerItemDomain()
}
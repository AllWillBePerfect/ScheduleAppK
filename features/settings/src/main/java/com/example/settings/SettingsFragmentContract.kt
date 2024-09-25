package com.example.settings

interface SettingsFragmentContract {
    fun navigateToAddSingleGroupScreen()
    fun navigateToAddReplaceGroupScreen()
    fun navigateToAddMultipleGroupScreen()
    fun navigateToMultipleGroupOptionFragment()
    fun popBackStack()
    fun intentReloadApp()
    fun recreateApp()
}
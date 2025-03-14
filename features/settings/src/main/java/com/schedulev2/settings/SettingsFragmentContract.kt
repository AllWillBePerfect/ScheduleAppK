package com.schedulev2.settings

interface SettingsFragmentContract {
    fun navigateToAddSingleGroupScreen()
    fun navigateToAddReplaceGroupScreen()
    fun navigateToAddMultipleGroupScreen()
    fun navigateToMultipleGroupOptionFragment()
    fun launchChangeModal()
    fun popBackStack()
    fun intentReloadApp()
    fun recreateApp()
}
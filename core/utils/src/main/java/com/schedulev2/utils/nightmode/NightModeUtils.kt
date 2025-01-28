package com.schedulev2.utils.nightmode

import android.app.UiModeManager
import android.content.Context
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

object NightModeUtils {

    fun AppCompatActivity.applyNightBeforeOnCrete(nightMode: Int) {
        if (checkApiToApplyNightMode())
            setNightModeApi31AndAbove(nightMode)
        else return
    }

    fun AppCompatActivity.applyNightAfterOnCrete(nightMode: Int) {
        if (!checkApiToApplyNightMode())
            setNightModeApi30andBelow(nightMode)
        else return
    }

    fun AppCompatActivity.setupNightMode(nightModeInt: Int) {
        if (checkApiToApplyNightMode())
            setNightModeApi31AndAbove(nightModeInt)
        else
            setNightModeApi30andBelow(nightModeInt)
    }

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
    fun checkApiToApplyNightMode(): Boolean =
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S


    @RequiresApi(Build.VERSION_CODES.S)
    private fun AppCompatActivity.setNightModeApi31AndAbove(nightMode: Int) {
        val uiModeManager =
            getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        when (nightMode) {
            0 -> uiModeManager.setApplicationNightMode(
                UiModeManager.MODE_NIGHT_NO
            )

            1 -> uiModeManager.setApplicationNightMode(
                UiModeManager.MODE_NIGHT_YES
            )

            2 -> uiModeManager.setApplicationNightMode(
                UiModeManager.MODE_NIGHT_AUTO
            )
        }

    }

    private fun AppCompatActivity.setNightModeApi30andBelow(nightMode: Int) {
        when (nightMode) {
            0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            2 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }


//    fun Fragment.setNightMode(nightMode: Int) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            val uiModeManager =
//                requireActivity().getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
//            when (nightMode) {
//                0 -> uiModeManager.setApplicationNightMode(
//                    UiModeManager.MODE_NIGHT_NO
//                )
//
//                1 -> uiModeManager.setApplicationNightMode(
//                    UiModeManager.MODE_NIGHT_YES
//                )
//
//                2 -> uiModeManager.setApplicationNightMode(
//                    UiModeManager.MODE_NIGHT_AUTO
//                )
//            }
//        } else {
//            when (nightMode) {
//                0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//                1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//                2 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
//            }
//        }
//    }
}
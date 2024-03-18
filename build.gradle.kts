// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.androidLibrary) apply false
    id("com.google.dagger.hilt.android") version "2.44" apply false
}

val minSdkVer: Int by rootProject.extra{24}
val targetSdkVer: Int by rootProject.extra{34}
val compileSdkVer: Int by rootProject.extra{34}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.hiltAndroid) apply false
    alias(libs.plugins.jetbrainsKotlinJvm) apply false
    id("com.google.devtools.ksp") version "1.8.10-1.0.9" apply false
}

val minSdkVer: Int by rootProject.extra{24}
val targetSdkVer: Int by rootProject.extra{34}
val compileSdkVer: Int by rootProject.extra{34}

val packageGroupName: String by rootProject.extra{"com.schedulev2"}



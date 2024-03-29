plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    kotlin("kapt")
    alias(libs.plugins.hiltAndroid)}

val compileSdkVer: Int by rootProject.extra
val minSdkVer: Int by rootProject.extra

android {
    namespace = "com.example.enter"
    compileSdk = compileSdkVer

    defaultConfig {
        minSdk = minSdkVer

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
    kapt {
        correctErrorTypes = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    implementation(libs.androidx.fragment.ktx)

    implementation(libs.keyboardvisibilityevent)

    implementation(libs.rxjava2)
    implementation(libs.rxkotlin2)
    implementation(libs.rxandroid)

    implementation (libs.rxbinding)
//    implementation (libs.rxbinding.kotlin)

    implementation(libs.retrofit)
    implementation(libs.adapter.rxjava2)
    implementation(libs.converter.gson)
    implementation(libs.converter.scalars)
    implementation(libs.converter.moshi)
    implementation(libs.logging.interceptor)


    api(project(":core:values"))
//    api(project(":core:views"))
    implementation(project(":core:data"))
    implementation(project(":core:domain"))





}
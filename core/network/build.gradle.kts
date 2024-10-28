plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    kotlin("kapt")
    alias(libs.plugins.hiltAndroid)
}

val compileSdkVer: Int by rootProject.extra
val minSdkVer: Int by rootProject.extra
val packageGroupName: String by rootProject.extra


android {
    namespace = "$packageGroupName.network"
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

    implementation(libs.rxjava2)
    implementation(libs.rxkotlin2)
    implementation(libs.rxandroid)

    implementation(libs.retrofit)
    implementation(libs.adapter.rxjava2)
    implementation(libs.converter.gson)
    implementation(libs.converter.scalars)
    implementation(libs.converter.moshi)
    implementation(libs.logging.interceptor)

    testImplementation(libs.mockito.core)
    testImplementation(libs.mockwebserver)
    testImplementation(libs.rxjava2)
    testImplementation(libs.rxkotlin2)
    testImplementation(libs.rxandroid)

    api(project(":core:models"))


}
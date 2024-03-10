plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

var versionMajor by extra(0)
var versionMinor by extra(1)
var versionPatch by extra(0)
var versionClassifier: String? by extra(null)
var isSnapshot by extra(true)

val minSdkVer: Int by rootProject.extra
val targetSdkVer: Int by rootProject.extra
val compileSdkVer: Int by rootProject.extra

android {
    namespace = "com.example.scheduleappk"
    compileSdk = compileSdkVer

    defaultConfig {
        applicationId = "com.example.scheduleappk"
        minSdk = minSdkVer
        targetSdk = targetSdkVer
        versionCode = generateVersionCode()
        versionName = generateVersionName()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.keyboardvisibilityevent)


    implementation(project(":core:network"))
    implementation(project(":core:database"))
    implementation(project(":core:values"))
    implementation(project(":core:views"))

    implementation(project(":features:enter"))
}

fun generateVersionCode(): Int {
    return versionMajor * 10000 + versionMinor * 100 + versionPatch;
}

fun generateVersionName(): String {
    var versionName: String =
        "${versionMajor}.${versionMinor}.${versionPatch}"
    if (versionClassifier == null && isSnapshot)
        versionClassifier = "SNAPSHOT"

    if (versionClassifier != null) {
        versionName += "-$versionClassifier"
    }

    return versionName

}
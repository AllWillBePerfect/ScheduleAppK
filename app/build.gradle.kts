import java.util.Properties
import java.util.regex.Pattern

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    kotlin("kapt")
//    id("com.google.devtools.ksp")
    alias(libs.plugins.hiltAndroid)
}

var versionMajor by extra(1)
var versionMinor by extra(0)
var versionPatch by extra(2)
var versionClassifier: String? by extra(null)
var isSnapshot by extra(false)

//change this flag to "true" if you need clear storage data after realise new app version
var clearStorageFlag by extra(false)

val minSdkVer: Int by rootProject.extra
val targetSdkVer: Int by rootProject.extra
val compileSdkVer: Int by rootProject.extra
val packageGroupName: String by rootProject.extra
val moduleAppArtifactName = "schedule"
android {
    signingConfigs {
        create("release") {
            val keystoreProp = Properties()
            val keystorePropFile = file("keystore/keystoreConfig.properties")

            if (keystorePropFile.exists()) {
                /** local check */
                keystorePropFile.inputStream().use { keystoreProp.load(it) }
                storeFile = file(keystoreProp["storeFile"] as String)
                storePassword = keystoreProp["storePassword"] as String
                keyAlias = keystoreProp["keyAlias"] as String
                keyPassword = keystoreProp["keyPassword"] as String
            } else {
                /** CI github check */
                storeFile = file("keystore/keystore.jks")
                storePassword = System.getenv("KEYSTORE_STORE_PASSWORD")
                keyAlias = System.getenv("KEYSTORE_KEY_ALIAS")
                keyPassword = System.getenv("KEYSTORE_KEY_PASSWORD")
            }
        }
    }
    namespace = "com.ita.$moduleAppArtifactName"
    compileSdk = compileSdkVer

    defaultConfig {
        applicationId = "com.ita.$moduleAppArtifactName"
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
            signingConfig = signingConfigs.getByName("release")
            if (getCurrentFlavor().lowercase() == "full")
                resValue("string", "app_name", "ИКТИБ Расписание")
            if (getCurrentFlavor().lowercase() == "demo")
                resValue("string", "app_name", "FScheduleAppK")


        }
        debug {
            versionNameSuffix = "-DEBUG"
            if (getCurrentFlavor().lowercase() == "full")
                resValue("string", "app_name", "DScheduleAppK")
            if (getCurrentFlavor().lowercase() == "demo")
                resValue("string", "app_name", "DScheduleAppK")
        }
        buildTypes.forEach {
            it.resValue("string", "clear_storage_flag", clearStorageFlag.toString())
        }
    }
    flavorDimensions.add("app_type")
    productFlavors {
        create("demo") {
            dimension = "app_type"
            applicationIdSuffix = ".demo"
        }

        create("full") {
            dimension = "app_type"
            /*applicationIdSuffix = ".full"*/
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
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    implementation(libs.keyboardvisibilityevent)

    implementation(libs.androidx.fragment.ktx)

    implementation(libs.androidx.preference.ktx)


    implementation(libs.rxjava2)
    implementation(libs.rxkotlin2)
    implementation(libs.rxandroid)

    implementation(libs.rxbinding)
//    implementation (libs.rxbinding.kotlin)

    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.work.rxjava2)
    implementation(libs.androidx.hilt.work)
    annotationProcessor(libs.androidx.hilt.compiler)

    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    kapt(libs.androidx.room.room.compiler)
    implementation(libs.androidx.room.rxjava2)

    implementation(libs.gson)


    implementation(libs.retrofit)
    implementation(libs.adapter.rxjava2)
    implementation(libs.converter.gson)
    implementation(libs.converter.scalars)
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    implementation(libs.moshi.adapters)
    implementation(libs.converter.moshi)
    implementation(libs.logging.interceptor)

    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.shimmer)

    testImplementation(libs.mockito.core)
    testImplementation(libs.mockwebserver)
    testImplementation(libs.rxjava2)
    testImplementation(libs.rxkotlin2)
    testImplementation(libs.rxandroid)



    implementation(project(":core:network"))
    implementation(project(":core:database"))
    implementation(project(":core:values"))
    implementation(project(":core:views"))
    implementation(project(":core:data"))
    implementation(project(":core:sharpref"))
    implementation(project(":core:models"))
    implementation(project(":core:utils"))
    implementation(project(":core:domain"))

    implementation(project(":features:enter"))
    implementation(project(":features:schedule"))
    implementation(project(":features:settings"))
    implementation(project(":features:clear"))

    implementation(project(":rxtest"))
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

fun getCurrentFlavor(): String {
    val gradle = gradle
    val tskReqStr = gradle.startParameter.taskRequests.toString()

    val pattern = when {
        tskReqStr.contains("assemble") -> Pattern.compile("assemble([A-Za-z0-9_\\-]+)?(Release|Debug)")
        tskReqStr.contains("bundle") -> Pattern.compile("bundle([A-Za-z0-9_\\-]+)?(Release|Debug)")
        else -> Pattern.compile("generate([A-Za-z0-9_\\-]+)?(Release|Debug)")
    }

    val matcher = pattern.matcher(tskReqStr)

    return if (matcher.find()) {
        // Если flavor указан, возвращаем его в нижнем регистре
        matcher.group(1)?.lowercase() ?: "full" // Возвращаем "full" по умолчанию, если flavor не найден
    } else {
        println("NO FLAVOR FOUND, using default flavor 'full'.")
        "full" // Возвращаем "full" по умолчанию
    }
}

tasks.register("fullBuild") {
    dependsOn("assembleRelease", "bundleRelease")
}
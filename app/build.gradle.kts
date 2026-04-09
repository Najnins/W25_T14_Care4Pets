// app/build.gradle.kts

plugins {
    // Apply the plugins needed for this specific module
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp) // Correctly apply the KSP plugin here
}

android {
    namespace = "com.arad.care4pets"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.arad.care4pets"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
}

dependencies {
    // Core Android libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Calendar view library
    implementation(libs.prolificinteractive.material.calendarview)

    // Room Database libraries
    implementation(libs.androidx.room.runtime)
    // FIX: Use 'ksp' for the Room compiler, not 'annotationProcessor' or 'kapt'
    ksp(libs.androidx.room.compiler)
    implementation("androidx.core:core:1.12.0")

    // Testing libraries
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

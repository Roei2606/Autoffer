plugins {
    alias(libs.plugins.android.library)
    kotlin("android")
}

android {
    namespace = "com.example.core_models_sdk"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

}



dependencies {
    // תלויות ל-Jackson עבור serialization
    implementation(libs.jackson.annotations)
    implementation(libs.jackson.core)
    implementation(libs.jackson.databind)

    // אל תכניס appcompat/material – אין צורך בהם במודול לוגי!
    // השאר רק את התלויות החיוניות ל־models

    // Testing if needed
    testImplementation(libs.junit)
}

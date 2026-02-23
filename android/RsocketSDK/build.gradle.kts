//plugins {
//    alias(libs.plugins.android.library)
//    kotlin("android")
//}
//
//android {
//    namespace = "com.example.rsocketsdk"
//    compileSdk = libs.versions.compileSdk.get().toInt()
//
//    defaultConfig {
//        minSdk = libs.versions.minSdk.get().toInt()
//        targetSdk = libs.versions.targetSdk.get().toInt()
//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        consumerProguardFiles("consumer-rules.pro")
//    }
//
//    buildTypes {
//        release {
//            isMinifyEnabled = false
//        }
//
//    }
//
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_17
//        targetCompatibility = JavaVersion.VERSION_17
//    }
//
//    kotlinOptions {
//        jvmTarget = "17"
//    }
//
//}
//
//dependencies {
//    implementation(libs.rsocket.core)
//    implementation(libs.rsocket.transport.netty)
//    implementation(libs.reactor.core)
//    implementation(libs.netty.all)
//
//    implementation(libs.jackson.annotations)
//    implementation(libs.jackson.core)
//    implementation(libs.jackson.databind)
//    implementation(libs.jackson.datatype.jsr310)
//    implementation(libs.jackson.module.kotlin)
//
//    implementation(libs.slf4j.simple)
//
//    implementation(libs.kotlinx.coroutines.core)
//    implementation(libs.kotlinx.coroutines.android)
//
//    testImplementation(libs.junit)
//}
plugins {
    alias(libs.plugins.android.library)
    kotlin("android")
}

android {
    namespace = "com.example.rsocketsdk"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            buildConfigField("boolean", "USE_PUBLIC_SERVER", "false")
        }
        getByName("release") {
            isMinifyEnabled = false
            buildConfigField("boolean", "USE_PUBLIC_SERVER", "true")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation(project(":coremodelsSDK"))

    implementation(libs.rsocket.core)
    implementation(libs.rsocket.transport.netty)
    implementation(libs.reactor.core)
    implementation(libs.netty.all)

    implementation(libs.jackson.annotations)
    implementation(libs.jackson.core)
    implementation(libs.jackson.databind)
    implementation(libs.jackson.datatype.jsr310)
    implementation(libs.jackson.module.kotlin)

    implementation(libs.slf4j.simple)
    implementation (libs.jackson.datatype.jsr310.v2152)


    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    testImplementation(libs.junit)
}

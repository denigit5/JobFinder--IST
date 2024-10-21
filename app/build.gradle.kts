plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.job_finder_ist"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.job_finder_ist"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // Enable Jetpack Compose
    buildFeatures {
        compose = true
    }

    // Set Compose Compiler version
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
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
    // Core AndroidX and Jetpack Compose dependencies
    implementation(libs.androidx.ui.v153)
    implementation(libs.androidx.ui.tooling.preview.v153)
    implementation(libs.androidx.material.v153)
    implementation(libs.androidx.activity.compose.v172)

    // AndroidX Libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.viewpager2)
    implementation (libs.androidx.material.icons.extended)
    implementation (libs.compose)
    implementation(libs.androidx.media3.common)
    implementation(libs.androidx.material.vversion)

    // Firebase SDKs
    implementation(libs.firebase.dynamic.module.support)
    implementation(libs.firebase.auth)
    implementation (libs.google.firebase.firestore.ktx.v2400)
    implementation(libs.firebase.firestore.ktx)
//    implementation(libs.firebase.messaging)
//    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.6")
//    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.5.0")
//    implementation ("androidx.activity:activity-ktx:1.7.0")

    // Glide for image loading
    implementation(libs.coil.compose)
    implementation(libs.glide)
    implementation(libs.landscapist.glide)
    implementation(libs.firebase.inappmessaging)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.messaging.ktx)

    // Jetpack Compose Testing
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Debugging tools for Compose
    debugImplementation(libs.androidx.ui.tooling.v153)
    debugImplementation(libs.androidx.ui.test.manifest.v153)

    // Optional: Compose navigation
    implementation(libs.androidx.navigation.compose)

    // Annotation Processor and Kotlin Script
    annotationProcessor(libs.compiler)
    implementation(kotlin("script-runtime"))

    // Test dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

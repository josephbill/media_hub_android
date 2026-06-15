plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.mediahub"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.mediahub"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    // navigation
    implementation(
        "androidx.navigation:navigation-compose:2.7.7")
    // coil : image routing
    implementation("io.coil-kt:coil-compose:2.6.0")
    //viewmodel : (data management and compose rendering)
    implementation(
        "androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    // get icons from material design
    implementation(
        "androidx.compose.material:material-icons-extended:1.7.8"
    )
    //firebase dependencies
    // firebase BOM - management dependency for firebase products
    implementation(platform(
        "com.google.firebase:firebase-bom:33.1.0"))
    // firebase authentication product (auth)
    implementation("com.google.firebase:firebase-auth-ktx")
    // database
    implementation("com.google.firebase:firebase-firestore-ktx")
    // storage
    implementation("com.google.firebase:firebase-storage-ktx")
    // coroutines support for firebase i.e. a way of handling
    //background process
    implementation(
        "org.jetbrains.kotlinx:kotlinx-coroutines-" +
                "play-services:1.8.0")
    // cloudinary
    implementation("com.cloudinary:cloudinary-android:2.3.1")
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
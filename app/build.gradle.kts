plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("kotlinx-serialization")
    id("com.google.firebase.crashlytics")
    id("kotlin-kapt")
}

android {
    namespace = "com.bigbigdw.manavara"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.bigbigdw.manavara"
        minSdk = 26
        targetSdk = 34
        versionCode = 100000
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
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
    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("com.google.firebase:firebase-auth-ktx:22.3.0")
    implementation("com.google.firebase:firebase-messaging-ktx:23.4.0")
    implementation("com.google.firebase:firebase-database-ktx:20.3.0")
    implementation("com.google.firebase:firebase-storage-ktx:20.3.0")
    implementation("com.google.firebase:firebase-crashlytics:18.6.0")
    implementation("com.google.firebase:firebase-analytics:21.5.0")
    implementation("com.google.firebase:firebase-auth:22.3.0")
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))
    implementation("androidx.compose.material3:material3")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")

    implementation("androidx.work:work-runtime-ktx:2.9.0")
    implementation("com.google.guava:guava:28.0-android")

    implementation("androidx.window:window-extensions:1.0.0-alpha01")
    implementation("androidx.compose.material3:material3-window-size-class")

    implementation("androidx.navigation:navigation-compose:2.7.6")
    implementation("androidx.compose.material:material")
    implementation("com.google.android.material:material:1.12.0-alpha02")

    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("androidx.datastore:datastore:1.0.0")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    implementation("io.coil-kt:coil-compose:2.4.0")

    implementation("org.jsoup:jsoup:1.16.1")
}
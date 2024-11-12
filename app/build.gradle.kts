plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.agencedevoyage"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.agencedevoyage"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    packaging {
        resources {
            excludes += "META-INF/NOTICE.md"
            excludes += "META-INF/LICENSE.md"
        }
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
}


dependencies {
    dependencies {
        implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")
    }
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.coordinatorlayout)
    implementation (libs.material.v190)
    implementation ("androidx.room:room-runtime:2.2.0")
    annotationProcessor ("androidx.room:room-compiler:2.2.0")
    implementation ("com.github.bumptech.glide:glide:4.15.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0") // For Java projects
    implementation ("com.sun.mail:android-mail:1.6.7")
    implementation ("com.sun.mail:android-activation:1.2.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation ("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.9")

    // Room dependencies
    implementation("androidx.room:room-runtime:2.5.0")
    annotationProcessor("androidx.room:room-compiler:2.5.0")
    implementation ("com.google.android.material:material:1.4.0")
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")

//    implementation ("com.google.android.material:material:1.4.0")
//    implementation ("androidx.webkit:webkit:1.4.0")



}
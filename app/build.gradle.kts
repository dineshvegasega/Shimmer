plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.daggerHilt)
    alias(libs.plugins.devtoolsKsp)
    alias (libs.plugins.kotlinParcelize)

    kotlin("kapt")
    id("kotlin-kapt")
}

android {
    namespace = "com.shimmer.store"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.shimmer.store"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        dataBinding = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }


    kapt {
        correctErrorTypes = true
    }

    bundle {
        language {
            enableSplit = false
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation ("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation ("androidx.databinding:databinding-ktx:8.4.2")
    implementation ("androidx.databinding:databinding-runtime:8.4.2")

    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.8.1")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")


    implementation("com.intuit.sdp:sdp-android:1.1.0")
    implementation("com.intuit.ssp:ssp-android:1.1.0")

    debugImplementation ("com.github.chuckerteam.chucker:library:4.0.0")
    releaseImplementation ("com.github.chuckerteam.chucker:library-no-op:4.0.0")

    implementation ("androidx.preference:preference-ktx:1.2.1")
    implementation ("io.coil-kt:coil:2.4.0")

    implementation ("com.github.bumptech.glide:glide:4.16.0")
    ksp ("com.github.bumptech.glide:ksp:4.16.0")
    implementation("com.github.bumptech.glide:okhttp3-integration:4.16.0") {
        exclude("glide-parent")
    }
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    implementation (platform(libs.firebase.bom))
    implementation (libs.firebase.auth)
    implementation (libs.firebase.auth.ktx)
    implementation (libs.firebase.database.ktx)
    implementation (libs.firebase.messaging.ktx)
    implementation (libs.firebase.analytics.ktx)
    implementation (libs.firebase.crashlytics.ktx)
    implementation (libs.google.firebase.config.ktx)
    implementation (libs.firebase.dynamic.links.ktx)

//    implementation (libs.stfalconimageviewer)
//    implementation ("com.github.stfalcon-studio:StfalconImageViewer:v1.0.1")
    implementation ("com.google.android.gms:play-services-auth-api-phone:18.0.2")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.hilt.common)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation)

    implementation(libs.splashscreen)
    implementation(libs.systemuicontroller)
    implementation (libs.navigation)
    implementation (libs.datastore)

    implementation (libs.pagingRuntime)
    implementation (libs.pagingCompose)
    implementation (libs.coilCompose)

    implementation (libs.roomRuntime)
    ksp (libs.roomCompiler)
    implementation (libs.roomKtx)

    implementation (libs.retrofit2)
    implementation (libs.retrofit2ConverterGson)

    implementation (libs.workRuntime)
    implementation (libs.runtimeLivedata)
    implementation (libs.accompanistPermissions)
    implementation (libs.hiltWork)
    implementation (libs.composeMaterial)
    implementation (libs.landscapisCoil)


    implementation (libs.timber)
    implementation (libs.stetho)
    implementation (libs.stethoOkhttp)
    implementation (libs.lottieCompose)

    implementation (libs.destinationsCore)
    ksp (libs.destinationsKsp)

    implementation(platform(libs.okhttpBom))
    implementation(libs.okhttp)
    implementation(libs.okhttpInterceptor)

    implementation (libs.materialIconsExtended)
    implementation (libs.constraintlayout)
}
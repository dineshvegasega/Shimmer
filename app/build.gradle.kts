@file:Suppress("UnstableApiUsage")
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
    id ("com.apollographql.apollo3") version ("3.7.0")
    id ("com.google.gms.google-services")
}

android {
    namespace = "com.shimmer.store"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.shimmer.store"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }


    buildTypes {
        release {
            isMinifyEnabled = false
//            isShrinkResources = true
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
//        compose = true
        dataBinding = true
        viewBinding = true
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
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    implementation ("androidx.appcompat:appcompat:1.7.0")

    implementation("com.google.dagger:hilt-android:2.51.1")
    ksp("com.google.dagger:hilt-android-compiler:2.51.1")


    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation ("androidx.navigation:navigation-ui-ktx:2.7.7")
    //noinspection GradleCompatible,GradleCompatible
    implementation ("androidx.databinding:databinding-ktx:8.5.1")
    implementation ("androidx.databinding:databinding-runtime:8.5.1")

    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    implementation("com.intuit.sdp:sdp-android:1.1.0")
    implementation("com.intuit.ssp:ssp-android:1.1.0")

    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation ("com.squareup.picasso:picasso:2.8")

    //retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.11.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation ("com.squareup.okhttp3:okhttp:5.0.0-alpha.11")
    implementation ("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.5")
    implementation ("com.squareup.retrofit2:converter-scalars:2.9.0")

    implementation ("com.google.code.gson:gson:2.10.1")

    implementation ("com.github.bumptech.glide:glide:4.16.0")
    ksp ("com.github.bumptech.glide:ksp:4.16.0")
    implementation("com.github.bumptech.glide:okhttp3-integration:4.16.0") {
        exclude("glide-parent")
    }


    implementation ("androidx.preference:preference-ktx:1.2.1")
    implementation ("io.coil-kt:coil:2.4.0")

    implementation ("androidx.datastore:datastore-preferences:1.1.1")
    implementation ("androidx.datastore:datastore-preferences-core:1.1.1")

    implementation ("id.zelory:compressor:3.0.1")
    implementation ("com.airbnb.android:lottie:6.4.0")

    implementation ("com.google.android.flexbox:flexbox:3.0.0")

    debugImplementation ("com.github.chuckerteam.chucker:library:4.0.0")
    releaseImplementation ("com.github.chuckerteam.chucker:library-no-op:4.0.0")

    implementation ("com.google.android.play:core-ktx:1.8.1")
    implementation (platform("com.google.firebase:firebase-bom:33.1.2"))
    implementation ("com.google.firebase:firebase-auth")
    implementation ("com.google.firebase:firebase-auth-ktx")
    implementation ("com.google.firebase:firebase-database-ktx")
    implementation ("com.google.firebase:firebase-messaging-ktx")
    implementation ("com.google.firebase:firebase-analytics-ktx")
//    implementation ("com.google.firebase:firebase-crashlytics-ktx")
    implementation ("com.google.firebase:firebase-config-ktx")
    implementation ("com.google.firebase:firebase-dynamic-links-ktx")

    implementation ("com.github.stfalcon-studio:StfalconImageViewer:v1.0.1")

    implementation ("com.google.android.gms:play-services-auth:21.2.0")
    implementation ("com.google.android.gms:play-services-location:21.3.0")
    implementation ("com.google.android.gms:play-services-maps:19.0.0")

    implementation ("androidx.paging:paging-common-ktx:3.3.1")
    implementation ("androidx.paging:paging-runtime-ktx:3.3.1")
//    implementation ("com.google.android.play:review-ktx:2.0.1")
    implementation ("org.jsoup:jsoup:1.17.2")
    implementation ("com.github.vipulasri:timelineview:1.1.5")

//    implementation ("com.daimajia.swipelayout:library:1.2.0")

//    implementation ("com.chauthai.swipereveallayout:swipe-reveal-layout:c04a9d7")
//    implementation ("com.github.hydrated:SwipeRevealLayout:cedd8e7")

//    implementation ("com.github.qiujayen:sticky-layoutmanager:1.0.1")
    implementation ("androidx.room:room-runtime:2.6.1")
    annotationProcessor ("androidx.room:room-compiler:2.6.1")
    ksp ("androidx.room:room-compiler:2.6.1")
    ksp ("androidx.room:room-ktx:2.6.1")
    implementation ("androidx.room:room-ktx:2.6.1")
//    kapt ("androidx.room:room-compiler:2.6.1")
    implementation ("androidx.work:work-runtime-ktx:2.9.0")
    implementation ("com.hbb20:ccp:2.7.0")
    implementation ("com.github.rajputmukesh748:MukeshOtpView:1.0.0")
    implementation ("com.apollographql.apollo3:apollo-runtime:3.7.0")

    implementation ("androidx.media3:media3-exoplayer:1.4.0")
    implementation ("androidx.media3:media3-ui:1.4.0")
    implementation ("androidx.media3:media3-exoplayer-hls:1.4.0")

//    implementation ("com.reginald.swiperefresh:library:1.0.0")
//    implementation ("com.google.android.exoplayer:exoplayer:2.19.1")

    implementation ("com.github.chrisbanes:PhotoView:2.2.0")

//    implementation ("com.github.barteksc:android-pdf-viewer:3.2.0-beta.1")

//    implementation ("io.github.afreakyelf:Pdf-Viewer:2.0.7")
}


//apollo {
//    generateModelBuilder = true
//}

apollo {
    packageName.set("com.shimmer.store")
}


//buildscript {
//    dependencies {
//        classpath ("com.google.firebase:firebase-crashlytics-gradle:3.0.2")
//    }
//}

plugins {
    id("org.jetbrains.kotlin.jvm") version "2.0.20" apply false
    id("com.android.application") version "8.3.2" apply false
    id("org.jetbrains.kotlin.android") version "2.0.20" apply false
    id("com.android.library") version "8.3.2" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
    id("com.google.devtools.ksp") version "2.0.20-1.0.25" apply false
    id("com.google.dagger.hilt.android") version "2.52" apply false
    id("com.google.firebase.crashlytics") version "3.0.2" apply false
}


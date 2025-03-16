buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
        val nav_version = "2.7.6"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
    }
}

allprojects {
    repositories {
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

//buildscript {
//    dependencies {
//        classpath("com.google.gms:google-services:4.4.0")
//        val nav_version = "2.7.6"
//        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
//
//    }
//



plugins {
    id("com.android.application") version "8.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.dagger.hilt.android") version "2.48" apply false
}
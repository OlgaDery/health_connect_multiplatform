buildscript {
    dependencies {
        classpath("com.android.tools.build:gradle:8.1.1")
        classpath("com.squareup.sqldelight:gradle-plugin:1.5.5")
        classpath("org.jetbrains.compose:compose-gradle-plugin:1.6.0-dev1265")
    }
}

plugins {
    //trick: for the same plugin versions in all sub-modules
    kotlin("multiplatform").apply(false)
    id("com.android.application").apply(false)
    id("com.android.library").apply(false)
    id("org.jetbrains.compose").apply(false)
    kotlin("native.cocoapods").apply(false)

    //check if those are required
    id("org.jetbrains.kotlin.kapt").apply(false)
    kotlin("android").apply(false)
    kotlin("jvm").apply(false)
    id("dev.icerock.mobile.multiplatform-resources") version "0.22.1"
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}


plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("kotlin-kapt")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization")
    id("app.cash.sqldelight") version "2.0.0-alpha05"
    id ("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("com.google.devtools.ksp") version "1.9.20-Beta-1.0.13"
    kotlin("native.cocoapods")
    id("co.touchlab.skie") version "0.5.2"
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    androidTarget() {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }
    version = "1.0.0"

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    )

    cocoapods{
        summary = "Kotlin sample project with CocoaPods dependencies"
        ios.deploymentTarget = "14.5"
    }

    dependencies {
        commonMainApi("dev.icerock.moko:mvvm-core:0.16.1") // only ViewModel, EventsDispatcher, Dispatchers.UI
        commonMainApi("dev.icerock.moko:mvvm-flow:0.16.1") // api mvvm-core, CFlow for native and binding extensions
        commonMainApi("dev.icerock.moko:mvvm-livedata:0.16.1") // api mvvm-core, LiveData and extensions
        commonMainApi("dev.icerock.moko:mvvm-state:0.16.1") // api mvvm-livedata, ResourceState class and extensions
        commonMainApi("dev.icerock.moko:mvvm-livedata-resources:0.16.1") // api mvvm-core, moko-resources, extensions for LiveData with moko-resources
        commonMainApi("dev.icerock.moko:mvvm-flow-resources:0.16.1") // api mvvm-core, moko-resources, extensions for Flow with moko-resources

        // compose multiplatform
        commonMainApi("dev.icerock.moko:mvvm-compose:0.16.1") // api mvvm-core, getViewModel for Compose Multiplatfrom
        commonMainApi("dev.icerock.moko:mvvm-flow-compose:0.16.1") // api mvvm-flow, binding extensions for Compose Multiplatfrom
        commonMainApi("dev.icerock.moko:mvvm-livedata-compose:0.16.1") // api mvvm-livedata, binding extensions for Compose Multiplatfrom
        commonTestImplementation("dev.icerock.moko:mvvm-test:0.16.1")

        commonMainImplementation("io.github.epicarchitect:calendar-compose-basis:1.0.5")
        commonMainImplementation("io.github.epicarchitect:calendar-compose-ranges:1.0.5") // includes basis
        commonMainImplementation("io.github.epicarchitect:calendar-compose-pager:1.0.5") // includes basis
        commonMainImplementation("io.github.epicarchitect:calendar-compose-datepicker:1.0.5")
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
                implementation("app.cash.sqldelight:coroutines-extensions:2.0.0-alpha05")
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
                implementation(compose.animation)
                implementation(compose.animationGraphics)
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")
                implementation("com.chrynan.navigation:navigation-compose:0.10.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("io.mockative:mockative:2.0.1")
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(
                    project.dependencies.platform(
                        "androidx.compose:compose-bom:2023.10.01"
                    )
                )
            }
        }
        dependencies {
            configurations
                .filter { it.name.startsWith("ksp") && it.name.contains("Test") }
                .forEach {
                    add(it.name, "io.mockative:mockative-processor:2.0.1")
                }
        }

        val androidMain by getting
        val androidUnitTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by getting {
            dependencies {
                implementation("app.cash.sqldelight:native-driver:2.0.0-alpha05")
                implementation("app.cash.sqldelight:coroutines-extensions:2.0.0-alpha05")
            }
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by getting
    }
}

android {
    namespace = "com.example.health_multiplstform"
    compileSdk = 34

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].res.srcDirs("src/commonMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        minSdk = 26
        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true"
                )
            }
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    dependencies {
        implementation("app.cash.sqldelight:android-driver:2.0.0-alpha05")
        api("androidx.appcompat:appcompat:1.6.1")
        api("androidx.core:core-ktx:1.10.1")
        implementation("com.google.maps.android:maps-compose:3.1.0")
        implementation ("com.google.android.gms:play-services-maps:18.1.0")
        implementation ("com.google.android.libraries.places:places:3.2.0")
        implementation("com.google.android.gms:play-services-awareness:19.0.1")
        implementation("com.google.android.gms:play-services-location:21.0.1")
        implementation("dev.shreyaspatil.permission-flow:permission-flow-android:1.2.0")
        implementation("androidx.health.connect:connect-client:1.0.0-alpha11")
        implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")
        testImplementation("org.jetbrains.compose.ui:ui-test-junit4:1.5.10")
    }
}

sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("com.example.health_multiplstform.shared.cache")
        }
    }
}


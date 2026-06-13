import java.util.Properties

plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
  id("com.google.gms.google-services")
  id("org.jetbrains.kotlin.plugin.compose")
  id("com.google.devtools.ksp")
}

kotlin {
  compilerOptions {
    jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
  }
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()

if (keystorePropertiesFile.exists()) {
  keystorePropertiesFile.inputStream().use {
    keystoreProperties.load(it)
  }
}

fun keystoreProperty(name: String): String {
  return keystoreProperties[name] as String?
    ?: error("Missing '$name' in keystore.properties")
}

android {
  namespace = "com.karandaev.geo_inspect"
  compileSdk = 36

  defaultConfig {
    applicationId = "com.karandaev.geo_inspect"
    minSdk = 26
    targetSdk = 36
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    vectorDrawables {
      useSupportLibrary = true
    }
  }

  signingConfigs {
    if (keystorePropertiesFile.exists()) {
      create("release") {
        storeFile = rootProject.file(keystoreProperty("storeFile"))
        storePassword = keystoreProperty("storePassword")
        keyAlias = keystoreProperty("keyAlias")
        keyPassword = keystoreProperty("keyPassword")
      }
    }
  }

  buildTypes {
    release {
      isMinifyEnabled = true
      isShrinkResources = true

      if (keystorePropertiesFile.exists()) {
        signingConfig = signingConfigs.getByName("release")
      } else {
        error("keystore.properties is required for the signed release build")
      }

      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
      )
    }

    create("unsignedRelease") {
      initWith(getByName("release"))

      signingConfig = null

      matchingFallbacks += listOf("release")
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  buildFeatures {
    compose = true
    buildConfig = true
    // viewBinding = true
  }

  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
}

dependencies {
  implementation("androidx.documentfile:documentfile:1.0.1")

  implementation("androidx.palette:palette:1.0.0")

  implementation("androidx.credentials:credentials:1.5.0")
  implementation("androidx.credentials:credentials-play-services-auth:1.5.0")
  implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")

  implementation("androidx.appcompat:appcompat:1.7.0")
  implementation("com.google.android.material:material:1.12.0")

  implementation("androidx.core:core-ktx:1.13.1")
  implementation("androidx.activity:activity-compose:1.9.2")

  implementation(platform("androidx.compose:compose-bom:2024.09.03"))

  implementation("androidx.compose.ui:ui")
  implementation("androidx.compose.ui:ui-graphics")
  implementation("androidx.compose.ui:ui-tooling-preview")

  implementation("androidx.compose.material:material")
  implementation("androidx.compose.material3:material3")
  implementation("androidx.compose.material:material-icons-extended")

  debugImplementation("androidx.compose.ui:ui-tooling")
  debugImplementation("androidx.compose.ui:ui-test-manifest")

  implementation("androidx.navigation:navigation-compose:2.8.2")

  implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
  implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.6")
  implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")

  implementation(platform("com.google.firebase:firebase-bom:33.5.1"))

  implementation("com.google.firebase:firebase-auth-ktx")
  implementation("com.google.firebase:firebase-analytics-ktx")
  implementation("com.google.firebase:firebase-storage")

  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.8.1")

  implementation("com.google.android.gms:play-services-auth:21.2.0")

  // Room local database
  val roomVersion = "2.7.2"
  implementation("androidx.room:room-runtime:$roomVersion")
  implementation("androidx.room:room-ktx:$roomVersion")
  ksp("androidx.room:room-compiler:$roomVersion")

  // osmdroid - OpenStreetMap for Android without an API key
  implementation("org.osmdroid:osmdroid-android:6.1.20")

  // Retrofit + Gson
  implementation("com.squareup.retrofit2:retrofit:2.11.0")
  implementation("com.squareup.retrofit2:converter-gson:2.11.0")

  // Moshi + KSP codegen
  implementation("com.squareup.retrofit2:converter-moshi:2.11.0")
  implementation("com.squareup.moshi:moshi:1.15.1")
  ksp("com.squareup.moshi:moshi-kotlin-codegen:1.15.1")

  // OkHttp
  implementation("com.squareup.okhttp3:okhttp:4.12.0")
  implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

  // Coil
  implementation("io.coil-kt:coil-compose:2.7.0")

  testImplementation("junit:junit:4.13.2")

  androidTestImplementation("androidx.test.ext:junit:1.2.1")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}
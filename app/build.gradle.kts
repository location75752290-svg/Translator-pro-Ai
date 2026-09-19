plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.google.devtools.ksp)
  alias(libs.plugins.roborazzi)
  alias(libs.plugins.secrets)
}

android {
  namespace = "com.example"
  compileSdk = 35

  defaultConfig {
    applicationId = "com.aistudio.translatorpro.ai"
    minSdk = 24
    targetSdk = 35
    versionCode = 2
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    multiDexEnabled = true
    multiDexKeepFile = file("maindex.txt")
    multiDexKeepProguard = file("multidex-config.pro")
  }

  signingConfigs {
    create("release") {
      fun envOrNull(key: String): String? = System.getenv(key)?.trim()?.takeIf { it.isNotBlank() }

      val envKsPath = envOrNull("KEYSTORE_PATH")
      val ksFile = when {
        envKsPath != null -> {
          val f1 = rootProject.file(envKsPath)
          if (f1.exists() && f1.length() > 0) f1 else file(envKsPath)
        }
        rootProject.file("app/release.keystore").exists() && rootProject.file("app/release.keystore").length() > 0 -> rootProject.file("app/release.keystore")
        rootProject.file("release.keystore").exists() && rootProject.file("release.keystore").length() > 0 -> rootProject.file("release.keystore")
        rootProject.file("app/my-upload-key.jks").exists() && rootProject.file("app/my-upload-key.jks").length() > 0 -> rootProject.file("app/my-upload-key.jks")
        rootProject.file("my-upload-key.jks").exists() && rootProject.file("my-upload-key.jks").length() > 0 -> rootProject.file("my-upload-key.jks")
        rootProject.file("debug.keystore").exists() && rootProject.file("debug.keystore").length() > 0 -> rootProject.file("debug.keystore")
        else -> null
      }

      val fallbackFile = rootProject.file("release-fallback.keystore")
      if (ksFile != null && ksFile.exists() && ksFile.length() > 0) {
        val isDebugKs = ksFile.name.contains("debug")
        storeFile = ksFile
        storePassword = envOrNull("KEYSTORE_PASSWORD") ?: envOrNull("STORE_PASSWORD") ?: "android"
        keyAlias = envOrNull("KEY_ALIAS") ?: if (isDebugKs) "androiddebugkey" else "upload"
        keyPassword = envOrNull("KEY_PASSWORD") ?: envOrNull("STORE_PASSWORD") ?: "android"
      } else if (fallbackFile.exists() && fallbackFile.length() > 0) {
        storeFile = fallbackFile
        storePassword = "android"
        keyAlias = "androiddebugkey"
        keyPassword = "android"
      } else {
        try {
          ProcessBuilder("keytool", "-genkeypair", "-v", "-keystore", fallbackFile.absolutePath, "-alias", "androiddebugkey", "-keyalg", "RSA", "-keysize", "2048", "-validity", "10000", "-storepass", "android", "-keypass", "android", "-dname", "CN=Android,O=Android,C=US").start().waitFor()
          if (fallbackFile.exists()) {
            storeFile = fallbackFile
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
          }
        } catch (_: Exception) {}
      }
    }
    create("debugConfig") {
      val dbg = rootProject.file("debug.keystore")
      if (dbg.exists() && dbg.length() > 0) {
        storeFile = dbg
        storePassword = "android"
        keyAlias = "androiddebugkey"
        keyPassword = "android"
      }
    }
  }

  buildTypes {
    release {
      isCrunchPngs = false
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      signingConfig = signingConfigs.getByName("release")
    }
    debug { signingConfig = signingConfigs.getByName("debugConfig") }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
  }
  kotlin {
    compilerOptions {
      jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
  }
  buildFeatures {
    compose = true
    buildConfig = true
  }
  testOptions { unitTests { isIncludeAndroidResources = true } }
  dependenciesInfo {
    includeInApk = false
    includeInBundle = true
  }
}

// Configure the Secrets Gradle Plugin to use .env and .env.example files
// to match the convention used in Web projects.
secrets {
  propertiesFileName = ".env"
  defaultPropertiesFileName = ".env.example"
  ignoreList.add("FIREBASE_APPCHECK_DEBUG_TOKEN")
}

// Some unused dependencies are commented out below instead of being removed.
// This makes it easy to add them back in the future if needed.
dependencies {
  implementation("com.google.android.gms:play-services-ads:23.3.0")
  implementation("com.google.guava:guava:33.2.1-android")
  implementation(platform(libs.androidx.compose.bom))
  // implementation(libs.accompanist.permissions)
  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.camera.camera2)
  implementation(libs.androidx.camera.core)
  implementation(libs.androidx.camera.lifecycle)
  implementation(libs.androidx.camera.view)
  implementation(libs.androidx.compose.material.icons.core)
  implementation(libs.androidx.compose.material.icons.extended)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.ui.graphics)
  implementation(libs.androidx.compose.ui.tooling.preview)
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.datastore.preferences)
  implementation(libs.androidx.lifecycle.runtime.compose)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.lifecycle.viewmodel.compose)
  implementation(libs.androidx.navigation.compose)
  implementation(libs.androidx.room.ktx)
  implementation(libs.androidx.room.runtime)
  implementation(libs.coil.compose)
  implementation(libs.converter.moshi)
  implementation(libs.kotlinx.coroutines.android)
  implementation(libs.kotlinx.coroutines.core)
  implementation(libs.logging.interceptor)
  implementation(libs.moshi.kotlin)
  implementation(libs.okhttp)
  implementation(libs.play.services.location)
  implementation(libs.play.services.mlkit.translate)
  implementation(libs.retrofit)
  testImplementation(libs.androidx.compose.ui.test.junit4)
  testImplementation(libs.androidx.core)
  testImplementation(libs.androidx.junit)
  testImplementation(libs.junit)
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.robolectric)
  testImplementation(libs.roborazzi)
  testImplementation(libs.roborazzi.compose)
  testImplementation(libs.roborazzi.junit.rule)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.compose.ui.test.junit4)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.runner)
  debugImplementation(libs.androidx.compose.ui.test.manifest)
  debugImplementation(libs.androidx.compose.ui.tooling)
  "ksp"(libs.androidx.room.compiler)
  "ksp"(libs.moshi.kotlin.codegen)
}

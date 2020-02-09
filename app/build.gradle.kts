plugins {
  id("com.android.application")
  kotlin("android")
  kotlin("android.extensions")
  kotlin("kapt")
}

val preDexEnabled = "true" == System.getProperty("pre-dex", "true")

android {
  compileSdkVersion(Versions.compileSdk)

  buildTypes {
    named("release") {
      isDebuggable = false
      versionNameSuffix = "-release"
      isMinifyEnabled = true
      isShrinkResources = true
      proguardFiles(getDefaultProguardFile(Versions.proguard_android), Versions.proguard_common, Versions.proguard_specific)
      buildConfigField("boolean", "ENABLE_LOGGING", "false")
    }
  }

  defaultConfig {
    applicationId = Versions.applicationId
    minSdkVersion(Versions.minSdk)
    targetSdkVersion(Versions.targetSdk)
    versionCode = Versions.versionCode
    versionName = Versions.versionName
    testInstrumentationRunner = Dependencies.android_junit_runner
  }

  dexOptions {
    preDexLibraries = preDexEnabled
  }

  kapt {
    correctErrorTypes = true
  }

  dataBinding {
    isEnabled = true
  }

  androidExtensions {
    isExperimental = true
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }

  sourceSets["test"].java.srcDir("src/test-common/java")
}

tasks {
  fun getVersionName(): String {
    return "${Versions.versionMajor}.${Versions.versionMinor}.${Versions.versionPatch}"
  }
}

dependencies {
  implementation(Dependencies.support_appcompat)
  implementation(Dependencies.support_design)
  implementation(Dependencies.support_card_view)
  implementation(Dependencies.support_recycler_view)
  implementation(Dependencies.constraint_layout)

  implementation(Dependencies.kotlin_stdlib)

  implementation(Dependencies.arch_lifecycle)
  implementation(Dependencies.liveData_ktx)
  implementation(Dependencies.arch_room)
  kapt(Dependencies.arch_room_compiler)
  testImplementation(Dependencies.arch_core_testing)
  testImplementation(Dependencies.mock_web_server)

  implementation(Dependencies.dagger)
  kapt(Dependencies.dagger_compiler)

  implementation(Dependencies.dagger_android)
  implementation(Dependencies.dagger_android_support)
  kapt(Dependencies.dagger_android_compiler)

  implementation(Dependencies.rx_java)
  implementation(Dependencies.rx_android)

  implementation(Dependencies.preference_room)
  kapt(Dependencies.preference_room_compiler)

  implementation(Dependencies.gson)

  implementation(Dependencies.retrofit)
  implementation(Dependencies.retrofit_gson)

  implementation(Dependencies.timber)
  debugImplementation(Dependencies.memory_leak_debug)

  implementation(Dependencies.glide)
  kapt(Dependencies.glide_compiler)

  testImplementation(Dependencies.junit)
  testImplementation(Dependencies.mockito)
  testImplementation(Dependencies.mockito_kotlin)
  androidTestImplementation(Dependencies.mockito_android)
  androidTestImplementation(Dependencies.espresso)
}
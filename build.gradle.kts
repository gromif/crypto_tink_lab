plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.nevidimka655.tink_lab"
    compileSdk = project.property("compileSdk").toString().toInt()

    defaultConfig {
        minSdk = project.property("minSdk").toString().toInt()
        testOptions {
            targetSdk = project.property("targetSdk").toString().toInt()
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        buildFeatures.compose = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}


kotlin {
    jvmToolchain(project.property("kotlinJvmToolchainVersion").toString().toInt())
}
dependencies {
    api(project(":crypto:tink"))
    api(project(":ui:compose-core"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
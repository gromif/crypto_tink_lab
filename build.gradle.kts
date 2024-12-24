plugins {
    alias(libs.plugins.astracrypt.android.library)
    alias(libs.plugins.astracrypt.android.library.compose)
    alias(libs.plugins.astracrypt.android.hilt)
    alias(libs.plugins.astracrypt.android.hilt.compose)
}

android {
    namespace = "com.nevidimka655.tink_lab"
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(projects.domain.tinkLab)
    implementation(projects.di.tinkLab)

    implementation(projects.core.resources)
    implementation(projects.core.utils)
    implementation(projects.core.tink)
    implementation(projects.di.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
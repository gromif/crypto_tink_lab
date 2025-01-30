plugins {
    alias(libs.plugins.astracrypt.android.library)
    alias(libs.plugins.astracrypt.android.hilt)
}

android {
    namespace = "com.nevidimka655.tink_lab.di"
}

dependencies {
    implementation(projects.features.tinkLab.domain)
    implementation(projects.features.tinkLab.data)

    implementation(projects.core.utils)
    implementation(projects.core.crypto.tink)
}
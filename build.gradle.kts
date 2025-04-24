// Top-level build.gradle.kts
buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal() // Required for KSP
    }
    dependencies {
        val navVersion = "2.7.5"

    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    // Remove the standalone KSP plugin (it's already in buildscript)
}
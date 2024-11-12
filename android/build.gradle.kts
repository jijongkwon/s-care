// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    id("com.google.devtools.ksp") version "1.9.21-1.0.15" apply false
    id("com.chaquo.python") version "16.0.0" apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.gradle)  // Android Gradle Plugin 버전 확인
        classpath(libs.google.services) // Google Services Plugin 추가
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.44")
    }
}
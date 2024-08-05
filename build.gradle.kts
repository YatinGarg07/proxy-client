// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript{
    dependencies {
        classpath("io.realm:realm-gradle-plugin:10.4.0")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.51.1")
    }
}
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
//    alias(libs.plugins.kotlin.compose) apply false
//    id("io.realm.kotlin") version "1.11.0" apply false
    alias(libs.plugins.realm) apply false
    alias(libs.plugins.kotlin.parcelize) apply false

    id ("com.google.dagger.hilt.android") version "2.46.1" apply false
}
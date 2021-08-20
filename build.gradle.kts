// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Plugins.Gradle.android)
        classpath(Plugins.Gradle.kotlin)
        classpath(Plugins.Gradle.jetpackSafeArgs)
    }
}

tasks.register("clean", Delete::class){
    delete(rootProject.buildDir)
}
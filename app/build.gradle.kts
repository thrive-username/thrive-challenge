plugins {
    id("com.android.application")
    id("kotlin-android")
    id ("kotlin-kapt")
}

object Libs {
    const val COMPOSE_VERSION = "1.0.0"
    const val DAGGER_VERSION  = "2.38.1"
    const val ROOM_VERSION = "2.3.0"
    object Jetpack {
        const val NAVIGATION_VERSION = "2.3.5"
    }
}

android {
    compileSdk = 30

    defaultConfig {
        applicationId = "com.triveglobal.challenge"
        minSdk = 21
        targetSdk = 30
        versionCode = 1
        versionName = "1.0"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release"){
            isMinifyEnabled =  false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),"proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs += "-Xopt-in=kotlin.time.ExperimentalTime" //Needed for turbine
    }

    flavorDimensions("default")

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Libs.COMPOSE_VERSION
        kotlinCompilerVersion = "1.5.10"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    productFlavors {
        create("sandbox") {
            buildConfigField("String", "BASE_URL", "\"https://ivy-ios-challenge.herokuapp.com/index.html\"")
        }
    }



}


dependencies {

    implementation("androidx.core:core-ktx:1.6.0")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("com.google.android.material:material:1.4.0")
    //Compose
    implementation("androidx.compose.ui:ui:${Libs.COMPOSE_VERSION}")
    implementation("androidx.compose.material:material:${Libs.COMPOSE_VERSION}")
    implementation("androidx.compose.ui:ui-tooling-preview:${Libs.COMPOSE_VERSION}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation("androidx.activity:activity-compose:1.3.1")
    //Dagger
    implementation("com.google.dagger:dagger:${Libs.DAGGER_VERSION}")
    implementation("com.google.dagger:dagger-android-support:${Libs.DAGGER_VERSION}")
    kapt("com.google.dagger:dagger-compiler:${Libs.DAGGER_VERSION}")
    //Jodatime
    implementation("joda-time:joda-time:2.10.10")
    //Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.0")
    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.5.0")
    //Room
    implementation("androidx.room:room-runtime:${Libs.ROOM_VERSION}")
    implementation("androidx.room:room-ktx:${Libs.ROOM_VERSION}")
    kapt("androidx.room:room-compiler:${Libs.ROOM_VERSION}")
    //Jetpack navigation
    implementation("androidx.navigation:navigation-fragment-ktx:${Libs.Jetpack.NAVIGATION_VERSION}")
    implementation("androidx.navigation:navigation-ui-ktx:${Libs.Jetpack.NAVIGATION_VERSION}")


    //Testing
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:3.11.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.1")
    testImplementation("app.cash.turbine:turbine:0.6.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:3.2.0")
}
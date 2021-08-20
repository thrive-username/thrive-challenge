plugins {
    id("com.android.application")
    id("kotlin-android")
    id ("kotlin-kapt")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
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
        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas") //We want to keep the schema versioning
            }
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
        kotlinCompilerExtensionVersion = Libraries.Compose.version
        kotlinCompilerVersion = "1.5.10"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    productFlavors {
        create("sandbox") {
            buildConfigField("String", "BASE_URL", "\"https://ivy-ios-challenge.herokuapp.com/\"")
        }
    }

    kapt {
        correctErrorTypes = true //Needed for AssistedInjection
    }



}


dependencies {
    implementation(Libraries.AndroidX.core)
    implementation(Libraries.AndroidX.appCompat)
    implementation(Libraries.AndroidX.lifecycleRuntimeKtx)
    implementation(Libraries.Google.material)
    implementation(Libraries.Compose.ui)
    implementation(Libraries.Compose.material)
    debugCompileOnly(Libraries.Compose.toolingPreview)
    implementation(Libraries.Compose.runtimeLiveData)
    implementation(Libraries.Compose.constraintLayout)
    implementation(Libraries.Compose.activity)
    implementation(Libraries.Dagger.core)
    implementation(Libraries.Dagger.androidSupport)
    kapt(Libraries.Dagger.compiler)
    kapt(Libraries.Dagger.androidProcessor)
    implementation(Libraries.JodaTime.core)
    implementation(Libraries.Jetbrains.kotlinCoroutines)
    implementation(Libraries.Square.Retrofit.core)
    implementation(Libraries.Square.Retrofit.gsonConverter)
    implementation(Libraries.Square.OkHttp.loggingInterceptor)
    implementation(Libraries.Jetpack.Room.runtime)
    implementation(Libraries.Jetpack.Room.ktk)
    kapt(Libraries.Jetpack.Room.compiler)
    implementation(Libraries.Jetpack.Navigation.fragment)
    implementation(Libraries.Jetpack.Navigation.uiKtx)
    testImplementation(Testing.junit)
    testImplementation(Testing.Mockito.core)
    testImplementation(Testing.Mockito.kotlin)
    testImplementation(Testing.kotlinCoroutines)
    testImplementation(Testing.turbine)
    testImplementation(Testing.androidArcCoreTesting)
}
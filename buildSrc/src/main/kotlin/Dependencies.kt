object Plugins {
    object Gradle {
        const val android = "com.android.tools.build:gradle:7.0.0"
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.10"
        const val jetpackSafeArgs = "androidx.navigation:navigation-safe-args-gradle-plugin:${Libraries.Jetpack.Navigation.version}"
    }
}

object Libraries {

    object Google {
        const val material = "com.google.android.material:material:1.4.0"
    }

    object AndroidX {
        const val core = "androidx.core:core-ktx:1.6.0"
        const val appCompat = "androidx.appcompat:appcompat:1.3.1"
        const val lifecycleRuntimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:2.3.1"
    }

    object Jetpack {

        object Navigation {
            internal const val version = "2.3.5"
            const val fragment = "androidx.navigation:navigation-fragment-ktx:$version"
            const val uiKtx = "androidx.navigation:navigation-ui-ktx:$version"
        }

        object Room {
            private const val version = "2.3.0"
            const val runtime = "androidx.room:room-runtime:$version"
            const val ktk = "androidx.room:room-ktx:$version"
            const val compiler = "androidx.room:room-compiler:$version"
        }
    }

    object Compose {
        const val version = "1.0.0"
        const val ui = "androidx.compose.ui:ui:$version"
        const val material = "androidx.compose.material:material:$version"
        const val toolingPreview  = "androidx.compose.ui:ui-tooling-preview:$version"
        const val runtimeLiveData = "androidx.compose.runtime:runtime-livedata:$version"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout-compose:1.0.0-beta02"
        const val activity = "androidx.activity:activity-compose:1.3.1"
    }

    object Dagger {
        private const val version = "2.38.1"
        const val core = "com.google.dagger:dagger:$version"
        const val androidSupport = "com.google.dagger:dagger-android-support:$version"
        const val compiler = "com.google.dagger:dagger-compiler:$version"
        const val androidProcessor = "com.google.dagger:dagger-android-processor:$version"
    }

    object JodaTime {
        const val core = "joda-time:joda-time:2.10.10"
    }

    object Jetbrains {
        const val kotlinCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.0"
    }

    object Square {

        object Retrofit {
            const val core = "com.squareup.retrofit2:retrofit:2.9.0"
            const val gsonConverter = "com.squareup.retrofit2:converter-gson:2.5.0"
        }

        object OkHttp {
            const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:4.2.1"
        }
    }

}


object Testing {
    const val junit = "junit:junit:4.13.2"

    object Mockito {
        const val core = "org.mockito:mockito-core:3.11.2"
        const val kotlin = "org.mockito.kotlin:mockito-kotlin:3.2.0"
    }

    const val kotlinCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.1"
    const val turbine = "app.cash.turbine:turbine:0.6.0"
    const val androidArcCoreTesting = "android.arch.core:core-testing:1.1.1"
}

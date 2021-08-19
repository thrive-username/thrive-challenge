object Plugins {
    object Jetpack {
        const val safeArgs = "androidx.navigation:navigation-safe-args-gradle-plugin:${Libraries.Jetpack.Navigation.version}"
    }
}

object Libraries {
    const val COMPOSE_VERSION = "1.0.0"
    const val DAGGER_VERSION  = "2.38.1"
    const val ROOM_VERSION = "2.3.0"
    object Jetpack {
        object Navigation {
            internal const val version = "2.3.5"
            const val fragment = "androidx.navigation:navigation-fragment-ktx:$version"
        }
    }
}


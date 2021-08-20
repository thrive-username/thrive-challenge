# Challenge APP 

The application should be able to run offline, and syncing on-demand to get new books, allowing the user to checkout books

## Architecture
The architecture has 3 layers, Presentation -> Repository -> Data Source, using Dagger as DI to provide instances of types
wherever needed.

## Presentation
Compose + Jetpack Navigation + MVVM + Kotlin Flows + Coroutines
2 Simple Screens Book Feed and Book  Details

## Repository
Kotlin Flows + Coroutines
Provides a stream of books and a layer of abstraction to the presentation layer without know where the data (remote/local) is coming from

## Data Source
Room + Retrofit + OkHttp
Expose Remote and Local Data sources to store/get books

### How to run the app

Given that  the app uses compose, latest version of Android Studio (Artic), it  can we also run from the command line but Java 11 is required

### TODOs/Improvements
* Update proguard for libraries used (gson will break our model serialization otherwise)
* Implement feature to Add Book
* Allow the to see the book details on landscape mode in the same screen as the feed
* Add instrumentation tests
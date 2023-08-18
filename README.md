<h1 align="center">Podcast Time</h1></br>
<p align="center">  
A demo Podcast app using <a href="https://developer.android.com/reference/android/service/media/MediaBrowserService" target="_blank"> MediaBrowserService </a> based on MVVM architecture.<br>
Media browser services enable applications to browse media content provided by an application and ask the application to start playing it. They may also be used to control content that is already playing by way of a MediaSession.
</p>
</br>

## Download
Go to the [Releases](https://github.com/skydoves/DisneyMotions/releases) to download the latest APK.

## Screenshots
<p align="center">
<img src="/preview/preview001.jpg" width="32%"/>
<img src="/preview/preview002.jpg" width="32%"/>
<img src="/preview/preview003.jpg" width="32%"/>
<img src="/preview/preview004.jpg" width="32%"/>
<img src="/preview/preview005.jpg" width="32%"/>
<img src="/preview/preview006.jpg" width="32%"/>
<img src="/preview/preview007.jpg" width="32%"/>
<img src="/preview/preview008.jpg" width="32%"/>
<img src="/preview/preview009.jpg" width="32%"/>
<img src="/preview/preview010.jpg" width="32%"/>
<img src="/preview/preview011.jpg" width="32%"/>
<img src="/preview/preview012.jpg" width="32%"/>
</p>

## Tech stack & Open-source libraries
- Minimum SDK level 22
- 100% [Kotlin](https://kotlinlang.org/) based + [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [StateFlow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-state-flow/) for asynchronous.
- JetPack
  - Lifecycle - dispose observing data when lifecycle state changes.
  - ViewModel - UI related data holder, lifecycle aware.
  - Room Persistence - construct database.
  - Navigation component.
  - DataStore - Preferences.
- Architecture
  - MVVM Architecture (ViewBinding - ViewModel - Model)
  - Repository pattern
  - [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) - dependency injection
- [Material3](https://m3.material.io/)
- [Retrofit2 & Gson](https://github.com/square/retrofit) - constructing the REST API
- [OkHttp3](https://github.com/square/okhttp) - implementing interceptor, logging and mocking web server
- [Glide](https://github.com/bumptech/glide) - loading images
- [Exo player](https://github.com/google/ExoPlayer) - an application level media player for Android.

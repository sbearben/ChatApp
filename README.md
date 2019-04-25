# ChatApp
Android realtime messaging app written in Kotlin that features user login/registration and the ability to add friends. Realtime messaging utilizes a WebSocket when app is in the foreground and Firebase Cloud Messaging when in the background.

### Built With
Written with Clean Architecture in mind and Single Activity Architecture, as well as MVVM for the presentation layer. Main libraries used were:
* [Tinder/Scarlet](https://github.com/Tinder/Scarlet): A cross-platform messaging solution that lets you reliably deliver messages at no cost
* [Firebase Cloud Messaging](https://firebase.google.com/docs/cloud-messaging/): A Retrofit inspired WebSocket client for Kotlin, Java, and Android
* [Room Persistence Library](https://developer.android.com/topic/libraries/architecture/room): An abstraction layer over SQLite to allow for more robust database access
* [Navigation Component](https://developer.android.com/jetpack/androidx/releases/navigation): A framework for navigating between 'destinations' within an Android application
* [Retrofit](https://square.github.io/retrofit/): A type-safe HTTP client for Android and Java
* [RxJava](https://github.com/ReactiveX/RxJava): Reactive Extensions for the JVM
* [Dagger2](https://github.com/google/dagger): A fast dependency injector for Android and Java

Testing libraries include:
* [Mockito](https://site.mockito.org/): Mocking framework for unit tests in Java
* [Roboelectric](http://robolectric.org/): Brings fast and reliable unit tests to Android
* [Espresso](https://developer.android.com/training/testing/espresso): Write concise, beautiful, and reliable Android UI tests.

### Features
* Login/registration
* Add friends
* Send and receive realtime messages

### Screenshots
<img src="../assets/login.png?raw=true">  <img src="../assets/websocket_messaging_250.gif?raw=true">  <img src="../assets/new_friend_250.gif?raw=true">
</br>
<img src="../assets/friends.png?raw=true">  <img src="../assets/send_friend_request.png?raw=true">  <img src="../assets/new_message_250.gif?raw=true">

### License
    Copyright (C) 2018 Armon Khosravi

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License. 

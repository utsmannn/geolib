---
title: Prerequisite
---

## Dependencies
```jsx
// Kotlin coroutine
implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.2-native-mt'

// Google maps if needed
implementation 'com.google.android.gms:play-services-maps:17.0.0'
implementation 'com.google.maps.android:android-maps-utils:0.5'
implementation 'com.google.maps.android:maps-ktx:2.1.1'

// Google play location
implementation 'com.google.android.gms:play-services-location:17.1.0'
```

## Jitpack Repository
```jsx
allprojects {
    repositories {

        // add this url
        maven { url 'https://jitpack.io' }
    }
}
```

## Here maps API Key
You need HERE Api, open https://developer.here.com/. Create new account or login and create REST api key.
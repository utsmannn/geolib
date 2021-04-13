---
title: Getting Started
slug: /
---

This is collection libraries of utility Google Maps SDK for Android. Build for modern architecture with Kotlin and Coroutine.

Layer customization on Google Maps SDK is not easy. I've done a lot of code work with it. I try to summarize my research in a library which contains several artifacts. The artifacts become a standalone library.

## Artifact and feature
- Location (`com.github.utsmannn.geolib:location`)
- Routes (`com.github.utsmannn.geolib:routes`)
- Polyline Helper (`com.github.utsmannn.geolib:polyline`)
- Marker View (`com.github.utsmannn.geolib:marker`)

### [Location](/docs/artifacts/location-lib)
With location library you can access current location, observer current location and get place or address of current and specified location (search).
### Routes
You able to search route between two location.
### Polyline Helper
You can animating the polyline with easier code. There is a feature to determine the draw of the animation.

### Marker View
Parse layout to original marker or you can create any view as marker. I added the layer for create any view as marker instead original marker.

## Prequisite
### Dependencies
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

### Jitpack Repository
```jsx
allprojects {
    repositories {

        // add this url
        maven { url 'https://jitpack.io' }
    }
}
```

### Here maps API Key
You need HERE Api, open https://developer.here.com/. Create new account or login and create REST api key.
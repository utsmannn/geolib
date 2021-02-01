
# Location and Places Helper
Libraries for Location, Routes and Polyline utils for GoogleMaps

## The libraries
- [Location library](location)
- [Routes Library](routes)
- [Polyline-utils for GoogleMaps](polyline-utils)

## Prerequisite
1. HERE Api
You need HERE Api, open https://developer.here.com/. Create new account or login and create api key.
![](images/here_api.png)

2. Google dependencies
```groovy
// Google maps if needed
implementation 'com.google.android.gms:play-services-maps:17.0.0'
implementation 'com.google.maps.android:android-maps-utils:0.5'
implementation 'com.google.maps.android:maps-ktx:2.1.1'

// Google play location
implementation 'com.google.android.gms:play-services-location:17.1.0'
```

## Screenshot
|Get location|Search location|
|---|---|
|![](images/current_location.gif)|![](images/search_location.gif)|

|Route|Polyline-utils|
|---|---|
|![](images/route.gif)|![](images/polyline_animate.gif)|

---


---
title: Location
---

With location library you can access current location, observer current location and get place or address of current and specified location (search).

## Download
![](https://jitpack.io/v/utsmannn/geolib.svg)
```kotlin
implementation 'com.github.utsmannn.geolib:location:{last_version}'
```
---

## Prerequisite class
You need `FusedLocationProviderClient`
```kotlin
val fusedLocation = LocationServices.getFusedLocationProviderClient(context)
```

## Create `PlaceLocation`
```kotlin
val placesLocation = fusedLocation.createPlacesLocation(HERE_API_KEY)
```
---

## Observer location
This function build under `CoroutineScope` with return `Flow`
```kotlin
MainScope().launch {
    // start observer location
    placesLocation.getLocationFlow()
        .collect { location ->
            // location result
        }
}
```

## Get current location 
```kotlin
MainScope().launch {
    val location = placesLocation.getLocationFlow().first()
}
```

## Get comparator location (prev and current)
```kotlin
MainScope().launch {
placesLocation.getComparisonLocation()
    .collect { comparisonLocation ->
        val prevLocation = comparisonLocation.previousLocation
        val currentLocation = comparisonLocation.currentLocation
    }
}
```
---

### Place Data
|Param|type|desc|
|---|---|---|
|`hereId`|`String`|Id place from Here API|
|`title`|`String`|Title of place|
|`address`|`String`|Address of place|
|`district`|`String`|District of place|
|`city`|`String`|City of place|
|`location`|`Location`|Location of place|
|`distance`|`Double`|Distance of current location and place|
|`category`|`String` nullable |Category of place|

## Get place data from location
```kotlin
val places = placesLocation.getPlacesLocation(location)
```

## Search nearby place
Search place is searching nearby place on location by query with data result `List<PlaceData>`
```kotlin
val data = placesLocation.searchPlaces(location, query)
```
---

---
title: Location
---

With location library you can access current location, observer current location and get place or address of current and specified location (search).

## Download
![](https://artifactory-badge.herokuapp.com/artifactory?url=https://utsmannn.jfrog.io/artifactory/android/com/utsman/geolib/location/)
```kotlin
implementation 'com.utsman.geolib:location:{last_version}'
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

## Current location

### Observer current location
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

### Get current location 
```kotlin
MainScope().launch {
    val location = placesLocation.getLocationFlow().first()
}
```

### Get comparator current location (prev and current)
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

## Place Location

### Structure class of `PlaceData`
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

### Get place from location
```kotlin
val result: ResultState<List<PlaceData>> = placesLocation.getPlacesLocation(location)

result.doOnSuccess { places ->
    // handle success
}

result.doOnFailure {
    // handler failure
}
```

### Search nearby place
Search place is searching nearby place on location by query with data result `List<PlaceData>`
```kotlin
val result: ResultState<List<PlaceData>> = placesLocation.searchPlaces(location, query)

result.doOnSuccess { places ->
    // handle success
}

result.doOnFailure {
    // handler failure
}
```
---

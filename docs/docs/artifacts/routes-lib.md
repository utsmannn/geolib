---
title: Routes
---
You able to search route between two location.

## Download
![](https://artifactory-badge.herokuapp.com/artifactory?url=https://utsmannn.jfrog.io/artifactory/android/com/utsman/geolib/location/)
```jsx
implementation 'com.utsman.geolib:routes:{last_version}'
```
---

## Create `PlaceRoute`
```jsx
val placesRoute = createPlacesRoute(HERE_MAPS_API)
```

## Search route between locations
For search a route, use `searchRoute` DSL builder.
```jsx

val buaran: Location = Location("").apply {
    latitude = -6.2220484
    longitude = 106.9217385
}

val depok: Location = Location("").apply {
    latitude = -6.4090897
    longitude = 106.8122967
}

val result: ResultState<RouteData> = placesRoute.searchRoute {
    startLocation = buaran // location start
    endLocation = depok // location end
    transportMode = TransportMode.BIKE
}

result.doOnSuccess { routes ->
    // handle success
}

result.doOnFailure {
    // handler failure
}

```
---

### Builder
|Param|type|desc|
|---|---|---|
|`startLocation`|`Location`|start destination|
|`endLocation`|`Location`|end destination|
|`transportMode`|`TransportMode`|transport mode, available `CAR` and `BIKE`, default is `CAR`|

### Route Data
|Param|type|desc|
|---|---|---|
|`encodedPolyline`|`String`|encoded of geometry|
|`geometries`|`List<LatLng>`|list LatLng of geometry|
|`length`|`Float`|Length of polyline decoded|

---
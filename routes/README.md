
# Route
This feature for search route between location, include ***LatLng Geometry*** and ***encoded path***

## Download
```groovy
implementation 'com.utsman.geolib:routes:1.1.0'
```

## Create `PlaceRoute`
```kotlin
val placesRoute = createPlacesRoute(HERE_MAPS_API)
```

## Search route between locations
For search a route, use `searchRoute` DSL builder.
```kotlin
val route = placesRoute.searchRoute {
    startLocation = buaran // location start
    endLocation = rawamangun // location end
    transportMode = TransportMode.BIKE
}
```

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
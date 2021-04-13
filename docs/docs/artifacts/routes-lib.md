---
title: Routes Library
---
You able to search route between two location.

## Download
![](https://jitpack.io/v/utsmannn/geolib.svg)
```jsx
implementation 'com.github.utsmannn.geolib:routes:{last_version}'
```
---

## Create `PlaceRoute`
```jsx
val placesRoute = createPlacesRoute(HERE_MAPS_API)
```

## Search route between locations
For search a route, use `searchRoute` DSL builder.
```jsx
val route = placesRoute.searchRoute {
    startLocation = buaran // location start
    endLocation = rawamangun // location end
    transportMode = TransportMode.BIKE
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


## Location and Places
This feature include ***observer location***, ***get current location***, ***get place data*** and ***search nearby place by location***

### Prerequisite
You need `FusedLocationProviderClient`
```kotlin
val fusedLocation = LocationServices.getFusedLocationProviderClient(context)
```

### Create `PlaceLocation`
```kotlin
val placesLocation = fusedLocation.createPlacesLocation(HERE_API_KEY)
```

### Observer location
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

#### Place Data
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

### Get place data from location
```kotlin
val places = placesLocation.getPlacesLocation(location)
```

### Search nearby place
Search place is searching nearby place on location by query with data result `List<PlaceData>`
```kotlin
val data = placesLocation.searchPlaces(location, query)
```

## Route
This feature for search route between location, include ***LatLng Geometry*** and ***encoded path***

### Create `PlaceRoute`
```kotlin
val placesRoute = createPlacesRoute(HERE_MAPS_API)
```

### Search route between locations
For search a route, use `searchRoute` DSL builder.
```kotlin
val route = placesRoute.searchRoute {
    startLocation = buaran // location start
    endLocation = rawamangun // location end
    transportMode = TransportMode.BIKE
}
```

#### Builder
|Param|type|desc|
|---|---|---|
|`startLocation`|`Location`|start destination|
|`endLocation`|`Location`|end destination|
|`transportMode`|`TransportMode`|transport mode, available `CAR` and `BIKE`, default is `CAR`|

#### Route Data
|Param|type|desc|
|---|---|---|
|`encodedPolyline`|`String`|encoded of geometry|
|`geometries`|`List<LatLng>`|list LatLng of geometry|
|`length`|`Float`|Length of polyline decoded|

## Animate polyline
### Create `PlacesPolyline`
```kotlin
val polylineBuilder = googleMap.createPlacesPolylineBuilder()
val polyline = polylineBuilder.createAnimatePolyline()
```

#### Configuration builder
|Param|type|desc|
|---|---|---|
|`withPrimaryPolyline(PolylineOptions.() -> Unit)`|`PlacesPolylineBuilder`|DSL param for add primary polyline|
|`withAccentPolyline(PolylineOptions.() -> Unit)`|`PlacesPolylineBuilder`|DSL param for add accent polyline|
|`withCameraAutoFocus(Boolean)`|`PlacesPolylineBuilder`|Set for auto zoom camera|
|`withStackAnimationMode(StackAnimationMode)`|`PlacesPolylineBuilder`|Set for animate type polyline|
|`createAnimatePolyline()`|`PlacesPolyline`|to create `PlacesPolyline`|

#### Stack Animation Mode
This is animation type of polyline

### Animating polyline
#### Start animate polyline
To start animate use `startAnimate(geometries: List<LatLng>, duration: Long)` and result is `PlacesPointPolyline`
```kotlin
val point = polyline.startAnimate(geometries, 2000)
```

#### Add points in polyline
You can added polyline in existing polyline
```kotlin
val nextPoint = point.addPoints(nextGeometries)
```

#### Remove polyline
Remove polyline on `PlacesPointPolyline`
```kotlin
val isRemoveSuccess = nextPoint.remove()
```

Or by geometries
```kotlin
val isRemoveSuccess = point.remove(nextGeometries)
```

#### Configuration DSL
|Param|return|desc|
|---|---|---|
|`withPrimaryPolyline(PolylineOptions.() -> Unit)`|`Unit`|DSL param for add primary polyline|
|`withAccentPolyline(PolylineOptions.() -> Unit)`|`Unit`|DSL param for add accent polyline|
|`duration`|`Long`|Duration of animation|
|`cameraAutoUpdate`|`Boolean`|Set for auto zoom camera|
|`stackAnimationMode`|`StackAnimationMode`|Set for animate type polyline|
|`doOnStartAnimation(action: (LatLng) -> Unit)`|`Unit`|Do action when animation is start|
|`doOnEndAnimation(action: (LatLng) -> Unit)`|`Unit`|Do action when animation is finish|
|`doOnUpdateAnimation(action: (latLng: LatLng, mapCameraDuration: Int) -> Unit)`|`Unit`|Do action when animation is update with duration for camera movement|

### Sample
```kotlin

val polylineBuilder = googleMap.createPlacesPolylineBuilder()
 
// default configuration
polylineBuilder
            .withStackAnimationMode(StackAnimationMode.OffStackAnimation)
            .withCameraAutoFocus(true)

val polyline = polylineBuilder.createAnimatePolyline()

val start = polyline.startAnimate(startGeometries) {
        duration = 10000
        cameraAutoUpdate = true
        withAccentPolyline {
            width(8f)
            color(Color.CYAN)
        }
        withPrimaryPolyline {
            width(8f)
            color(Color.BLUE)
        }
        doOnStartAnimation { latLng ->
            googleMap.addMarker {
                this.position(latLng)
            }
        }
    }

val middle = start.addPoints(middleGeometries) {
        stackAnimationMode = StackAnimationMode.BlockStackAnimation
        duration = 4000
        doOnUpdateAnimation { latLng, mapCameraDuration ->
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17f)
            googleMap.animateCamera(cameraUpdate, mapCameraDuration, null)
        }
    }

val end = middle.addPoints(endGeometries) {
        stackAnimationMode = StackAnimationMode.WaitStackEndAnimation
        duration = 2000
        doOnEndAnimation { latLng ->
            googleMap.addMarker {
                this.position(latLng)
            }
        }
    }
```
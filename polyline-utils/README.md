
# Polyline utils

## Download
```groovy
implementation 'com.utsman.geolib:polyline:1.0.0'
```

## Create `PlacesPolyline`
```kotlin
val polylineBuilder = googleMap.createPlacesPolylineBuilder()
val polyline = polylineBuilder.createAnimatePolyline()
```

### Configuration builder
|Param|type|desc|
|---|---|---|
|`withPrimaryPolyline(PolylineOptions.() -> Unit)`|`PlacesPolylineBuilder`|DSL param for add primary polyline|
|`withAccentPolyline(PolylineOptions.() -> Unit)`|`PlacesPolylineBuilder`|DSL param for add accent polyline|
|`withCameraAutoFocus(Boolean)`|`PlacesPolylineBuilder`|Set for auto zoom camera|
|`withStackAnimationMode(StackAnimationMode)`|`PlacesPolylineBuilder`|Set for animate type polyline|
|`createAnimatePolyline()`|`PlacesPolyline`|to create `PlacesPolyline`|

### Stack Animation Mode
This is animation type of polyline

![](/images/polyline_animate.gif)

- Button 1: `StackAnimationMode.BlockStackAnimation` (this default configuration)
- Button 2: `StackAnimationMode.WaitStackEndAnimation`
- Button 2: `StackAnimationMode.OffStackAnimation`

## Animating polyline
### Start animate polyline
To start animate use `startAnimate(geometries: List<LatLng>, duration: Long)` and result is `PlacesPointPolyline`
```kotlin
val point = polyline.startAnimate(geometries, 2000)
```

### Add points in polyline
You can added polyline in existing polyline
```kotlin
val nextPoint = point.addPoints(nextGeometries)
```

### Remove polyline
Remove polyline on `PlacesPointPolyline`
```kotlin
val isRemoveSuccess = nextPoint.remove()
```

Or by geometries
```kotlin
val isRemoveSuccess = point.remove(nextGeometries)
```

### Configuration DSL
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

## Sample
```kotlin

val polylineBuilder = googleMap.createPlacesPolylineBuilder()
 
// configuration for default polyline
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
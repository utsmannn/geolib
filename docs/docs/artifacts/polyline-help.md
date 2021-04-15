---
title: Polyline
---
You can animating the polyline with easier code. There is a feature to determine the draw of the animation.

## Download
![](https://jitpack.io/v/utsmannn/geolib.svg)
```jsx
implementation 'com.github.utsmannn.geolib:polyline:{last_version}'
```
---

## Create `PlacesPolyline`
```jsx
val polylineBuilder = googleMap.createPlacesPolylineBuilder()
val polylineAnimator = polylineBuilder.createAnimatePolyline()
```

### Configuration builder
|Param|type|desc|
|---|---|---|
|`withPrimaryPolyline(PolylineOptions.() -> Unit)`|`PolylineAnimatorBuilder`|DSL param for add primary polyline|
|`withAccentPolyline(PolylineOptions.() -> Unit)`|`PolylineAnimatorBuilder`|DSL param for add accent polyline|
|`withCameraAutoFocus(Boolean)`|`PolylineAnimatorBuilder`|Set for auto zoom camera|
|`withStackAnimationMode(StackAnimationMode)`|`PolylineAnimatorBuilder`|Set for animate type polyline|
|`createAnimatePolyline()`|`PolylineAnimator`|to create `PolylineAnimator`|

---

## Animating polyline
### Start animate polyline
To start animate use `startAnimate(geometries: List<LatLng>, duration: Long)` and result is `PointPolyline`
```jsx
val point = polylineAnimator.startAnimate(geometries, 2000)
```

### Start with existing Polyline
With `PolylineOptions`
```jsx
val polyline = googleMap.addPolyline(polylineOptions)

polyline.withAnimate(googleMap, polylineOptions) {
        stackAnimationMode = StackAnimationMode.BlockStackAnimation
    }
```

Or with `PolylineAnimator`
```jsx
val polylineAnimator = polylineBuilder.createAnimatePolyline()

val polyline = googleMap.addPolyline(options)
polyline.withAnimate(polylineAnimator) {
        polylineDrawMode = PolylineDrawMode.Normal
    }
```

### Add points in polyline
You can added polyline in existing polyline
```jsx
val nextPoint = point.addPoints(nextGeometries)
```

### Remove polyline
Remove polyline on `PointPolyline`
```jsx
val isRemoveSuccess = nextPoint.remove()
```

Or by geometries
```jsx
val isRemoveSuccess = point.remove(nextGeometries)
```

### Configuration with DSL
|Param|return|desc|
|---|---|---|
|`withPrimaryPolyline(PolylineOptions.() -> Unit)`|`Unit`|DSL param for add primary polyline|
|`withAccentPolyline(PolylineOptions.() -> Unit)`|`Unit`|DSL param for add accent polyline|
|`duration`|`Long`|Duration of animation|
|`cameraAutoUpdate`|`Boolean`|Set for auto zoom camera|
|`stackAnimationMode`|`StackAnimationMode`|Set for animate type polyline|
|`polylineDrawMode`|`PolylineDrawMode`|Set for draw type polyline|
|`enableBorder(isEnable: Boolean, color: Int, width: Int)`|`Unit`|Set for border of polyline|
|`doOnStartAnimation(action: (LatLng) -> Unit)`|`Unit`|Do action when animation is start|
|`doOnEndAnimation(action: (LatLng) -> Unit)`|`Unit`|Do action when animation is finish|
|`doOnUpdateAnimation(action: (latLng: LatLng, mapCameraDuration: Int) -> Unit)`|`Unit`|Do action when animation is update with duration for camera movement|

### Stack Animation Mode
This is animation type of polyline

![](/img/polyline/polyline_animate.gif)

- Button 1: `StackAnimationMode.BlockStackAnimation` (this default configuration)
- Button 2: `StackAnimationMode.WaitStackEndAnimation`
- Button 2: `StackAnimationMode.OffStackAnimation`

### Polyline draw mode
This library able to customize polyline draw mode

![](/img/polyline/draw_polyline.gif)

- Button 1: `PolylineDrawMode.Normal` (this default configuration)
- Button 2: `PolylineDrawMode.Curved`
- Button 2: `PolylineDrawMode.Lank`

---
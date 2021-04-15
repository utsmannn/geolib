---
title: Marker
---
Parse layout to original marker or you can create any view as marker. I added the layer for create any view as marker instead original marker.

## Download
![](https://jitpack.io/v/utsmannn/geolib.svg)
```jsx
implementation 'com.github.utsmannn.geolib:marker:{last_version}'
```

---
## Bitmap Marker
This marker is original from Google Maps SDK. But, you can create bitmap marker from xml layout.

### Create marker adapter
Extend your adapter with `MarkerBitmapAdapter()` and implement methods.

- `createView()` (suspend)
- `maxWidth()`
- `maxHeight()`

```jsx
class MarkerAdapter(private val context: Context) : MarkerBitmapAdapter() {
    override suspend fun createView(): View {
        return LayoutInflater.from(context).inflate(R.layout.marker_cat, null)
    }

    override fun maxWidth(): Int {
        return 50.dp
    }

    override fun maxHeight(): Int {
        return 70.dp
    }
}
```

`CreateView()` is setting up your view, inflate the xml to view and return to it. This function is suspend, so support the asynchronus function like load image from url.

`maxWidth()` is max width for your marker in pixel.

`maxHeight()` is max height for your marker in pixel.

### Implement marker adapter
From the adapter, you can get bitmap icon with `getIconView()`. 

```jsx
val iconBitmap = markerAdapter.getIconView()
val melbourneLocation = LatLng(-37.813, 144.962)
val melbourne = map.addMarker(
    MarkerOptions()
        .position(melbourneLocation)
        .icon(iconBitmap)
)
```
---

## Any View Marker
This feature is enabling posibility create any view (not bitmap) as a marker. But this marker is not original marker from Google Maps. If you create **animate marker** with **gif** file or **Lottie** file, use this feature.

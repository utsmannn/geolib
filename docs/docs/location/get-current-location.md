---
title: Get current location
---
Required `CoroutineScope`

## Observer current location
This function build under `CoroutineScope` with return `Flow`
```jsx
MainScope().launch {
    placesLocation.getLocationFlow()
        .collect { location ->
            // location result
        }
}
```
If you don't need to observer.
```jsx
MainScope().launch {
    val location = placesLocation.getLocationFlow().first()
}
```

## Get comparator location (prev and current)
```jsx
MainScope().launch {
    placesLocation.getComparisonLocation()
        .collect { comparisonLocation ->
            val prevLocation = comparisonLocation.previousLocation
            val currentLocation = comparisonLocation.currentLocation
        }
}
```
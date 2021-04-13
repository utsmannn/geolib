---
title: Download and preparation
---

This feature include ***observer location***, ***get current location***, ***get place data*** and ***search nearby place by location***

## Download
```jsx
implementation 'com.github.utsmannn.geolib:location:{last_version}'
```

## Prerequisite
You need `FusedLocationProviderClient`
```jsx
val fusedLocation = LocationServices.getFusedLocationProviderClient(context)
```

## Create `PlaceLocation`
```jsx
val placesLocation = fusedLocation.createPlacesLocation(HERE_API_KEY)
```
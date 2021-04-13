---
title: Get place data
---
Required `CoroutineScope`

## Get place data from location
```jsx
val places = placesLocation.getPlacesLocation(location)
```

## Search nearby place
Search place is searching nearby place on location by query with data result `List<PlaceData>`
```jsx
val data = placesLocation.searchPlaces(location, query)
```

## Place Data
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
package hu.bme.aut.android.wekerleitervers.locations

import com.google.android.gms.maps.model.LatLng
import kotlin.collections.ArrayList

object AllLocationObject {
    val locationsCoordinates: MutableList<LatLng> = ArrayList()
    val routerAllCoordinates: MutableList<LatLng> = ArrayList()
    val locationBearings: MutableList<Float> = ArrayList()

    init{
        locationsCoordinates.add(LatLng(47.455532,19.124689))
        locationsCoordinates.add(LatLng(47.456709,19.124594))
        locationsCoordinates.add(LatLng(47.457224,19.123304))
        locationsCoordinates.add(LatLng(47.459378,19.126337))
        locationsCoordinates.add(LatLng(47.459503,19.128533))
        locationsCoordinates.add(LatLng(47.458204,19.129968))
        locationsCoordinates.add(LatLng(47.455707,19.129886))
        locationsCoordinates.add(LatLng(47.455624,19.127805))
        locationsCoordinates.add(LatLng(47.455499,19.126123))
        locationsCoordinates.add(LatLng(47.455644,19.124962))

        routerAllCoordinates.add(locationsCoordinates[0])
        routerAllCoordinates.add(LatLng(47.455543, 19.124815))
        routerAllCoordinates.add(LatLng(47.456393, 19.124821))
        routerAllCoordinates.add(LatLng(47.456473,19.124949))
        routerAllCoordinates.add(locationsCoordinates[1])
        routerAllCoordinates.add(LatLng(47.457165, 19.123911))
        routerAllCoordinates.add(locationsCoordinates[2])
        routerAllCoordinates.add(LatLng(47.459245,19.126232))
        routerAllCoordinates.add(locationsCoordinates[3])
        routerAllCoordinates.add(LatLng(47.459537,19.126578))
        routerAllCoordinates.add(LatLng(47.459465,19.128254))
        routerAllCoordinates.add(locationsCoordinates[4])
        routerAllCoordinates.add(LatLng(47.459335, 19.128625))
        routerAllCoordinates.add(LatLng(47.458498, 19.129834))
        routerAllCoordinates.add(locationsCoordinates[5])
        routerAllCoordinates.add(LatLng(47.458112, 19.130040))
        routerAllCoordinates.add(locationsCoordinates[6])
        routerAllCoordinates.add(locationsCoordinates[7])
        routerAllCoordinates.add(locationsCoordinates[8])
        routerAllCoordinates.add(locationsCoordinates[9])

        locationBearings.add(0f)
        locationBearings.add(314.6f)
        locationBearings.add(45.96f)
        locationBearings.add(79.36f)
        locationBearings.add(144.26f)
        locationBearings.add(181.22f)
        locationBearings.add(268.3f)
        locationBearings.add(264.83f)
        locationBearings.add(280.61f)
        locationBearings.add(0f)
    }

}
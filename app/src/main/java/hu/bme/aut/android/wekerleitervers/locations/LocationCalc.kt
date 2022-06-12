package hu.bme.aut.android.wekerleitervers.locations

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import kotlin.math.*

class LocationCalc(
    private var activity: Activity,
    private var map: GoogleMap,
    private var client: FusedLocationProviderClient,
    private var zoomLocation: LatLng,
    private val hasStartState: Boolean
) : AppCompatActivity() {
    /*******MAX DISTANCE FROM STATES**********/
    private val maxDistance = 0.01 /**in km***/

    var myPosition: LatLng? = null
    private var listener: OnLocationEnable

    interface OnLocationEnable {
        fun onLocationEnabled()
    }

    init {
        try {
            listener = activity as OnLocationEnable
        } catch (e: ClassCastException) {
            throw RuntimeException(e)
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1
    }

    fun setMyLocation(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_CODE
            )
            return false
        }
        map.isMyLocationEnabled = true
        client.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                myPosition = LatLng(location.latitude, location.longitude)
                if (!hasStartState) {
                    val center = midpoint(zoomLocation, myPosition!!)
                    val zoomTo: Float = when (calculationByDistance(myPosition!!, zoomLocation)) {
                        in 0.0..1.0 -> 15f
                        in 1.0..2.0 -> 14.8f
                        in 2.0..5.0 -> 12.8f
                        in 5.0..10.0 -> 12.0f
                        else -> 6.0f
                    }
                    map.moveCamera(CameraUpdateFactory.newLatLng(center))
                    map.moveCamera(CameraUpdateFactory.zoomTo(zoomTo))
                }
                listener.onLocationEnabled()
            }
        }
        return true
    }

    private fun calculationByDistance(StartP: LatLng, EndP: LatLng): Double {
        val radius = 6371 // radius of earth in Km
        val lat1 = StartP.latitude
        val lat2 = EndP.latitude
        val lon1 = StartP.longitude
        val lon2 = EndP.longitude
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = (sin(dLat / 2) * sin(dLat / 2)
                + (cos(Math.toRadians(lat1))
                * cos(Math.toRadians(lat2)) * sin(dLon / 2)
                * sin(dLon / 2)))
        val c = 2 * asin(sqrt(a))
        val valueResult = radius * c
        return valueResult / 1
    }

    private fun midpoint(loc1: LatLng, loc2: LatLng): LatLng {
        var lat1 = loc1.latitude
        var lon1 = loc1.longitude
        var lat2 = loc2.latitude
        val lon2 = loc2.longitude
        val dLon = Math.toRadians(lon2 - lon1)

        lat1 = Math.toRadians(lat1)
        lat2 = Math.toRadians(lat2)
        lon1 = Math.toRadians(lon1)

        val bx = cos(lat2) * cos(dLon)
        val by = cos(lat2) * sin(dLon)
        var lat3 = atan2(
            sin(lat1) + sin(lat2),
            sqrt((cos(lat1) + bx) * (cos(lat1) + bx) + by * by)
        )
        lat3 = Math.toDegrees(lat3)

        var lon3: Double = lon1 + atan2(by, cos(lat1) + bx)
        lon3 = Math.toDegrees(lon3)

        return LatLng(lat3, lon3)
    }

    fun ifNearState(state: LatLng): Boolean {
        val distance: Double = calculationByDistance(state, myPosition!!)
        if (distance <= maxDistance) {
            return true
        }
        return false
    }

    fun updateLocation() {
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        client.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                myPosition = LatLng(location.latitude, location.longitude)
            }
        }
    }
}
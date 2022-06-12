package hu.bme.aut.android.wekerleitervers.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.gms.location.LocationServices
import hu.bme.aut.android.wekerleitervers.R
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import hu.bme.aut.android.wekerleitervers.databinding.ActivityMapsBinding
import hu.bme.aut.android.wekerleitervers.locations.AllLocationObject
import hu.bme.aut.android.wekerleitervers.locations.LocationCalc
import hu.bme.aut.android.wekerleitervers.navview.NavView

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
    GoogleMap.OnInfoWindowClickListener, LocationCalc.OnLocationEnable {
    private lateinit var navigation: NavView
    private var isLocationEnabled = false
    private var isMainHandlerStarted = false
    private lateinit var map: GoogleMap
    private lateinit var firstLocation: LatLng
    private lateinit var binding: ActivityMapsBinding
    private lateinit var drawer: DrawerLayout
    private var isMakeDialogArray: Array<Boolean> =
        arrayOf(false, false, false, false, false, false, false, false, false, false)
    private val markers: MutableList<Marker> = ArrayList()
    private lateinit var location: LocationCalc
    private var nearStateID = "0"

    lateinit var handler: Handler
    private val updateLocationTask = object : Runnable {
        override fun run() {
            location.updateLocation()
            ifNearAnyState()
            if (isMainHandlerStarted) {
                handler.postDelayed(this, 2000)
            }
        }
    }

    companion object {
        const val id = "100"
        const val prevNext = "3"
        private const val PERMISSION_REQUEST_CODE = 1
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Set toolbar, navView
        val toolbar = binding.toolbar.toolbar
        setSupportActionBar(toolbar)
        drawer = binding.drawerLayout
        val id = 2
        navigation = NavView(this, drawer, toolbar, id, binding.nawview.navView)
        navigation.create()

        //Set Map
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        handler = Handler(Looper.getMainLooper())

        binding.btnWalk.setOnClickListener {
            var startStateId = intent.getStringExtra(Companion.id)?.toInt()
            if (startStateId != null && startStateId < 99) {
                if (intent.getStringExtra(prevNext) == "0") {
                    startStateId -= 1
                }
                map.animateCamera(
                    CameraUpdateFactory.newCameraPosition(
                        CameraPosition(
                            AllLocationObject.locationsCoordinates[startStateId + 1],
                            18f,
                            60f,
                            AllLocationObject.locationBearings[startStateId + 1]
                        )
                    )
                )
                markers[startStateId + 1].showInfoWindow()
            } else {
                map.animateCamera(
                    CameraUpdateFactory.newCameraPosition(
                        CameraPosition(
                            firstLocation,
                            18f,
                            60f,
                            AllLocationObject.locationBearings[0]
                        )
                    )
                )
                markers[0].showInfoWindow()
            }
        }

        binding.btnOpenState.visibility = View.GONE
        binding.guideline5.setGuidelinePercent(0.85f)
        binding.btnOpenState.setOnClickListener {
            openLocationDetails(nearStateID.toInt() + 1)
        }
    }

    override fun onResume() {
        super.onResume()
        if (isLocationEnabled && !isMainHandlerStarted) {
            handler.post(updateLocationTask)
            isMainHandlerStarted = true
        }
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(updateLocationTask)
        isMainHandlerStarted = false
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen((GravityCompat.START))) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setOnMarkerClickListener(this)
        map.setOnInfoWindowClickListener(this)
        map.uiSettings.isZoomControlsEnabled = true
        firstLocation = AllLocationObject.locationsCoordinates[0]

        var start = false
        val startStateId = intent.getStringExtra(id)?.toInt()
        if (startStateId != null && startStateId < 99)
            start = true

        location = LocationCalc(
            this,
            map,
            LocationServices.getFusedLocationProviderClient(this),
            firstLocation,
            start
        )


        setUpMap()

        if (location.setMyLocation()) {
            ifNearAnyState()
            isLocationEnabled = true
            if (!isMainHandlerStarted) {
                isMainHandlerStarted = true
                handler.post(updateLocationTask)
            }
        }

        startState()
    }

    private fun startState() {
        val startStateId = intent.getStringExtra(id)?.toInt()
        if (startStateId == null || startStateId > 99)
            return

        if (intent.getStringExtra(prevNext) == "0") {
            isMakeDialogArray[startStateId + 1] = true
            map.moveCamera(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition(
                        AllLocationObject.locationsCoordinates[startStateId + 1],
                        18f,
                        60f,
                        AllLocationObject.locationBearings[startStateId] + 180f
                    )
                )
            )
            markers[startStateId].showInfoWindow()
        } else {
            isMakeDialogArray[startStateId] = true
            map.moveCamera(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition(
                        AllLocationObject.locationsCoordinates[startStateId],
                        18f,
                        60f,
                        AllLocationObject.locationBearings[startStateId]
                    )
                )
            )
            markers[startStateId + 1].showInfoWindow()
        }
    }

    private fun setUpMap() {
        val b = BitmapFactory.decodeResource(resources, R.drawable.marker_icon)
        val bitmapIcon = Bitmap.createScaledBitmap(b, 50, 50, false)
        var loc: LatLng
        for (i in 0..9) {
            loc = AllLocationObject.locationsCoordinates[i]
            val marker = map.addMarker(
                MarkerOptions()
                    .position(loc)
                    .title((i + 1).toString() + resources.getString(R.string.dot_loc))
                    .icon(BitmapDescriptorFactory.fromBitmap(bitmapIcon))
            )
            markers.add(marker!!)
        }
        map.addPolyline(
            PolylineOptions()
                .visible(true)
                .addAll(
                    AllLocationObject.routerAllCoordinates
                )
                .color(R.color.route_blue)
                .width(10f)
                .startCap(RoundCap())
                .endCap(RoundCap())
        )
        map.moveCamera(CameraUpdateFactory.newLatLng(firstLocation))
        map.moveCamera(CameraUpdateFactory.zoomTo(15f))
    }

    private fun ifNearAnyState() {
        if (location.myPosition == null)
            return

        for (i in 0..9) {
            if (location.ifNearState(AllLocationObject.locationsCoordinates[i])) {
                nearStateID = i.toString()
                binding.btnOpenState.text = (i + 1).toString()
                binding.btnOpenState.visibility = View.VISIBLE
                binding.guideline5.setGuidelinePercent(0.7f)
                if (!isMakeDialogArray[i]) {
                    isMainHandlerStarted = false
                    handler.removeCallbacks(updateLocationTask)
                    makeAlertDialog(i + 1)
                }
                return
            }
        }
        binding.btnOpenState.visibility = View.GONE
        binding.guideline5.setGuidelinePercent(0.85f)
    }

    private fun makeAlertDialog(id: Int) {
        isMakeDialogArray[id - 1] = true
        AlertDialog.Builder(this)
            .setMessage(resources.getString(R.string.near_state_the) + id + resources.getString(R.string.near_state_10m))
            .setPositiveButton(resources.getString(R.string.btn_yes)) { _, _ ->
                openLocationDetails(
                    id
                )
            }
            .setNegativeButton(resources.getString(R.string.btn_no)) { _, _ ->
                handler.post(updateLocationTask)
                isMainHandlerStarted = true
            }
            .show()
    }

    private fun openLocationDetails(id: Int) {
        val i = (id - 1).toString()
        val showActivityIntent = Intent()
        showActivityIntent.setClass(this, LocationDetailsActivity::class.java)
        showActivityIntent.putExtra(LocationDetailsActivity.id, i)
        startActivity(showActivityIntent)
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        val id = p0.title?.get(0).toString().toInt()
        map.animateCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition(
                    p0.position,
                    18f,
                    60f,
                    AllLocationObject.locationBearings[id - 1]
                )
            )
        )
        p0.showInfoWindow()
        return true
    }

    override fun onInfoWindowClick(p0: Marker) {
        val id = p0.title?.get(0).toString().toInt()
        openLocationDetails(id)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.enable_loc),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    location.setMyLocation()
                } else {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.dis_loc),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun onLocationEnabled() {
        location.updateLocation()
        ifNearAnyState()
    }
}


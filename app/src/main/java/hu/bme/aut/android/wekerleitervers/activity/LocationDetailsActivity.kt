package hu.bme.aut.android.wekerleitervers.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import hu.bme.aut.android.wekerleitervers.R
import hu.bme.aut.android.wekerleitervers.databinding.ActivityLocationDetailsBinding
import hu.bme.aut.android.wekerleitervers.locations.AllLocationObject
import hu.bme.aut.android.wekerleitervers.locations.LocationCalc
import hu.bme.aut.android.wekerleitervers.navview.NavView
import hu.bme.aut.android.wekerleitervers.services.PlaySoundService

class LocationDetailsActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener, LocationCalc.OnLocationEnable {
    private lateinit var binding: ActivityLocationDetailsBinding
    private lateinit var map: GoogleMap
    private lateinit var stateLocation: LatLng
    private var locID: Int? = null
    private lateinit var navigation: NavView
    private lateinit var drawer: DrawerLayout
    private lateinit var location: LocationCalc

    companion object {
        const val id = "1"
        var isPlaying = false
        private const val PERMISSION_REQUEST_CODE = 1
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        locID = intent.getStringExtra(id)?.toInt()
        binding.locDesc.text = resources.getStringArray(R.array.loc_texts)[locID!!]
        binding.locQuoteDetails.text =
            "\"" + resources.getStringArray(R.array.loc_quotes)[locID!!] + "\""


        binding.soundControl.visibility = View.GONE
        binding.stateControl.visibility = View.GONE

        if (locID!! < 9) {
            binding.textNextState.text =
                (locID!! + 2).toString() + resources.getString(R.string.dot_loc)
        } else {
            binding.btnNextState.visibility = View.GONE
            binding.textNextState.visibility = View.GONE
        }

        if (locID!! > 0) {
            binding.textPrevState.text =
                (locID!!).toString() + resources.getString(R.string.dot_loc)
        } else {
            binding.btnPrevState.visibility = View.GONE
            binding.textPrevState.visibility = View.GONE
        }


        val toolbar = binding.toolbar.toolbar
        toolbar.title = (locID!! + 1).toString() + resources.getString(R.string.dot_loc)
        setSupportActionBar(toolbar)
        drawer = binding.drawerLayout
        val id = 4
        navigation = NavView(this, drawer, toolbar, id, binding.nawview.navView)
        navigation.create()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.detail_map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        binding.btnSoundPlay.setOnClickListener {
            playSound()
        }

        binding.btnSoundPause.setOnClickListener {
            pauseSound()
        }

        binding.btnNextState.setOnClickListener {
            val showActivityIntent = Intent()
            showActivityIntent.setClass(this, MapsActivity::class.java)
            showActivityIntent.putExtra(MapsActivity.id, locID.toString())
            startActivity(showActivityIntent)
        }

        binding.btnPrevState.setOnClickListener {
            val showActivityIntent = Intent()
            showActivityIntent.setClass(this, MapsActivity::class.java)
            val putID = locID!! - 1
            showActivityIntent.putExtra(MapsActivity.id, putID.toString())
            showActivityIntent.putExtra(MapsActivity.prevNext, "0")
            startActivity(showActivityIntent)
        }
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen((GravityCompat.START))) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onStop() {
        super.onStop()
        pauseSound()
    }


    override fun onMapReady(p0: GoogleMap) {
        map = p0
        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMyLocationButtonClickListener(this)
        stateLocation = AllLocationObject.locationsCoordinates[locID!!]
        location = LocationCalc(
            this,
            map,
            LocationServices.getFusedLocationProviderClient(this),
            stateLocation,
            false
        )

        if (location.setMyLocation()) {
            enableControls()
        }

        val b = BitmapFactory.decodeResource(resources, R.drawable.marker_icon)
        val bitmapIcon = Bitmap.createScaledBitmap(b, 50, 50, false)
        val id = locID!! + 1
        map.addMarker(
            MarkerOptions()
                .position(stateLocation)
                .title(id.toString() + resources.getString(R.string.dot_loc))
                .icon(BitmapDescriptorFactory.fromBitmap(bitmapIcon))
        )
        map.moveCamera(CameraUpdateFactory.newLatLng(stateLocation))
        map.moveCamera(CameraUpdateFactory.zoomTo(15f))
    }

    private fun playSound() {
        if (isPlaying)
            return

        val serviceIntent = Intent()
        serviceIntent.setClass(this, PlaySoundService::class.java)
        startService(serviceIntent)
        isPlaying = true
    }

    private fun pauseSound() {
        if (!isPlaying)
            return

        val serviceIntent = Intent()
        serviceIntent.setClass(this, PlaySoundService::class.java)
        stopService(serviceIntent)
        isPlaying = false
    }

    private fun enableControls() {
        if (location.myPosition == null) {
            return
        }

        if (location.ifNearState(stateLocation)) {
            binding.guideline3.setGuidelinePercent(0.86f)
            binding.soundControl.visibility = View.VISIBLE
            binding.mapsFragment.root.visibility = View.GONE
            binding.stateControl.visibility = View.VISIBLE
        } else {
            binding.guideline3.setGuidelinePercent(0.6f)
            binding.soundControl.visibility = View.GONE
            binding.stateControl.visibility = View.GONE
            binding.mapsFragment.root.visibility = View.VISIBLE
        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        location.updateLocation()
        enableControls()
        return false
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
                    Toast.makeText(this, resources.getString(R.string.enable_loc), Toast.LENGTH_SHORT)
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
        enableControls()
    }
}
package br.cademeubicho.ui.maps

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import br.cademeubicho.R
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_maps.*
import java.lang.Exception
import java.util.*


class MapsActivity :
    AppCompatActivity(),
    OnMapReadyCallback {

    private var map: GoogleMap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        val mapFragment = SupportMapFragment.newInstance()
        mapFragment.getMapAsync(this)
        supportFragmentManager.beginTransaction()
            .replace(R.id.google_map, mapFragment)
            .commit()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10 * 1000

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
            }
        }

        setUpMap()
    }

    override fun onResume() {
        super.onResume()

        val service: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val enabled: Boolean = service.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (!enabled) {
            val mySnackbar = Snackbar.make(rootView, "GPS destivado", Snackbar.LENGTH_INDEFINITE)
            mySnackbar.setAction(R.string.activate) {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
            mySnackbar.show()

            btNext.visibility = GONE

        } else {
            btNext.visibility = VISIBLE
        }


        startLocationUpdates()

    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null /* Looper */
        )
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun getLocationBy(markLatLng: LatLng, context: Context) {
        val geocoder = Geocoder(context, Locale.getDefault())

        val address: Address
        address = try {
            val locationAddress =
                geocoder.getFromLocation(markLatLng.latitude, markLatLng.longitude, 1)
            locationAddress[0]
        } catch (e: Exception) {
            Address(Locale.ENGLISH)
        }

        return getLocationBy(address)
    }

    private fun getLocationBy(address: Address) {
        var fullAddress = ""
        for (addressLineIndex in IntRange(0, address.maxAddressLineIndex)) {
            fullAddress += address.getAddressLine(addressLineIndex)
        }

        tvAddress.text = fullAddress
        try {
            getLocationByLatLng(address.latitude, address.longitude)
        } catch (e: Exception) {
            getLocationByLatLng(-17.797713, -50.934774)
        }
    }

    private fun getLocationByLatLng(latitude: Double, longitude: Double) {
        btNext.setOnClickListener {
            val returnIntent = Intent()
            returnIntent.putExtra("latitude", latitude)
            returnIntent.putExtra("longitude", longitude)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        if (googleMap != null) {
            map = googleMap
        }

        map?.uiSettings?.isZoomControlsEnabled = false

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                val currentLatLng = LatLng(location.latitude, location.longitude)
                map?.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 18F))
            }
        }

        onResume()

        map?.setOnCameraIdleListener {
            val mPosition = map?.cameraPosition?.target
            mPosition?.let { getLocationBy(it, this) }
        }

    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        map?.isMyLocationEnabled = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}



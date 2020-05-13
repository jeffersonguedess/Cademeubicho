package br.cademeubicho.maps

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
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
                val location = locationResult.lastLocation
                val currentLatLng = LatLng(location.latitude, location.longitude)
                //map?.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 20f))
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val service: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val enabled: Boolean = service.isProviderEnabled(LocationManager.GPS_PROVIDER)

        // check if enabled and if not send user to the GSP settings
        // Better solution would be to display a dialog and suggesting to
        // go to the settings

        // check if enabled and if not send user to the GSP settings
        // Better solution would be to display a dialog and suggesting to
        // go to the settings
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

        if (map != null) {
            setUpMap()

            startLocationUpdates()
        }
    }

    private fun startLocationUpdates() {
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
        val locationAddress = geocoder.getFromLocation(markLatLng.latitude, markLatLng.longitude, 1)

        val address = locationAddress[0]

        return getLocationBy(address)
    }

    private fun getLocationBy(address: Address) {
        var fullAddress = ""
        for (addressLineIndex in IntRange(0, address.maxAddressLineIndex)) {
            fullAddress += address.getAddressLine(addressLineIndex)
        }

        tvAddress.text = fullAddress
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        if (googleMap != null) {
            map = googleMap
        }

        map?.uiSettings?.isZoomControlsEnabled = false

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
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        map?.isMyLocationEnabled = false

    }

}



package br.cademeubicho.maps

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import br.cademeubicho.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.util.*


const val REQUEST_CODE_LOCATION = 123


@Suppress("DEPRECATED_IDENTITY_EQUALS", "DEPRECATION")
class MapsActivity() :
    AppCompatActivity(),
    OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {

    private lateinit var map: GoogleMap
    var position: LatLng? = null
    var myLocation: Location? = null
    val marker = MarkerOptions()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment: SupportMapFragment? =
            supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

    }


    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap ?: return
        enableMyLocation()

        /*   val latlng = LatLng(0.0, 0.0)//local nulo no mei do mar
           val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latlng, 10f)
           map?.moveCamera(cameraUpdate)

             val marker = map.addMarker(
                 MarkerOptions()
                     .position(latlng)
                     .title("Mar")
                     .snippet("Oceano Atlântica Sul")
                     .draggable(true)

             )
           marker.showInfoWindow()
           marker.hideInfoWindow()
           onMarkerClick(marker)
           //Tipos de Maps
           map?.mapType = GoogleMap.MAP_TYPE_SATELLITE*/
        /*    customAddMarker(
                LatLng(position.latitude, position.longitude),
                "Marcador Alterado",
                "O marcador foi reposicionado"
            )*/

        centerMapMyLOcation()

    }

    private fun centerMapMyLOcation() {
        myLocation = map.myLocation
        if (myLocation != null) {
            position =
                myLocation?.latitude?.let { latitude ->
                    myLocation?.longitude?.let { longitude ->
                        LatLng(
                            latitude,
                            longitude
                        )
                    }
                }

            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(position, 10f)
            map.animateCamera(cameraUpdate)

        }
    }

    fun customAddMarker(latLng: LatLng, title: String, snippet: String) {
        val options: MarkerOptions = MarkerOptions()
        options.position(latLng).title(title).snippet(snippet).draggable(true)
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10f)
        map.addMarker(options)

    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val mMarker = WeakHashMap<Marker, String>()

        mMarker[marker] = String()
        map.setOnMarkerClickListener {
            onMarkerClick(marker)
        }
        return false
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)

    }

    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(REQUEST_CODE_LOCATION)
    private fun enableMyLocation() {
        if (hasLocationPermission()) {
            map.isMyLocationEnabled = true
            centerMapMyLOcation()
        } else {
            EasyPermissions.requestPermissions(
                this, getString(R.string.location),
                REQUEST_CODE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION

            )
        }
    }

    private fun hasLocationPermission(): Boolean {
        return EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun updateLocationUI() {
        try {
            if (hasLocationPermission()) {
                map.isMyLocationEnabled = true
                map.uiSettings.isMyLocationButtonEnabled = true
            } else {
                map.isMyLocationEnabled = false
                map.uiSettings.isMyLocationButtonEnabled = false
                hasLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message)
        }
    }

}


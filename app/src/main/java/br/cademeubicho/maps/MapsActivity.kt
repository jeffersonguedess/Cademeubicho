package br.cademeubicho.maps

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import br.cademeubicho.R
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


@Suppress("DEPRECATED_IDENTITY_EQUALS")
class MapsActivity() :
    AppCompatActivity(),
    OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener
{

    private lateinit var map: GoogleMap
    val position = LatLng(0.0, 0.0)

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
       // updateLocationUI()

       val marker =  map.addMarker(MarkerOptions()
            .position(LatLng(0.0,0.0))
            .title("Museum")
            .snippet("National Air and Space Museum"))
        marker.showInfoWindow()
        marker.hideInfoWindow()

        onMarkerClick( marker)
        //map?.animateCamera(CameraUpdateFactory.zoomIn())

        /*Tipos de Maps
        map?.mapType = GoogleMap.MAP_TYPE_SATELLITE*/
}

    override fun onMarkerClick(marker: Marker): Boolean {
        val mMarker = WeakHashMap<Marker, String>()

        mMarker.put(marker, String())
        map.setOnMarkerClickListener {
            onMarkerClick(marker)
        }
        return false
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)

        when (requestCode) {
            REQUEST_CODE_LOCATION -> {

                // Se a solicitação for cancelada, as matrizes de resultados estarão vazias.
                if (grantResults.isNotEmpty()
                    && grantResults[0] === PackageManager.PERMISSION_GRANTED
                ) {
                }
            }
        }
        updateLocationUI()
    }



    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(REQUEST_CODE_LOCATION)
    private fun enableMyLocation() {
        if (hasLocationPermission()) {
            map.isMyLocationEnabled = true
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


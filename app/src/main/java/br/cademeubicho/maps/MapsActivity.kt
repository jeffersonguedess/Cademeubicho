package br.cademeubicho.maps

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build.VERSION_CODES.O
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.cademeubicho.R
import br.cademeubicho.webservice.model.Localidade
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_maps.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

const val REQUEST_CODE_LOCATION = 123


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    /* private lateinit var mMap: GoogleMap

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.fragment_maps)
         // Obtain the SupportMapFragment and get notified when the map is ready to be used.
         val mapFragment =
             supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
         mapFragment!!.getMapAsync(this)
     }

     override fun onMapReady(googleMap: GoogleMap) {
         mMap = googleMap

         // Add a marker in Sydney and move the camera
         val sydney = LatLng(-34.0, 151.0)
         mMap.addMarker(
             MarkerOptions().position(LatLng(-25.443150, -49.238243)).title("Jardim Botânico")
         )
         mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
     }*/
    /*  val Localidade = LatLng(-10.3333333, -53.2)
      val ZOOM_LEVEL = 13f*/

    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_maps)
        val mapFragment: SupportMapFragment? =
            supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap ?: return
        enableMyLocation()
        /*   with(googleMap) {
               addMarker(MarkerOptions().position(Localidade).title("Localização"))
               moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-10.3333333, -53.2), ZOOM_LEVEL))
               //  moveCamera(CameraUpdateFactory.newLatLngZoom(SYDNEY, ZOOM_LEVEL))


           }*/
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

}


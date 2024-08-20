package com.app.apparquitectura

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class PuntoDeRecoleccion : AppCompatActivity() {
    private lateinit var mGoogleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_punto_de_recoleccion)

    }

    fun requestLocationPermission() {
        val permiso = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permiso == PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    1
                )
            }
        } else {
            startLocationUpdates()
        }
    }

    fun handleMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        getLocation()
    }

    fun getLocation() {
        val permiso = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
        if (permiso == PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Aquí puedes mostrar un diálogo o mensaje explicando por qué necesitas el permiso.
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }
        } else {
            // Si ya se tiene el permiso, puedes realizar las operaciones necesarias.
            startLocationUpdates()
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permiso concedido, realizar las operaciones necesarias.
                    startLocationUpdates()
                } else {
                    // Permiso denegado, puedes informar al usuario o tomar medidas adicionales.
                }
            }
        }
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Verificar si mGoogleMap está inicializado antes de usarlo.
            if (::mGoogleMap.isInitialized) {
                mGoogleMap.isMyLocationEnabled = true
                mGoogleMap.uiSettings.isCompassEnabled = true
                mGoogleMap.uiSettings.isMyLocationButtonEnabled = true
                mGoogleMap.uiSettings.isZoomControlsEnabled = true

                val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                val locationListener: LocationListener = object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        val miUbicacion = LatLng(location.latitude, location.longitude)
                        mGoogleMap.addMarker(MarkerOptions().position(miUbicacion).title("Ubicación actual"))
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(miUbicacion))
                        val cameraPosition = CameraPosition.Builder()
                            .target(miUbicacion)
                            .zoom(14f)
                            .bearing(90f)
                            .tilt(45f)
                            .build()
                        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                    }

                    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
                    override fun onProviderEnabled(provider: String) {}
                    override fun onProviderDisabled(provider: String) {}
                }

                try {
                    locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        0,
                        0f,
                        locationListener
                    )
                } catch (e: SecurityException) {
                    // Manejar la excepción, por ejemplo, solicitando permisos nuevamente.
                }
            }
        }
    }

}/*    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mGoogleMap.setMyLocationEnabled(true)
        mGoogleMap.getUiSettings().setCompassEnabled(true)
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true)
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true)
        val locationManager = this.getSystemService(LOCATION_SERVICE) as LocationManager
        val locationListener: LocationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                val miUbicacion = LatLng(location.latitude, location.longitude)
                mGoogleMap.addMarker(MarkerOptions().position(miUbicacion).title("ubicacion actual"))
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(miUbicacion))
                val cameraPosition = CameraPosition.Builder()
                    .target(miUbicacion)
                    .zoom(14f)
                    .bearing(90f)
                    .tilt(45f)
                    .build()
                mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }
        locationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            0,
            0f,
            locationListener
        )
    }
*/



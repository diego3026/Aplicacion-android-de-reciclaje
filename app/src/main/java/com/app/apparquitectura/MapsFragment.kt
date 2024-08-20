package com.app.apparquitectura

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import java.io.IOException
import java.util.Locale

class MapsFragment : Fragment(),OnMapReadyCallback{
    private lateinit var mGoogleMap: GoogleMap
    private lateinit var placesClient: PlacesClient
    private lateinit var textDias: TextView
    private lateinit var textHorario: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_punto_de_recoleccion, container, false)
        Places.initialize(requireContext(), "AIzaSyBU_Mrz66usqcKt1FWIg4dtxJGm0Nqs-bE")
        requestLocationPermission()
        loadMapFragment()

        val editTextSearch = view.findViewById<EditText>(R.id.editTextSearch)
        editTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                // Realizar la búsqueda cuando se presiona "Enter"
                performSearch(editTextSearch.text.toString())
                return@setOnEditorActionListener true
            }
            false
        }

        textDias = view.findViewById(R.id.textDiasRecogida2)
        textHorario = view.findViewById(R.id.textHorario)

        return view
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        startLocationUpdates()

        mGoogleMap.setOnMarkerClickListener { marker ->
            val markerDescription = marker.snippet

            val parts = markerDescription?.split("/")

            if (parts != null) {
                if (parts.size == 2) {
                    val beforeSlash = parts[0].trim() // Eliminar espacios en blanco alrededor de la primera cadena
                    val afterSlash = parts[1].trim()  // Eliminar espacios en blanco alrededor de la segunda cadena

                    textDias.text = beforeSlash
                    textHorario.text = afterSlash
                }
            }
            false
        }

    }

    private fun loadMarker() {
        val iconBitmap = BitmapFactory.decodeResource(resources, R.drawable.marker_transparente)
        val scaledIconBitmap = Bitmap.createScaledBitmap(iconBitmap, 40, 40, false)
        val bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(scaledIconBitmap)

        if (::mGoogleMap.isInitialized) {

            var location = LatLng(11.228648, -74.149165)

            var markerOptions = MarkerOptions()
                .position(location)
                .title("Contenedor")
                .icon(bitmapDescriptor)
                .snippet("Lunes - Miercoles - Viernes/8 am - 10 am")
            mGoogleMap.addMarker(markerOptions)

            location = LatLng(11.228081, -74.150109)
            markerOptions = MarkerOptions()
                .position(location)
                .title("Contenedor")
                .icon(bitmapDescriptor)
                .snippet("Lunes - Miercoles - Viernes/8 am - 10 am")

            mGoogleMap.addMarker(markerOptions)

        }

    }

    private fun setupAutocompleteSearch() {
        val editTextSearch = view?.findViewById<EditText>(R.id.editTextSearch)

        editTextSearch?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                // Realizar la búsqueda cuando se presiona "Enter"
                performSearch(editTextSearch.text.toString())
                return@setOnEditorActionListener true
            }
            false
        }
    }

    private fun performSearch(query: String) {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())

        try {
            val addresses = geocoder.getFromLocationName(query, 1)

            if (addresses != null && addresses.isNotEmpty()) {
                val location = LatLng(addresses[0].latitude, addresses[0].longitude)

                // Mover la cámara a la ubicación encontrada
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(location))
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))

                // Añadir un marcador en la ubicación encontrada
                mGoogleMap.clear()
                mGoogleMap.addMarker(MarkerOptions().position(location).title(query))
            } else {
                // Manejar el caso en que no se encuentren resultados
                Toast.makeText(requireContext(), "No se encontraron resultados para la búsqueda.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            // Manejar excepciones de geocodificación
            Toast.makeText(requireContext(), "Error al buscar la ubicación.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun requestLocationPermission() {
        val permiso = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permiso == PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                // Aquí puedes mostrar un diálogo o mensaje explicando por qué necesitas el permiso.
            } else {
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    1
                )
            }
        } else {
            startLocationUpdates()
        }
    }

    private fun loadMapFragment() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
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
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Verificar si mGoogleMap está inicializado antes de usarlo.
            if (::mGoogleMap.isInitialized) {
                //mGoogleMap.isMyLocationEnabled = true
                mGoogleMap.uiSettings.isCompassEnabled = true
                mGoogleMap.uiSettings.isMyLocationButtonEnabled = true
                mGoogleMap.uiSettings.isZoomControlsEnabled = true

                val locationManager =
                    requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
                val locationListener: LocationListener = object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        if (isAdded) { // Check if the fragment is added to the activity
                            val miUbicacion = LatLng(location.latitude, location.longitude)
                            mGoogleMap.clear()
                            mGoogleMap.addMarker(MarkerOptions().position(miUbicacion).title("Ubicación actual"))
                            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(miUbicacion))
                            val cameraPosition = CameraPosition.Builder()
                                .target(miUbicacion)
                                .zoom(14f)
                                .bearing(90f)
                                .tilt(45f)
                                .build()
                            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                            loadMarker()
                        }
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

}
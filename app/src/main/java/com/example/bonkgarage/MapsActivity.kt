package com.example.bonkgarage



import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task

class MapsActivity : AppCompatActivity(), OnMapReadyCallback{

    private lateinit var myMap: GoogleMap
    private lateinit var currentLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        getLastLocation()

    }

    fun getLastLocation(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            return
        }

        Log.d("test", (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED).toString())

        Log.d("debug", ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION).toString())
        Log.d("debug", ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION).toString())

        // Get Last Location
        var task: Task<Location> = fusedLocationProviderClient.getLastLocation()
        task.addOnSuccessListener { location: Location? ->
            if(location != null) {
                currentLocation = location
            }
            else {
                currentLocation = Location(LocationManager.NETWORK_PROVIDER)

                currentLocation.longitude = 106.78113
                currentLocation.latitude = -6.20201

            }

            var mapFragment:SupportMapFragment = getSupportFragmentManager().findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            }
            else {
                Toast.makeText(this, "Location Permission is Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        myMap = p0

        var myLocation = LatLng(currentLocation.latitude, currentLocation.longitude)
        var cameraPosition = CameraPosition.builder().target(myLocation).zoom(20.0F).tilt(80.0F).build()
        myMap.addMarker(MarkerOptions().position(myLocation).title("PuFF and Poof"))
        myMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        myMap.isBuildingsEnabled = true
    }
}
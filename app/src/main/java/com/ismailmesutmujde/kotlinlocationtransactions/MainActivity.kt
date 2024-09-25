package com.ismailmesutmujde.kotlinlocationtransactions

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.ismailmesutmujde.kotlinlocationtransactions.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var bindingMain : ActivityMainBinding
    private var permissionControl = 0
    private lateinit var flpc: FusedLocationProviderClient
    private lateinit var locationTask: Task<Location>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingMain = ActivityMainBinding.inflate(layoutInflater)
        val view = bindingMain.root
        setContentView(view)

        flpc = LocationServices.getFusedLocationProviderClient(this)

        bindingMain.buttonGetLocation.setOnClickListener {
            permissionControl = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            if (permissionControl != PackageManager.PERMISSION_GRANTED) {
                // if permission is not approved
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
            } else {
                // if permission is approved
                locationTask =  flpc.lastLocation
                getLocationInformation()

            }
        }
    }

    fun getLocationInformation() {
        locationTask.addOnSuccessListener{
            if (it != null) {
                bindingMain.textViewLatitude.text = "Latitude : ${it.latitude}"
                bindingMain.textViewLongitude.text = "Longitude : ${it.longitude}"
            } else {
                bindingMain.textViewLatitude.text = "Latitude : Could not be received."
                bindingMain.textViewLongitude.text = "Longitude : Could not be received."
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == 100) {
            permissionControl = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "Permission granted", Toast.LENGTH_SHORT).show()
                locationTask = flpc.lastLocation
                getLocationInformation()
            } else {
                Toast.makeText(applicationContext, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
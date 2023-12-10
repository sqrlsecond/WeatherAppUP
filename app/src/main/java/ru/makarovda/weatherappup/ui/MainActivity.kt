package ru.makarovda.weatherappup.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices

import ru.makarovda.weatherappup.R
import ru.makarovda.weatherappup.domain.CityDomain

const val requestPermissionCode = 1
class MainActivity : AppCompatActivity() {

    private val weatherViewModel: WeatherViewModel by viewModels { WeatherViewModel.Factory }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Поворот экрана
        if(savedInstanceState != null) return

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,  Manifest.permission.ACCESS_COARSE_LOCATION), requestPermissionCode)
            return
        }
        setLocation()
    }

    fun cityChose(city: CityDomain) {
        weatherViewModel.asyncRequestWeather(city.lat.toDouble(), city.lon.toDouble())
    }

    private fun setLocation(){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        weatherViewModel.setCoordinates(location)
                    }
                }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == requestPermissionCode){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setLocation()
            }
        }
    }
}
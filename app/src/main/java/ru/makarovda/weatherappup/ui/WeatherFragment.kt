package ru.makarovda.weatherappup.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.collect
import ru.makarovda.weatherappup.R

class WeatherFragment() : Fragment() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val weatherViewModel: WeatherViewModel by viewModels{ WeatherViewModel.Factory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val temperatureTextView = view.findViewById<TextView>(R.id.temperature_textView)

        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                if(location != null) {
                    weatherViewModel.asyncRequestWeather( String.format("%f,%f", location.latitude, location.longitude))
                }
                //location.latitude
                //location.longitude
            }
        lifecycleScope.launchWhenResumed {
            temperatureTextView.text = "KOAW"
            weatherViewModel.weatherResponseFlow.collect{
                if (it is RequestState.WeatherSuccess) {
                    temperatureTextView.text = it.response.current.temp_c.toString()
                }
            }
        }


    }
}
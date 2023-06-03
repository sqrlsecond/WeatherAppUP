package ru.makarovda.weatherappup.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.flow.collect
import ru.makarovda.weatherappup.R

class WeatherFragment() : Fragment() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var cityChosen = false

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

        val cityNameTextView  = view.findViewById<TextView>(R.id.city_textView)
        val temperatureTextView = view.findViewById<TextView>(R.id.temperature_textView)
        val feelsLikeTemperatureTextView = view.findViewById<TextView>(R.id.feelslike_textView)
        val windSpeedTextView = view.findViewById<TextView>(R.id.windSpeed_textView)
        val humidityTemperatureTextView = view.findViewById<TextView>(R.id.humidity_textView)
        val weatherIcon = view.findViewById<ImageView>(R.id.weatherIcon_imageView)

        val chosenIcon = view.findViewById<ImageView>(R.id.choosen_imageView)
        chosenIcon.setOnClickListener {
            cityChosen = !cityChosen
            if (cityChosen) {
                (it as ImageView).setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.baseline_star_rate_24))
            } else {
                (it as ImageView).setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.baseline_star_border_24))
            }
        }

        view.findViewById<Button>(R.id.findCity_button).setOnClickListener {
            val findCityFragment = FindCityFragment()
            findCityFragment.show(parentFragmentManager,null)
        }

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
            weatherViewModel.weatherResponseFlow.collect{
                if (it is RequestState.WeatherSuccess) {
                    cityNameTextView.text = getString(R.string.cityName_text, it.response.location.name)
                    temperatureTextView.text = getString(R.string.temperature_text, it.response.current.temp_c)
                    feelsLikeTemperatureTextView.text = getString(R.string.feelslike_text, it.response.current.feelslike_c)
                    windSpeedTextView.text = getString(R.string.windSpeed_text, it.response.current.wind_kph)
                    humidityTemperatureTextView.text = getString(R.string.humidity_text, it.response.current.humidity)
                    if(it.response.current.condition.code in 1001..1065) {
                        weatherIcon.setImageDrawable (ContextCompat.getDrawable(requireActivity(), R.drawable.baseline_wb_cloudy_24))
                    } else if (it.response.current.condition.code > 1180) {
                        weatherIcon.setImageDrawable (ContextCompat.getDrawable(requireActivity(), R.drawable.rain_icon))
                    }
                }
            }
        }
    }


}
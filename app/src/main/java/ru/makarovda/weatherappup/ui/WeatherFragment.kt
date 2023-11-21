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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.findFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.makarovda.weatherappup.R

class WeatherFragment() : Fragment() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var cityChosen = false

    // Общая с MainActivity
    private val weatherViewModel: WeatherViewModel by activityViewModels { WeatherViewModel.Factory }

    private var longitude: Float = 0.0f

    private var latitude: Float = 0.0f

    // Общая с ChosenCityFragment
    private val chosenCityViewModel: ChosenCityViewModel by activityViewModels { ChosenCityViewModel.Factory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cityNameTextView = view.findViewById<TextView>(R.id.city_textView)
        val temperatureTextView = view.findViewById<TextView>(R.id.temperature_textView)
        val feelsLikeTemperatureTextView = view.findViewById<TextView>(R.id.feelslike_textView)
        val windSpeedTextView = view.findViewById<TextView>(R.id.windSpeed_textView)
        val humidityTemperatureTextView = view.findViewById<TextView>(R.id.humidity_textView)
        val weatherIcon = view.findViewById<ImageView>(R.id.weatherIcon_imageView)

        val chosenIcon = view.findViewById<ImageView>(R.id.choosen_imageView)
        chosenIcon.setOnClickListener {
            cityChosen = !cityChosen
            if (cityChosen) {
                (it as ImageView).setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.baseline_star_rate_24
                    )
                )
                weatherViewModel.asyncAddChosenCity((weatherViewModel.weatherResponseFlow.value as RequestState.WeatherSuccess).response.city)
            } else {
                (it as ImageView).setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.baseline_star_border_24
                    )
                )
                weatherViewModel.asyncDeleteChosenCity((weatherViewModel.weatherResponseFlow.value as RequestState.WeatherSuccess).response.city)
            }
        }

        view.findViewById<Button>(R.id.findCity_button).setOnClickListener {
            val findCityFragment = FindCityFragment()
            findCityFragment.show(parentFragmentManager, null)
        }

        view.findViewById<Button>(R.id.showChosenCities_button).setOnClickListener {
            val chosenCitiesFragment = ChosenCitiesFragment()
            chosenCitiesFragment.show(parentFragmentManager, null)
        }


        viewLifecycleOwner.lifecycleScope.launch {
            // Отображение погоды
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                weatherViewModel.weatherResponseFlow.collect {
                    if (it is RequestState.WeatherSuccess) {
                        cityNameTextView.text =
                            getString(R.string.cityName_text, it.response.city.name)
                        temperatureTextView.text =
                            getString(R.string.temperature_text, it.response.temp_c)
                        feelsLikeTemperatureTextView.text =
                            getString(R.string.feelslike_text, it.response.feelslike_c)
                        windSpeedTextView.text =
                            getString(R.string.windSpeed_text, it.response.wind_kph)
                        humidityTemperatureTextView.text =
                            getString(R.string.humidity_text, it.response.humidity)
                        if (it.response.condition in 1001..1065) {
                            weatherIcon.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireActivity(),
                                    R.drawable.baseline_wb_cloudy_24
                                )
                            )
                        } else if (it.response.condition > 1180) {
                            weatherIcon.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireActivity(),
                                    R.drawable.rain_icon
                                )
                            )
                        }
                        cityChosen = it.response.city.isChosen
                        if (cityChosen) {
                            chosenIcon.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireActivity(),
                                    R.drawable.baseline_star_rate_24
                                )
                            )
                        }
                    }
                }
            }
            // Отображение города
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                chosenCityViewModel.citiesResponseFlow.collect {
                    if (it is RequestState.ChosenCitiesSuccess) {
                        cityChosen =
                            it.response.contains((weatherViewModel.weatherResponseFlow.value as RequestState.WeatherSuccess).response.city)
                        if (cityChosen) {
                            chosenIcon.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireActivity(),
                                    R.drawable.baseline_star_rate_24
                                )
                            )
                        } else {
                            chosenIcon.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireActivity(),
                                    R.drawable.baseline_star_border_24
                                )
                            )

                        }
                    }
                }
            }
        }

    }
}
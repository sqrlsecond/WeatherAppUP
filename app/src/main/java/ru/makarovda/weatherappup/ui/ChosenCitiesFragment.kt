package ru.makarovda.weatherappup.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.makarovda.weatherappup.R
import ru.makarovda.weatherappup.domain.City

class ChosenCitiesFragment: BottomSheetDialogFragment() {

    val chosenCityViewModel: ChosenCityViewModel by viewModels { ChosenCityViewModel.Factory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chosen_city, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val recView = view.findViewById<RecyclerView>(R.id.chosenCities_recView)

        val adapter = CitiesAdapter(ArrayList<City>())
        recView.adapter = adapter
        recView.layoutManager = LinearLayoutManager(requireActivity())

        lifecycleScope.launchWhenResumed {
            chosenCityViewModel.citiesResponseFlow.collect {
                if (it is RequestState.ChosenCitiesSuccess) {
                    adapter.cities = it.response
                    adapter.notifyDataSetChanged()
                }
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        chosenCityViewModel.asyncGetChosenCity()
    }
}
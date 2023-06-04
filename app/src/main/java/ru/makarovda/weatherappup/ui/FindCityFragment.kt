package ru.makarovda.weatherappup.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.makarovda.weatherappup.R
import ru.makarovda.weatherappup.data.City
import ru.makarovda.weatherappup.domain.CityDomain

class FindCityFragment: BottomSheetDialogFragment() {

    val findCityVM: FindCityViewModel by viewModels{ FindCityViewModel.Factory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_find_city, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val nameInput = view.findViewById<EditText>(R.id.cityName_editText)
        val cityFindButton = view.findViewById<Button>(R.id.findCityDialog_button)

        cityFindButton.setOnClickListener {
            val name = nameInput.text.toString()
            if(name.isNotBlank()){
                findCityVM.asyncFindCity(name)
            }
        }

        val adapter = CitiesFindAdapter(ArrayList<CityDomain>(), findCityVM::addChosenCity)

        val recView = view.findViewById<RecyclerView>(R.id.cities_recView)
        recView.adapter = adapter
        recView.layoutManager = LinearLayoutManager(requireActivity())

        lifecycleScope.launchWhenResumed {
            findCityVM.citiesResponseFlow.collect {
                if (it is RequestState.FindCitiesSuccess) {
                    adapter.cities = it.response
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }
}
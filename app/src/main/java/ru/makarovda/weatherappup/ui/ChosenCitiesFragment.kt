package ru.makarovda.weatherappup.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch
import ru.makarovda.weatherappup.R
import ru.makarovda.weatherappup.domain.CityDomain

class ChosenCitiesFragment: BottomSheetDialogFragment() {

    private val chosenCityViewModel: ChosenCityViewModel by activityViewModels { ChosenCityViewModel.Factory }

    private var choseCityHandler: (CityDomain) -> Unit = {  }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chosen_city, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is MainActivity) {
            choseCityHandler = context::cityChose
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recView = view.findViewById<RecyclerView>(R.id.chosenCities_recView)

        val adapter = CitiesChosenAdapter(ArrayList<CityDomain>(), chosenCityViewModel::removeChosenCity) {
            choseCityHandler(it)
            dismiss()
        }
        recView.adapter = adapter
        recView.layoutManager = LinearLayoutManager(requireActivity())

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                chosenCityViewModel.citiesResponseFlow.collect {
                    val diffRes = DiffUtil.calculateDiff(
                        CitiesDiffUtil(adapter.cities, it)
                    )
                    adapter.cities = it
                    diffRes.dispatchUpdatesTo(adapter)
                    //adapter.notifyDataSetChanged()
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        chosenCityViewModel.asyncGetChosenCity()
    }
}
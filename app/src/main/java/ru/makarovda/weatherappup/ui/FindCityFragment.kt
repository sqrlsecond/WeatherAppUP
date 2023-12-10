package ru.makarovda.weatherappup.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.DiffResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch
import ru.makarovda.weatherappup.R
import ru.makarovda.weatherappup.domain.CityDomain
import ru.makarovda.weatherappup.domain.RequestState

class FindCityFragment: BottomSheetDialogFragment() {

    val findCityVM: FindCityViewModel by viewModels{ FindCityViewModel.Factory }

    private var choseCityHandler: (CityDomain) -> Unit = {  }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_find_city, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is MainActivity) {
            choseCityHandler = context::cityChose
        }
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

        val adapter = CitiesFindAdapter(ArrayList<CityDomain>(), findCityVM::addChosenCity){
            choseCityHandler(it)
            dismiss()
        }

        val recView = view.findViewById<RecyclerView>(R.id.cities_recView)
        recView.adapter = adapter
        recView.layoutManager = LinearLayoutManager(requireActivity())

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED){
                findCityVM.citiesResponseFlow.collect {
                    if (it is RequestState.CitiesSuccess) {
                        val diffUtilRes = DiffUtil.calculateDiff(
                            CitiesDiffUtil(adapter.cities, it.response)
                        )
                        adapter.cities = it.response
                        diffUtilRes.dispatchUpdatesTo(adapter)
                    }
                }
            }
        }
    }
}
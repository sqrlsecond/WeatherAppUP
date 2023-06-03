package ru.makarovda.weatherappup.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.makarovda.weatherappup.R
import ru.makarovda.weatherappup.domain.City

class CitiesAdapter(var cities: List<City>): RecyclerView.Adapter<CitiesAdapter.CitiesViewHolder>() {


    class CitiesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val cityNameTextView: TextView

        val countryTextView: TextView

        init{
            cityNameTextView = itemView.findViewById(R.id.cityName_textView)
            countryTextView = itemView.findViewById(R.id.country_textView)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesViewHolder {
        return CitiesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_city, parent, false))
    }

    override fun getItemCount(): Int = cities.size

    override fun onBindViewHolder(holder: CitiesViewHolder, position: Int) {
        holder.cityNameTextView.text = cities[position].name
        holder.countryTextView.text = cities[position].country
    }
}
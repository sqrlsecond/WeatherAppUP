package ru.makarovda.weatherappup.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.makarovda.weatherappup.R
import ru.makarovda.weatherappup.domain.CityDomain

class CitiesChosenAdapter(var cities: List<CityDomain>,
                          val iconClickListener: (CityDomain) -> Unit,
                          val itemClickListener: (CityDomain) -> Unit
                        ): RecyclerView.Adapter<CitiesChosenAdapter.CitiesViewHolder>() {

    class CitiesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val cityNameTextView: TextView

        val countryTextView: TextView

        val deleteIcon: ImageView

        init{
            cityNameTextView = itemView.findViewById(R.id.cityName_textView)
            countryTextView = itemView.findViewById(R.id.country_textView)
            deleteIcon = itemView.findViewById(R.id.deleteCityIcon_imageView)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesViewHolder {
        return CitiesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_chosen_city, parent, false))
    }

    override fun getItemCount(): Int = cities.size

    override fun onBindViewHolder(holder: CitiesViewHolder, position: Int) {
        holder.cityNameTextView.text = cities[position].name
        holder.countryTextView.text = cities[position].country
        holder.deleteIcon.setOnClickListener {
            iconClickListener(cities[position])
        }
        holder.itemView.setOnClickListener {
            itemClickListener(cities[position])
        }
    }
}
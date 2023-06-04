package ru.makarovda.weatherappup.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.makarovda.weatherappup.R
import ru.makarovda.weatherappup.domain.CityDomain

class CitiesFindAdapter(var cities: List<CityDomain>,
                        val iconClickListener: (CityDomain) -> Unit
                        ): RecyclerView.Adapter<CitiesFindAdapter.CitiesViewHolder>() {


    class CitiesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val cityNameTextView: TextView

        val countryTextView: TextView

        val chosenIcon: ImageView

        var chosenState = false

        init{
            cityNameTextView = itemView.findViewById(R.id.cityName_textView)
            countryTextView = itemView.findViewById(R.id.country_textView)
            chosenIcon = itemView.findViewById(R.id.findCityChosenIcon_imageView)
        }

        fun setState(state: Boolean) {
            if(!state) return

            chosenState = true
            chosenIcon.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.baseline_star_rate_24))

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesViewHolder {
        return CitiesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_find_city, parent, false))
    }

    override fun getItemCount(): Int = cities.size

    override fun onBindViewHolder(holder: CitiesViewHolder, position: Int) {
        holder.cityNameTextView.text = cities[position].name
        holder.countryTextView.text = cities[position].country
        holder.setState(cities[position].isChosen)
        holder.chosenIcon.setOnClickListener {
            if(holder.chosenState) return@setOnClickListener
            holder.chosenState = true
            (it as ImageView).setImageDrawable(ContextCompat.getDrawable(it.context, R.drawable.baseline_star_rate_24))
            iconClickListener(cities[position])
        }
    }
}
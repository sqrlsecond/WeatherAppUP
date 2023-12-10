package ru.makarovda.weatherappup.ui

import androidx.recyclerview.widget.DiffUtil
import ru.makarovda.weatherappup.domain.CityDomain

class CitiesDiffUtil(private var oldList: List<CityDomain>, private val newList: List<CityDomain>): DiffUtil.Callback()
{

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]

}
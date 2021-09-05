package com.example.task1new.screens.countryList

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.data.ext.loadSvg
import com.example.domain.dto.CountryDto
import com.example.domain.dto.convertLanguagesDtoToString
import com.example.task1new.R
import kotlinx.android.extensions.LayoutContainer

class MvvmListAdapter :
    ListAdapter<CountryDto, MvvmListAdapter.ListViewHolder>(DifferItemCallback()) {

    protected var mOnItemClickListener: ((CountryDto) -> Unit?)? = null

    interface OnItemClickListener {
        fun onClick(item: CountryDto)
    }

    class DifferItemCallback : DiffUtil.ItemCallback<CountryDto>() {
        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: CountryDto, newItem: CountryDto): Boolean {
            return oldItem === newItem
        }

        override fun areItemsTheSame(oldItem: CountryDto, newItem: CountryDto): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ListViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        var itemImage: ImageView = containerView.findViewById(R.id.cardIcon)
        var itemName: TextView = containerView.findViewById(R.id.text_name_title)
        var itemTitle: TextView = containerView.findViewById(R.id.text_title)
        var itemDetail: TextView = containerView.findViewById(R.id.text_description)
        var languages: TextView = containerView.findViewById(R.id.text_languages)
        var area: TextView = containerView.findViewById(R.id.text_area)
        var distance: TextView = containerView.findViewById(R.id.text_distance)
        fun bind(item: CountryDto) {
            itemName.text =
                itemName.context.getString(
                    R.string.adapter_name,
                    item.name
                )
            itemTitle.text =
                itemView.context.getString(
                    R.string.adapter_capital,
                    item.capital
                )
            itemDetail.text =
                itemView.context.getString(
                    R.string.adapter_population,
                    item.population
                )
            area.text =
                itemView.context.getString(
                    R.string.adapter_area,
                    item.area
                )

            languages.text =
                itemView.context.getString(
                    R.string.adapter_languages,
                    item.languages.convertLanguagesDtoToString()
                )

            distance.text = itemName.context.getString(
                R.string.adapter_distance,
                item.distance.toString()
            )
            itemImage.loadSvg(item.flag)
            itemView.setOnClickListener {
                mOnItemClickListener?.invoke(item)
            }
        }
    }
}
package com.example.task1new.screens.countryList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.task1new.R
import com.example.task1new.base.adapter.BaseAdapter
import com.example.domain.dto.CountryDto
import com.example.domain.dto.convertLanguagesDtoToString
import com.example.data.ext.loadSvg

class CountryListAdapter : BaseAdapter<CountryDto>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false)
        return CountryViewHolder(v)
    }

    override fun getItemCount(): Int {
        return mDataListInAdapter.size
    }

    fun repopulateAdapterData(newItemsDto: List<CountryDto>) {
        mDataListInAdapter.clear()
        mDataListInAdapter.addAll(newItemsDto)
        notifyDataSetChanged()
    }

    // TODO: Implement notifyItemChanged()

    fun sortAscendingDataListInAdapter() {
        mDataListInAdapter.sortBy { it.population }
        notifyDataSetChanged()
    }

    fun sortDescendingDataListInAdapter() {
        mDataListInAdapter.sortByDescending { it.population }
        notifyDataSetChanged()
    }

    inner class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImage: ImageView = itemView.findViewById(R.id.cardIcon)
        var itemName: TextView = itemView.findViewById(R.id.text_name_title)
        var itemTitle: TextView = itemView.findViewById(R.id.text_title)
        var itemDetail: TextView = itemView.findViewById(R.id.text_description)
        var languages: TextView = itemView.findViewById(R.id.text_languages)
        var area: TextView = itemView.findViewById(R.id.text_area)
        var distance: TextView = itemView.findViewById(R.id.text_distance)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CountryViewHolder) {
            holder.itemName.text =
                holder.itemName.context.getString(
                    R.string.adapter_name,
                    mDataListInAdapter[position].name
                )
            holder.itemTitle.text =
                holder.itemView.context.getString(
                    R.string.adapter_capital,
                    mDataListInAdapter[position].capital
                )
            holder.itemDetail.text =
                holder.itemView.context.getString(
                    R.string.adapter_population,
                    mDataListInAdapter[position].population
                )
            holder.area.text =
                holder.itemView.context.getString(
                    R.string.adapter_area,
                    mDataListInAdapter[position].area
                )

            holder.languages.text =
                holder.itemView.context.getString(
                    R.string.adapter_languages,
                    mDataListInAdapter[position].languages.convertLanguagesDtoToString()
                )

            holder.distance.text = holder.itemName.context.getString(
                R.string.adapter_distance,
                mDataListInAdapter[position].distance.toString()
            )
            holder.itemImage.loadSvg(mDataListInAdapter[position].flag)
            holder.itemView.setOnClickListener { mOnItemClickListener?.invoke(mDataListInAdapter[position]) }
        }
    }
}


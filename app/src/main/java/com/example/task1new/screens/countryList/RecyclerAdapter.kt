package com.example.task1new.screens.countryList

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.task1new.R
import com.example.task1new.base.adapter.BaseAdapter
import com.example.task1new.dto.PostCountryItemDto
import com.example.task1new.dto.convertLanguagesDtoToString
import com.example.task1new.ext.loadSvg

//var dataList: MutableList<PostCountryItemDto>

class RecyclerAdapter : BaseAdapter<PostCountryItemDto>() {

    private var images = R.drawable.icon
    private var currentPosition = 0
    private var mFilteredDataList: MutableList<PostCountryItemDto> = mDataListInAdapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false)
        Log.d(TAG, " ON CREATE VIEW HOLDER STAGE")
        return CountryViewHolder(v)
    }

    fun getDataListFromAdapter(): MutableList<PostCountryItemDto> {
        return mFilteredDataList
    }

    fun getCurrentAdapterPosition(): Int {
        return currentPosition
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "GET ITEM COUNT STAGE")
        return mFilteredDataList.size
    }

    fun addNewUniqueItems(newItemsDto: List<PostCountryItemDto>) {
        val uniqueItems = mutableListOf<PostCountryItemDto>()
        for (newDto in newItemsDto) {
            var itemIsUnique = true
            for (oldDto in mDataListInAdapter) {
                if (newDto.name == oldDto.name) {
                    itemIsUnique = false
                    break
                }
            }
            if (itemIsUnique) {
                uniqueItems.add(newDto)
            }
        }
        mDataListInAdapter.addAll(uniqueItems)
        mFilteredDataList = mDataListInAdapter
        notifyDataSetChanged()
    }

    fun sortAscendingDataListInAdapter() {
        mFilteredDataList.sortBy { it.population }
        notifyDataSetChanged()
    }

    fun sortDescendingDataListInAdapter() {
        mFilteredDataList.sortByDescending { it.population }
        notifyDataSetChanged()
    }

    inner class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImage: ImageView = itemView.findViewById(R.id.cardIcon)
        var itemName: TextView = itemView.findViewById(R.id.text_name_title)
        var itemTitle: TextView = itemView.findViewById(R.id.text_title)
        var itemDetail: TextView = itemView.findViewById(R.id.text_description)
        var languages: TextView = itemView.findViewById(R.id.text_languages)

    }

    fun filterByName(name: String) {
        mFilteredDataList = if (name.isEmpty()) {
            mDataListInAdapter
        } else {
            var filteredList = mutableListOf<PostCountryItemDto>()
            for (country in mFilteredDataList) {
                if (country.name.lowercase().contains(name.lowercase())) {
                    filteredList.add(country)
                }
            }
            filteredList
        }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CountryViewHolder) {
            holder.itemName.text =
                holder.itemName.context.getString(
                    R.string.adapter_name,
                    mFilteredDataList[position].name
                )
            holder.itemTitle.text =
                holder.itemView.context.getString(
                    R.string.adapter_capital,
                    mFilteredDataList[position].capital
                )
            holder.itemDetail.text =
                holder.itemView.context.getString(
                    R.string.adapter_population,
                    mFilteredDataList[position].population
                )
            holder.languages.text =
                holder.itemView.context.getString(
                    R.string.adapter_languages,
                    mFilteredDataList[position].languages.convertLanguagesDtoToString()
                )
            holder.itemImage.loadSvg(mFilteredDataList[position].flag)

            holder.itemView.setOnClickListener { mOnItemClickListener?.invoke(mFilteredDataList[position]) }

            Log.d(TAG, "ON BIND VIEW HOLDER STAGE")
            currentPosition = position
        }
    }
//
}

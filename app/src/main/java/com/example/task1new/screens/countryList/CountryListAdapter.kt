package com.example.task1new.screens.countryList

import android.content.ContentValues.TAG
import android.location.Location
import android.location.LocationManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.SavedStateHandle
import androidx.recyclerview.widget.RecyclerView
import com.example.task1new.R
import com.example.task1new.app.CountriesApp
import com.example.task1new.base.adapter.BaseAdapter
import com.example.task1new.base.filter.CountryDtoListFilterObject
import com.example.task1new.base.filter.CountryDtoListFilterObject.applyFilter
import com.example.task1new.base.mvvm.Outcome
import com.example.task1new.dto.CountryDto
import com.example.task1new.dto.convertLanguagesDtoToString
import com.example.task1new.ext.loadSvg

//var dataList: MutableList<PostCountryItemDto>

class CountryListAdapter : BaseAdapter<CountryDto>() {

//    private var mFilteredDataList: MutableList<CountryDto> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false)
        Log.d(TAG, " ON CREATE VIEW HOLDER STAGE")
        return CountryViewHolder(v)
    }

//    fun getDataListFromAdapter(): MutableList<CountryDto> {
//        return mFilteredDataList
//    }

    fun clearAdapter() {
        mDataListInAdapter.clear()
//        mFilteredDataList.clear()
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "GET ITEM COUNT STAGE")
        return mDataListInAdapter.size
    }

//    fun isFiltered(): Boolean {
//        return mFilteredDataList.size != mDataListInAdapter.size
//    }

//    fun updateFilteredDataList(mDtoList: List<CountryDto>){
//        mFilteredDataList.clear()
//        mFilteredDataList.addAll(mDtoList)
//        notifyDataSetChanged()
//    }


//    fun repopulateFilteredDataListWithFilter(filter: CountryDtoListFilterObject){
//        mFilteredDataList.clear()
//        mFilteredDataList.addAll(mDataListInAdapter.applyFilter(filter))
//        notifyDataSetChanged()
//    }

    fun repopulateAdapterData(newItemsDto: List<CountryDto>) {

        mDataListInAdapter.clear()
        mDataListInAdapter.addAll(newItemsDto)
//        mFilteredDataList.clear()
//        mFilteredDataList.addAll(newItemsDto)
        notifyDataSetChanged()

        // case #1 -> Data loading (initially)
//        if (mDataListInAdapter.isEmpty()) {
//            mDataListInAdapter.addAll(newItemsDto)
//            mFilteredDataList.clear()
//            mFilteredDataList.addAll(mDataListInAdapter)
//            notifyDataSetChanged()
//            Log.e(
//                TAG,
//                " CASE 1 : ADD ALL ITEMS IN DATA LIST. mData.size = ${mDataListInAdapter.size}"
//            )
//        } // case #2 -> no new elements
//        else if (mDataListInAdapter.containsAll(newItemsDto)) {
//            Log.e(
//                TAG,
//                " CASE 2 : NO DATA TO ADD, NO NEW ELEMENTS mData.size = ${mDataListInAdapter.size}"
//            )
//        } // case #3 -> adding new unique elements
//        else {
//            val uniqueItems = mutableListOf<CountryDto>()
//            for (newDto in newItemsDto) {
//                var itemIsUnique = true
//                for (oldDto in mDataListInAdapter) {
//                    if (newDto.name == oldDto.name) {
//                        itemIsUnique = false
//                        break
//                    }
//                }
//                if (itemIsUnique) {
//                    uniqueItems.add(newDto)
//                }
//            }
//            mDataListInAdapter.addAll(uniqueItems)
//            mFilteredDataList.addAll(uniqueItems)
//            if (mFilteredDataList.size == 0) {
//                mFilteredDataList.addAll(mDataListInAdapter)
//            }
//            val initialSize = mDataListInAdapter.size
//            //notifyItemRangeChanged(initialSize - 1, mDataListInAdapter.size - 1)
//            Log.e(TAG, " CASE 3 : ADD NEW UNIQUE ITEMS mData.size = ${mDataListInAdapter.size}")
//        }
    }

    fun sortAscendingDataListInAdapter() {
//        mFilteredDataList.sortBy { it.population }
        mDataListInAdapter.sortBy { it.population }
        notifyDataSetChanged()
    }

    fun sortDescendingDataListInAdapter() {
//        mFilteredDataList.sortByDescending { it.population }
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

//    fun filterByName(name: String) {
//        mFilteredDataList.clear()
//        mFilteredDataList.addAll(mDataListInAdapter)
//        val filteredList = mutableListOf<CountryDto>()
//        for (country in mFilteredDataList) {
//            if (country.name.lowercase().contains(name.lowercase())) {
//                filteredList.add(country)
//            }
//        }
//        mFilteredDataList = filteredList
//        notifyDataSetChanged()
//    }

//    fun repopulateFilteredDataList(data: List<CountryDto>) {
//        mFilteredDataList.clear()
//        mFilteredDataList.addAll(data)
//        notifyDataSetChanged()
//    }

//    fun restoreFilteredListFromDataList() {
//        if (mFilteredDataList.size != mDataListInAdapter.size) {
//            mFilteredDataList.clear()
//            mFilteredDataList.addAll(mDataListInAdapter)
//            notifyDataSetChanged()
//        }
//    }

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



            Log.d(TAG, "ON BIND VIEW HOLDER STAGE")
        }
    }
//
}

//    fun getMinimumArea(): String {
//        var mAreaMin: Double = Double.MAX_VALUE
//        for (country in mDataListInAdapter) {
//            if (country.area < mAreaMin) {
//                mAreaMin = country.area
//            }
//        }
//        return mAreaMin.toString()
//    }
//
//    fun getMaximumArea(): String {
//        var mAreaMax: Double = Double.MIN_VALUE
//        for (country in mDataListInAdapter) {
//            if (country.area > mAreaMax) {
//                mAreaMax = country.area
//            }
//        }
//        return mAreaMax.toString()
//    }

//    fun getMinimumPopulation(): String {
//        var mPopulationMin: Int = Int.MAX_VALUE
//        for (country in mDataListInAdapter) {
//            if (country.population < mPopulationMin) {
//                mPopulationMin = country.population
//            }
//        }
//        return mPopulationMin.toString()
//    }
//
//    fun getMaximumPopulation(): String {
//        var mPopulationMax: Int = Int.MIN_VALUE
//        for (country in mDataListInAdapter) {
//            if (country.population > mPopulationMax) {
//                mPopulationMax = country.population
//            }
//        }
//        return mPopulationMax.toString()
//    }

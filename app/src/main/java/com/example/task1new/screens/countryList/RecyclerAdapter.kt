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

//var dataList: MutableList<PostCountryItemDto>

class RecyclerAdapter : BaseAdapter<PostCountryItemDto>() {

    var images = R.drawable.icon

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false)
        Log.d(TAG, " ON CREATE VIEW HOLDER STAGE")
        return CountryViewHolder(v)
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "GET ITEM COUNT STAGE")
        return mDataListInAdapter.size
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
        notifyDataSetChanged()
    }

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
            holder.languages.text =
                holder.itemView.context.getString(
                    R.string.adapter_languages,
                    mDataListInAdapter[position].languages
                )
            holder.itemImage.setImageResource(images)
            holder.itemView.setOnClickListener { mOnItemClickListener?.invoke(mDataListInAdapter[position]) }

            Log.d(TAG, "ON BIND VIEW HOLDER STAGE")
        }
    }
//
}

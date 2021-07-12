package com.example.task1new

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.task1new.ext.convertToCountryNameList
import com.example.task1new.dto.PostCountryItemDto
import com.example.task1new.ext.addNewItemsToCountryListDto

//var dataList: MutableList<PostCountryItemDto>

class RecyclerAdapter() :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    var dataListInAdapter = mutableListOf<PostCountryItemDto>()
    var images = R.drawable.icon

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false)
        Log.d(TAG, " ON CREATE VIEW HOLDER STAGE")
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "GET ITEM COUNT STAGE")
        return dataListInAdapter.size
    }

    fun addInitialItemsToDataListInAdapter(initialItems: List<PostCountryItemDto>) {
        dataListInAdapter.addAll(initialItems)
        notifyDataSetChanged()
    }

    fun clearDataListInAdapter() {
        dataListInAdapter.clear()
        notifyDataSetChanged()
    }

    fun addNewUniqueItems(newItemsDto: List<PostCountryItemDto>) {
        dataListInAdapter.addNewItemsToCountryListDto(newItemsDto.toMutableList())
        notifyDataSetChanged()
    }

    fun sortAscendingDataListInAdapter() {
        dataListInAdapter.sortBy { it.population }
        notifyDataSetChanged()
    }

    fun sortDescendingDataListInAdapter() {
        dataListInAdapter.sortByDescending { it.population }
        notifyDataSetChanged()
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {

        holder.itemName.text =
            holder.itemName.context.getString(
                R.string.adapter_name,
                dataListInAdapter[position].name
            )
        holder.itemTitle.text =
            holder.itemView.context.getString(
                R.string.adapter_capital,
                dataListInAdapter[position].capital
            )
        holder.itemDetail.text =
            holder.itemView.context.getString(
                R.string.adapter_population,
                dataListInAdapter[position].population
            )
        holder.languages.text =
            holder.itemView.context.getString(
                R.string.adapter_languages,
                dataListInAdapter[position].languages.convertToCountryNameList()
            )
        holder.itemImage.setImageResource(images)

        Log.d(TAG, "ON BIND VIEW HOLDER STAGE")
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImage: ImageView
        var itemName: TextView
        var itemTitle: TextView
        var itemDetail: TextView
        var languages: TextView

        init {
            itemImage = itemView.findViewById(R.id.cardIcon)
            itemName = itemView.findViewById(R.id.text_name_title)
            itemTitle = itemView.findViewById(R.id.text_title)
            itemDetail = itemView.findViewById(R.id.text_description)
            languages = itemView.findViewById(R.id.text_languages)
        }
    }
//
}

//class RecyclerAdapter(var dataList: MutableList<PostCountryItemDto>) :
//    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
//
//    var dataListInAdapter = dataList
//    var images = R.drawable.icon
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false)
//        Log.d(TAG, " ON CREATE VIEW HOLDER STAGE")
//        return ViewHolder(v)
//    }
//
//    override fun getItemCount(): Int {
//        Log.d(TAG, "GET ITEM COUNT STAGE")
//        return dataList.size
//    }
//
//    fun addInitialItemsList(initialItems: List<PostCountryItemDto>){
//        dataListInAdapter.addAll(initialItems)
//    }
//
//    fun addNewUniqueItems(newItemDtos: List<PostCountryItemDto>){
//        for(newItem in newItemDtos){
//            var newItemIsInDataList = false
//            for(oldItem in dataList){
//                if(newItem.name == oldItem.name){
//                    newItemIsInDataList = true
//                    break
//                }
//            }
//            if(!newItemIsInDataList){
//                dataList.add(newItem)
//            }
//        }
//    }
//
//    @SuppressLint("SetTextI18n")
//    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
//        if (dataList[position].capital.isNotEmpty()) {
//            holder.itemTitle.text =
//                holder.itemView.context.getString(
//                    R.string.adapter_capital,
//                    dataList[position].capital
//                )
//            holder.itemDetail.text =
//                holder.itemView.context.getString(
//                    R.string.adapter_population,
//                    dataList[position].population
//                )
//            holder.languages.text =
//                holder.itemView.context.getString(
//                    R.string.adapter_languages,
//                    dataList[position].languages.convertToCountryNameList()
//                )
//            holder.itemImage.setImageResource(images)
//        }
//        Log.d(TAG, "ON BIND VIEW HOLDER STAGE")
//    }
//
//    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        var itemImage: ImageView
//        var itemTitle: TextView
//        var itemDetail: TextView
//        var languages: TextView
//
//        init {
//            itemImage = itemView.findViewById(R.id.cardIcon)
//            itemTitle = itemView.findViewById(R.id.text_title)
//            itemDetail = itemView.findViewById(R.id.text_description)
//            languages = itemView.findViewById(R.id.text_languages)
//        }
//    }
////
//}

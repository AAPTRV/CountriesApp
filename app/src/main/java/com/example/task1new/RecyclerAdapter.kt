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

class RecyclerAdapter(val dataList: List<Post>) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    var images = R.drawable.icon

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false)
        Log.d(TAG, " ON CREATE VIEW HOLDER STAGE")
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "GET ITEM COUNT STAGE")
        return dataList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        if (dataList[position].capital.isNotEmpty()) {
            holder.itemTitle.text =
                holder.itemView.context.getString(
                    R.string.adapter_capital,
                    dataList[position].capital
                )
            holder.itemDetail.text =
                holder.itemView.context.getString(
                    R.string.adapter_population,
                    dataList[position].population
                )
            holder.languages.text =
                holder.itemView.context.getString(
                    R.string.adapter_languages,
                    dataList[position].languages.convertToCountryNameList()
                )
            holder.itemImage.setImageResource(images)
        }
        Log.d(TAG, "ON BIND VIEW HOLDER STAGE")
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImage: ImageView
        var itemTitle: TextView
        var itemDetail: TextView
        var languages: TextView

        init {
            itemImage = itemView.findViewById(R.id.cardIcon)
            itemTitle = itemView.findViewById(R.id.text_title)
            itemDetail = itemView.findViewById(R.id.text_description)
            languages = itemView.findViewById(R.id.text_languages)
        }
    }
//
}

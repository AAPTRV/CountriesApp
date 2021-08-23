package com.example.task1new.screens.capitals

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.dto.SingleCapitalDto
import com.example.task1new.R
import com.example.task1new.base.adapter.BaseAdapter

// TODO: Also read about snap helper and implement it in a project

class CapitalsAdapter: BaseAdapter<SingleCapitalDto>() {

    inner class CapitalsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvNumber: AppCompatTextView = itemView.findViewById(R.id.capitalNumber)
        var tvCapitalName: AppCompatTextView = itemView.findViewById(R.id.capitalName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_capital, parent, false)
        return CapitalsViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CapitalsViewHolder){
            val item = mDataListInAdapter[position]
            holder.tvNumber.text = position.toString()
            holder.tvCapitalName.text = item.mCapital
        }
    }
}
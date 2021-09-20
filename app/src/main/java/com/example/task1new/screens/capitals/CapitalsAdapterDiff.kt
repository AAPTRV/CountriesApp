package com.example.task1new.screens.capitals

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.dto.SingleCapitalDto
import com.example.task1new.R
import kotlinx.android.extensions.LayoutContainer

class CapitalsAdapterDiff :

    ListAdapter<SingleCapitalDto, CapitalsAdapterDiff.ListViewHolder>(DifferItemCallback()) {

    private var mOnItemClickListener: ((SingleCapitalDto) -> Unit?)? = null

    class DifferItemCallback : DiffUtil.ItemCallback<SingleCapitalDto>() {
        @SuppressLint("DiffUtilEquals")
        override fun areItemsTheSame(
            oldItem: SingleCapitalDto,
            newItem: SingleCapitalDto
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: SingleCapitalDto,
            newItem: SingleCapitalDto
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun submitList(list: MutableList<SingleCapitalDto>?) {
        val refinedList: MutableList<SingleCapitalDto> = mutableListOf()
        list?.let {
            for(item in list){
                if(item.mCapital.isNotEmpty()){
                    refinedList.add(item)
                }
            }
        }
        super.submitList(refinedList)
    }

    inner class ListViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        var tvNumber: AppCompatTextView = itemView.findViewById(R.id.capitalNumber)
        var tvCapitalName: AppCompatTextView = itemView.findViewById(R.id.capitalName)
        fun bind(item: SingleCapitalDto) {
            tvNumber.text = (adapterPosition + 1).toString()
            tvCapitalName.text = item.mCapital
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_capital, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}
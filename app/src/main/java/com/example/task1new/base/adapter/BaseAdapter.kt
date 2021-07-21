package com.example.task1new.base.adapter

import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<ItemType>: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    protected val mDataListInAdapter: MutableList<ItemType> = mutableListOf()

    protected var mOnItemClickListener: ((ItemType) -> Unit?)? = null

    interface OnItemClickListener<ItemType>{
        fun onClick(item: ItemType)
    }

    fun setItemClick(clickListener: (ItemType) -> Unit){
        mOnItemClickListener = clickListener
    }

    override fun getItemCount(): Int = mDataListInAdapter.size

    open fun addListOfItems(list: MutableList<ItemType>){
        mDataListInAdapter.addAll(list)
        notifyDataSetChanged()
    }

    open fun addItem(item: ItemType){
        mDataListInAdapter.add(item)
        notifyItemChanged(mDataListInAdapter.size - 1)
    }

}
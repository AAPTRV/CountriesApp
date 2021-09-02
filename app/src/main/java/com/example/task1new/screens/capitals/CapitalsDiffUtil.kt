package com.example.task1new.screens.capitals

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.dto.SingleCapitalDto
import io.reactivex.rxjava3.core.Single

class CapitalsDiffUtil (
    private val mOldList: List<SingleCapitalDto>,
    private val mNewList: List<SingleCapitalDto>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldList.size
    }

    override fun getNewListSize(): Int {
        return mNewList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldList[oldItemPosition].mCapital == mNewList[newItemPosition].mCapital
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            mOldList[oldItemPosition].mCapital != mNewList[newItemPosition].mCapital -> {
                false
            }
            else -> true
        }
    }
}
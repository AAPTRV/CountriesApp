package com.example.task1new.screens.countryList

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.dto.CountryDto

class CountryListDiffUtil
    (
    private val mOldList: List<CountryDto>,
    private val mNewList: List<CountryDto>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldList.size
    }

    override fun getNewListSize(): Int {
        return mNewList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldList[oldItemPosition].name == mNewList[newItemPosition].name
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            mOldList[oldItemPosition].name != mNewList[newItemPosition].name -> {
                false
            }
            mOldList[oldItemPosition].capital != mNewList[newItemPosition].capital -> {
                false
            }
            mOldList[oldItemPosition].population != mNewList[newItemPosition].population -> {
                false
            }
            mOldList[oldItemPosition].languages != mNewList[newItemPosition].languages -> {
                false
            }
            mOldList[oldItemPosition].flag != mNewList[newItemPosition].flag -> {
                false
            }
            mOldList[oldItemPosition].area != mNewList[newItemPosition].area -> {
                false
            }
            mOldList[oldItemPosition].location != mNewList[newItemPosition].location -> {
                false
            }
            mOldList[oldItemPosition].distance != mNewList[newItemPosition].distance -> {
                false
            }
            else -> true
        }
    }
}
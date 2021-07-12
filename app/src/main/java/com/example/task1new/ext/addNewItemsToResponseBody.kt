package com.example.task1new.ext

import com.example.task1new.dto.PostCountryItemDto

fun MutableList<PostCountryItemDto>.addNewItemsToCountryListDto(newItemDtos: MutableList<PostCountryItemDto>) {
    for (newItem in newItemDtos) {
        var newItemIsInDataList = false
        for (oldItem in this) {
            if (newItem.name == oldItem.name) {
                newItemIsInDataList = true
                break
            }
        }
        if (!newItemIsInDataList) {
            this.add(newItem)
        }
    }
}

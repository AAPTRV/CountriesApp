package com.example.task1new.ext

import com.example.task1new.model.PostCountryItem

fun MutableList<PostCountryItem>.addNewItemsToResponseBody (newItems: MutableList<PostCountryItem>) {
    for(newItem in newItems){
        var newItemIsInDataList = false
        for(oldItem in this){
            if(newItem.name == oldItem.name){
                newItemIsInDataList = true
                break
            }
        }
        if(!newItemIsInDataList){
            this.add(newItem)
        }
    }
}

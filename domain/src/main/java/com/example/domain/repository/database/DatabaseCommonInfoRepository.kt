package com.example.domain.repository.database

import com.example.domain.dto.RoomCommonInfoDto

interface DatabaseCommonInfoRepository {

    fun getAllInfo(): List<RoomCommonInfoDto>

    fun add(entityToDto: RoomCommonInfoDto)

    fun addAll(list: List<RoomCommonInfoDto>)

    fun updateAll(list: List<RoomCommonInfoDto>)

    fun deleteAll(list: List<RoomCommonInfoDto>)

    fun delete(entityToDto: RoomCommonInfoDto)

}
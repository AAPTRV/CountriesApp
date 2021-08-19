package com.example.data.repository.databaseRepo

import com.example.data.room.DBInfo
import com.example.data.room.convertToEntity
import com.example.data.room.convertToRoomDto
import com.example.domain.dto.RoomCommonInfoDto
import com.example.domain.repository.database.DatabaseCommonInfoRepository

class DatabaseCommonInfoRepositoryImpl(private val db: DBInfo) : DatabaseCommonInfoRepository {
    override fun getAllInfo(): List<RoomCommonInfoDto> {
        return db.getCountryCommonInfoDAO().getAllInfo().convertToRoomDto()
    }

    override fun add(entityToDto: RoomCommonInfoDto) {
        db.getCountryCommonInfoDAO().add(entityToDto.convertToEntity())
    }

    override fun addAll(list: List<RoomCommonInfoDto>) {
        db.getCountryCommonInfoDAO().addAll(list.convertToEntity())
    }

    override fun updateAll(list: List<RoomCommonInfoDto>) {
        db.getCountryCommonInfoDAO().updateAll(list.convertToEntity())
    }

    override fun deleteAll(list: List<RoomCommonInfoDto>) {
        db.getCountryCommonInfoDAO().deleteAll(list.convertToEntity())
    }

    override fun delete(entityToDto: RoomCommonInfoDto) {
        db.getCountryCommonInfoDAO().delete(entityToDto.convertToEntity())
    }

}
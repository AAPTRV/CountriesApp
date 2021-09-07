package com.example.data.repository.databaseRepo

import com.example.data.ext.convertInfoToEntity
import com.example.data.ext.convertInfoToRoomDto
import com.example.data.ext.convertToEntity
import com.example.data.room.DBInfo
import com.example.domain.dto.RoomCommonInfoDto
import com.example.domain.repository.database.DatabaseCommonInfoRepository

class DatabaseCommonInfoRepositoryImpl(private val db: DBInfo) : DatabaseCommonInfoRepository {
    override fun getAllInfo(): List<RoomCommonInfoDto> {
        return db.getCountryCommonInfoDAO().getAllInfo().convertInfoToRoomDto()
    }

    override fun add(entityToDto: RoomCommonInfoDto) {
        db.getCountryCommonInfoDAO().add(entityToDto.convertToEntity())
    }

    override fun addAll(list: List<RoomCommonInfoDto>) {
        db.getCountryCommonInfoDAO().addAll(list.convertInfoToEntity())
    }

    override fun updateAll(list: List<RoomCommonInfoDto>) {
        db.getCountryCommonInfoDAO().updateAll(list.convertInfoToEntity())
    }

    override fun deleteAll(list: List<RoomCommonInfoDto>) {
        db.getCountryCommonInfoDAO().deleteAll(list.convertInfoToEntity())
    }

    override fun delete(entityToDto: RoomCommonInfoDto) {
        db.getCountryCommonInfoDAO().delete(entityToDto.convertToEntity())
    }

}
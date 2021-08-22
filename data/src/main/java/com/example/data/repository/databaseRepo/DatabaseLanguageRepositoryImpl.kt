package com.example.data.repository.databaseRepo

import com.example.data.ext.convertLanguageToEntity
import com.example.data.ext.convertLanguageToRoomDto
import com.example.data.ext.convertToEntity
import com.example.data.ext.convertToRoomDto
import com.example.data.room.DBInfo
import com.example.domain.dto.RoomLanguageDto
import com.example.domain.repository.database.DatabaseLanguageRepository

class DatabaseLanguageRepositoryImpl(private val db: DBInfo): DatabaseLanguageRepository {

    override fun getAllInfo(): List<RoomLanguageDto> {
        return db.getLanguageCommonInfoDAO().getAllInfo().convertLanguageToRoomDto()
    }

    override fun getLanguageInfoByCountry(countryName: String): RoomLanguageDto {
        return db.getLanguageCommonInfoDAO().getLanguageInfoByCountry(countryName).convertToRoomDto()
    }

    override fun add(entityToDto: RoomLanguageDto) {
        db.getLanguageCommonInfoDAO().add(entityToDto.convertToEntity())
    }

    override fun addAll(entitiesToDto: List<RoomLanguageDto>) {
        db.getLanguageCommonInfoDAO().addAll(entitiesToDto.convertLanguageToEntity())
    }

    override fun deleteAll(entitiesToDto: List<RoomLanguageDto>) {
        db.getLanguageCommonInfoDAO().deleteAll(entitiesToDto.convertLanguageToEntity())
    }

}
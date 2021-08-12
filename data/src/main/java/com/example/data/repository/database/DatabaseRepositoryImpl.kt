package com.example.data.repository.database

import com.example.data.room.DBInfo
import com.example.domain.dto.LanguageDto
import com.example.domain.repository.DatabaseRepository
import com.example.task1new.ext.convertToLanguagesDto

class DatabaseRepositoryImpl(private val db: DBInfo) : DatabaseRepository {

    override fun getAllInfo(): List<LanguageDto> {
        val data = db.getLanguageCommonInfoDAO().getAllInfo()
        return data[0].convertToLanguagesDto()
    }

    override fun getLanguageInfoByCountry(countryName: String): List<LanguageDto> =
        db.getLanguageCommonInfoDAO().getLanguageInfoByCountry(countryName).convertToLanguagesDto()

//    override fun add(entity: CountryDatabaseLanguageInfoEntity) {
//        db.getLanguageCommonInfoDAO().add(entity = entity)
//    }
//
//    override fun addAll(entities: List<CountryDatabaseLanguageInfoEntity>) {
//        db.getLanguageCommonInfoDAO().addAll(entities = entities)
//    }
//
//    override fun deleteAll(entities: List<CountryDatabaseLanguageInfoEntity>) {
//        db.getLanguageCommonInfoDAO().deleteAll(entities)
//    }
}
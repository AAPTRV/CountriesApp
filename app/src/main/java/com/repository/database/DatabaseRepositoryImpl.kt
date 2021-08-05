package com.repository.database

import com.example.task1new.dto.LanguageDto
import com.example.task1new.ext.convertToLanguagesDto
import com.example.task1new.room.CountryDatabaseLanguageInfoEntity
import com.example.task1new.room.DBInfo

class DatabaseRepositoryImpl(private val db: DBInfo) : DatabaseRepository {

    override fun getAllInfo(): List<LanguageDto> {
        val data = db.getLanguageCommonInfoDAO().getAllInfo()
        return data[0].convertToLanguagesDto()
    }

    override fun getLanguageInfoByCountry(countryName: String): List<LanguageDto> =
        db.getLanguageCommonInfoDAO().getLanguageInfoByCountry(countryName).convertToLanguagesDto()

    override fun add(entity: CountryDatabaseLanguageInfoEntity) {
        db.getLanguageCommonInfoDAO().add(entity = entity)
    }

    override fun addAll(entities: List<CountryDatabaseLanguageInfoEntity>) {
        db.getLanguageCommonInfoDAO().addAll(entities = entities)
    }

    override fun deleteAll(entities: List<CountryDatabaseLanguageInfoEntity>) {
        db.getLanguageCommonInfoDAO().deleteAll(entities)
    }
}
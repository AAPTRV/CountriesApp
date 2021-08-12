package com.example.data.repository.cache

import com.example.data.network.Retrofit
import com.example.domain.dto.CountryDto
import com.example.domain.repository.CacheRepository
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.FlowableEmitter
import io.reactivex.rxjava3.core.FlowableOnSubscribe

class CacheRepositoryImpl : CacheRepository {

    private val mCachedList: MutableList<CountryDto> = mutableListOf()
    private val mCachedFilteredList: MutableList<CountryDto> = mutableListOf()

    override fun addAllCountries(list: MutableList<CountryDto>): Flowable<MutableList<CountryDto>> {
        return Flowable.create({
            updateList(mCachedList, list, it)
        }, BackpressureStrategy.LATEST)
    }

//    init {
//        Flowable.just("database.getData()")
//
//        //1 - "database.getData()"
//        //2 - Flowable.just(result)
//
//        Flowable.create<String>({
//            val result = "database.getData()"
//            it.onNext(result)
//            it.onComplete()
//        }, BackpressureStrategy.LATEST)
//            .flatMap {
//                Retrofit.getCountriesApi().getCountryByName(it).map { data -> Pair(it, data) }
//            }
//            .flatMap {
//                Retrofit.getCountriesApi().getCountryList()
//            }
//            .map {
//                Thread.sleep(10000)
//            }
//
//        //1 - Flowable
//        //2 - Subscribe
//        //3 - Execute
//
//        //1 - Flowable.create(db.getData) -> List<DataBaseItem>
//        //2 - FlatMap -> Flowable1 -> Flowable2 -> List<DataBaseItem> ->
//        //2 - Flowable.create(db.getData) -> List<DataBaseItem>
//
//        // 1 -> (12s)
//        // 5s -> 2 -> buffer -> 5s -> 3 -> 2 delete -> 3 -> buffer
//    }

    override fun addFilteredCountries(list: MutableList<CountryDto>): Flowable<MutableList<CountryDto>> {
        return Flowable.create({
            updateList(mCachedFilteredList, list, it)
        }, BackpressureStrategy.LATEST)
    }

    override fun getAllCountries(): Flowable<MutableList<CountryDto>> {
        return Flowable.just(mCachedList)
    }

    override fun getFilteredCountries(): Flowable<MutableList<CountryDto>> {
        return Flowable.just(mCachedFilteredList)
    }

    private fun updateList(
        currentList: MutableList<CountryDto>,
        list: MutableList<CountryDto>,
        it: @NonNull FlowableEmitter<MutableList<CountryDto>>
    ) {
        currentList.clear()
        currentList.addAll(list)
        it.onNext(list)
        it.onComplete()
    }

}
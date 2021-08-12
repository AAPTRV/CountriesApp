package com.example.data.interactor

import com.example.domain.dto.CountryDto
import com.example.domain.interactor.BaseInteractor
import com.example.domain.interactor.CountryInteractor
import com.example.domain.repository.CacheRepository
import com.example.domain.repository.DatabaseRepository
import com.example.domain.repository.FilterRepository
import com.example.domain.repository.NetworkRepository
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.subjects.BehaviorSubject

class CountryInteractorImpl(
    mFilterRepository: FilterRepository,
    mDatabaseRepository: DatabaseRepository,
    private val mNetworkRepository: NetworkRepository,
    private val mCacheRepository: CacheRepository
) : BaseInteractor(), CountryInteractor {

    private var mLastNetworkRequestTime: Long = 0
    private val mCountrySubject = BehaviorSubject.createDefault(mutableListOf<CountryDto>())

    override fun getCountryChanel(): Flowable<MutableList<CountryDto>> = mCountrySubject.toFlowable(BackpressureStrategy.LATEST)

    /**
     * this method only triggers data update over mCountrySubject, do not expect it to return new list directly
     */
    override fun generateAllCountries(): Flowable<Any> {
        return getTimeSinceLastUpdate()
            .flatMap { isMoreThenMinute ->
                return@flatMap if (isMoreThenMinute) {
                    // TODO: 12.08.2021 this is bad, need to remove external reference
                    mLastNetworkRequestTime = System.currentTimeMillis()
                    mNetworkRepository.getAllCountries()
                        .flatMap { mCacheRepository.addAllCountries(it) }
                } else {
                    mCacheRepository.getAllCountries()
                }
            }.doOnNext {
                mCountrySubject.onNext(it)
            }
            .map { Any() }
    }

    override fun getCountriesByName(name: String): Flowable<Any> {
        return getTimeSinceLastUpdate()
            .flatMap { isMoreThenMinute ->
                return@flatMap if (isMoreThenMinute) {
                    mLastNetworkRequestTime = System.currentTimeMillis()
                    mNetworkRepository.getCountryByName(name)
                        .flatMap { mCacheRepository.addFilteredCountries(it) }
                } else {
                    mCacheRepository.getAllCountries().map {
                        it.filter { item -> item.name.contains(name) }.toMutableList()
                    }
                }
                    .doOnNext { mCountrySubject.onNext(it) }
                    .map { Any() }
            }
    }

    private fun getTimeSinceLastUpdate() =
        Flowable.just(mLastNetworkRequestTime == 0L || System.currentTimeMillis() - mLastNetworkRequestTime > 60000)

}
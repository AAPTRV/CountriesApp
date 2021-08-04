package com.example.task1new.base.mvvm

import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable

fun <T> executeJob(job: Flowable<T>, outcome: MutableLiveData<Outcome<T>>): Disposable{
    outcome.loading
}

fun <T> executeJob(job: Single<T>, outcome: MutableLiveData<Outcome<T>>): Disposable{

}
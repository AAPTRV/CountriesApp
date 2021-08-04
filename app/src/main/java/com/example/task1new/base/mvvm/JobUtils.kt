package com.example.task1new.base.mvvm

import androidx.lifecycle.MutableLiveData
import com.example.task1new.base.mvvm.Outcome.Companion.loading
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable

fun <T> executeJob(job: Flowable<T>, outcome: MutableLiveData<Outcome<T>>): Disposable{
    outcome.loading(true)
    return job.executeOnIoThread()
        .subscribe(
            {outcome.next(it)}
        )
}

fun <T> executeJob(job: Single<T>, outcome: MutableLiveData<Outcome<T>>): Disposable{

}

fun <T> MutableLiveData<Outcome<T>>.loading(isLoading: Boolean){
    this.value = Outcome.loading(isLoading)
}

fun <T> MutableLiveData<Outcome<T>>.success(t: T) {
    with(this){
        loading(false)
        value = Outcome.success(t)
    }
}
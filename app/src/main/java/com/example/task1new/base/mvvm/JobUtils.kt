package com.example.task1new.base.mvvm

import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.Disposable

fun <T> executeJob(job: Flowable<T>, outcome: MutableLiveData<Outcome<T>>): Disposable {
    outcome.loading(true)
    return job.executeOnIoThread()
        .subscribe(
            { outcome.next(it) }, { outcome.failed(it) }
        )
}

fun <T> MutableLiveData<Outcome<T>>.loading(isLoading: Boolean) {
    this.value = Outcome.loading(isLoading)
}

fun <T> MutableLiveData<Outcome<T>>.success(t: T) {
    with(this) {
        loading(false)
        value = Outcome.success(t)
    }
}

fun <T> MutableLiveData<Outcome<T>>.next(t: T) {
    with(this) {
        loading(false)
        value = Outcome.next(t)
    }
}

fun <T> MutableLiveData<Outcome<T>>.failed(e: Throwable) {
    with(this) {
        loading(false)
        value = Outcome.failure(e)
    }
}
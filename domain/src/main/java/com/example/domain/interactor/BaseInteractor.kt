package com.example.domain.interactor

import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

open class BaseInteractor {

    private val mCompositeDisposable = CompositeDisposable()

    protected fun addDisposable(disposable: Disposable?) {
        mCompositeDisposable.add(disposable)
    }

    protected fun <T> inBackground(dest: Flowable<T>): Flowable<T> {
        return dest.subscribeOn(Schedulers.io())
    }

    protected fun releaseResources() {
        mCompositeDisposable.clear()
    }

}
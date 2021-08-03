package com.example.task1new.base.mvp

import android.content.ContentValues.TAG
import android.util.Log
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

abstract class BaseMvpPresenter<View : BaseMvpView> {

    private var mView: View? = null
    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    fun attachView(view: View) {
        mView = view
    }

    protected open fun getView(): View? = mView

    fun detachView() {
        mView = null
    }

    fun onDestroyView() {
        mCompositeDisposable.clear()
    }

    fun addDisposable(disposable: Disposable) {
        mCompositeDisposable.add(disposable)
    }

    fun <Data> inBackground(flowable: Flowable<Data>): Flowable<Data> {
        return flowable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())

    }

    fun <Data> handleProgress(flowable: Flowable<Data>, isRefresh: Boolean): Flowable<Data> {
        return flowable
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap {
                Flowable.just(it).doOnSubscribe {
                    if (!isRefresh) {
                        getView()?.showProgress()
                        println("GET VIEW SHOW PROGRESS")
                    }
                }
            }.doOnNext {
                getView()?.hideProgress()
                println("GET VIEW HIDE PROGRESS")
            }
            .observeOn(Schedulers.io())
    }
}

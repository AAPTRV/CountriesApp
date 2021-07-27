package com.example.task1new.base.mvvm

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers


/**
 * Extension function to subscribe [Single] on the IO thread and observe on the UI thread.
 * */
fun <T> Single<T>.executeOnIoThread(): Single<T> {
	return this.subscribeOn(Schedulers.io())
		.observeOn(AndroidSchedulers.mainThread())
}


/**
 * Extension function to subscribe [Completable] on the IO thread and observe on the UI thread.
 * */
fun Completable.executeOnIoThread(): Completable {
	return this.subscribeOn(Schedulers.io())
		.observeOn(AndroidSchedulers.mainThread())
}

/**
 * Extension function to subscribe [Flowable] on the IO thread and observe on the UI thread.
 * */
fun <T> Flowable<T>.executeOnIoThread(): Flowable<T> {
	return this.subscribeOn(Schedulers.io())
		.observeOn(AndroidSchedulers.mainThread())
}

/**
 * Extension function to subscribe [Observable] on the IO thread and observe on the UI thread.
 * */
fun <T> Observable<T>.executeOnIoThread(): Observable<T> {
	return this.subscribeOn(Schedulers.io())
		.observeOn(AndroidSchedulers.mainThread())
}


/**
 * Extension function to add a Disposable to a CompositeDisposable
 */
fun Disposable.addToComposite(compositeDisposable: CompositeDisposable): Disposable {
	compositeDisposable.add(this)
	return this
}

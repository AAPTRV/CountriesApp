package com.example.task1new.base.mvvm

import androidx.annotation.CallSuper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.task1new.dto.CountryDto
import io.reactivex.rxjava3.disposables.CompositeDisposable

open class BaseViewModel(protected  val savedStateHandle: SavedStateHandle): ViewModel() {

    protected val mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        mCompositeDisposable.clear()
    }
}
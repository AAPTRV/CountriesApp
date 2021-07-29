package com.example.task1new.base.mvvm

import androidx.annotation.CallSuper
import androidx.lifecycle.*
import com.example.task1new.OkRetrofit
import com.example.task1new.dto.LanguageDto
import com.example.task1new.dto.PostCountryItemDto
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

open class BaseViewModel(protected val savedStateHandle: SavedStateHandle) : ViewModel() {

    //val vm: BaseViewModel by viewModels()
    //val mCountryLiveData = MutableLiveData<Outcome<PostCountryItemDto>>()
    protected val mCompositeDisposable: CompositeDisposable = CompositeDisposable()


    @CallSuper
    override fun onCleared() {
        super.onCleared()
        mCompositeDisposable.clear()
    }

}
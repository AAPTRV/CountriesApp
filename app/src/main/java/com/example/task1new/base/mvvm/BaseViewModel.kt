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

//    class MVPActivity : AppCompatActivity(R.layout.mvp_activity), KoinScopeComponent {
//
//        // Create scope
//        override val scope: Scope by lazy { newScope() }
//
//        // Inject presenter with org.koin.core.scope.inject extension
//        // also can use directly the scope: scope.inject<>()
//        val presenter: ScopedPresenter by inject()
//
//        // Don't forget to close it when finish
//        override fun onDestroy() {
//            super.onDestroy()
//            scope.close()
//        }
//    }

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        mCompositeDisposable.clear()
    }

}
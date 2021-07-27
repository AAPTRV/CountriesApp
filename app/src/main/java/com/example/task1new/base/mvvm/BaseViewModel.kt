package com.example.task1new.base.mvvm

import androidx.annotation.CallSuper
import androidx.lifecycle.*
import com.example.task1new.OkRetrofit
import com.example.task1new.dto.LanguageDto
import com.example.task1new.dto.PostCountryItemDto
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

class BaseViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    //val vm: BaseViewModel by viewModels()
    //val mCountryLiveData = MutableLiveData<Outcome<PostCountryItemDto>>()
    val mCompositeDisposable: CompositeDisposable = CompositeDisposable()
    val mCountryLiveData = savedStateHandle.getLiveData<Outcome<PostCountryItemDto>>("countryDto")

    fun getCountryByName() {
        mCompositeDisposable.add(
            executeJob(
                OkRetrofit.jsonPlaceHolderApi.getCountryByName("belarus")
                    .map { it[0].convertToPostCountryItemDto() }, mCountryLiveData
            )
        )
    }

    fun getFilteredData(): LiveData<String> = savedStateHandle.getLiveData<String>("query").switchMap { query ->
        MutableLiveData()//repository.getFilteredData(query)
    }

    fun setQuery(query: String) {
        savedStateHandle["countryName"] = query
    }

    fun setCountryName(name: String) {
        savedStateHandle["countryName"] = name
    }

    fun setPostCountryItemDto(dto: PostCountryItemDto) {
        savedStateHandle["countryDto"] = dto
    }

    fun getPostCountryItemDto(): Outcome<PostCountryItemDto>? =
        savedStateHandle["countryDto"]

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